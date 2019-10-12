package com.outside.kotlineye.ui.activity


import android.annotation.TargetApi
import android.os.Build
import android.transition.Transition
import android.util.Log
import android.widget.ImageView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.gyf.immersionbar.ktx.immersionBar
import com.outside.kotlineye.R
import com.outside.kotlineye.base.BaseApplication
import com.outside.kotlineye.base.BaseMvpActivity
import com.outside.kotlineye.common.Constants
import com.outside.kotlineye.mvp.model.bean.HomeBean
import com.outside.kotlineye.mvp.presenter.VideoDetailPresenter
import com.outside.kotlineye.mvp.view.VideoDetailView
import com.outside.kotlineye.ui.adapter.VideoDetailAdapter
import com.outside.kotlineye.utils.GlideUtils
import com.outside.kotlineye.utils.WatchHistoryUtils
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack
import com.shuyu.gsyvideoplayer.utils.OrientationUtils

import kotlinx.android.synthetic.main.activity_video_detail.*
import org.jetbrains.anko.toast
import java.text.SimpleDateFormat
import java.util.*
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Handler
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.outside.kotlineye.utils.CleanLeakUtils
import com.outside.kotlineye.utils.StatusBarUtil
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer
import kotlin.collections.ArrayList


class VideoDetailActivity : BaseMvpActivity<VideoDetailPresenter>(), VideoDetailView {


    private var isPlay: Boolean = false
    private var isPause: Boolean = false

    private var isTransition: Boolean = false

    private var transition: Transition? = null

    private lateinit var itemData: HomeBean.Issue.Item

    private var itemList = ArrayList<HomeBean.Issue.Item>()

    private val mFormat by lazy { SimpleDateFormat("yyyyMMddHHmmss"); }

    private var orientationUtils: OrientationUtils? = null

    private val mAdapter by lazy { VideoDetailAdapter(this, itemList) }

    override fun injectComponent() {
        activityComponent.inject(this)
        mPresenter.mView = this
    }


    companion object {
        const val IMG_TRANSITION = "IMG_TRANSITION"
        const val TRANSITION = "TRANSITION"
    }


    override fun getLayoutId(): Int {
        return R.layout.activity_video_detail
    }


    override fun initView() {


        //状态栏透明和间距处理
        StatusBarUtil.immersive(this)

        itemData = intent.getSerializableExtra(Constants.BUNDLE_VIDEO_DATA) as HomeBean.Issue.Item

        Log.e("111111--itemData>>", itemData.toString())

        isTransition = intent.getBooleanExtra(TRANSITION, false)

        saveWatchVideoHistoryInfo(itemData)

        initTransition()
        initVideoViewConfig()

        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.adapter = mAdapter

        //设置相关视频Item的点击事件
        mAdapter.onItemClick { t, _ ->
            mPresenter.loadVideoInfo(t)
        }


//        mRefreshLayout.setOnRefreshListener {
//            loadVideoInfo()
//        }

    }

    /**
     * description: 初始化 VideoView 的配置
     * params:
     * creatTime:  2019/10/11 10:30
     */
    private fun initVideoViewConfig() {
        //设置旋转
        val orientationUtils = OrientationUtils(this, mVideoView)
        //是否旋转
        mVideoView.isRotateViewAuto = false
        //是否可以滑动调整
        mVideoView.setIsTouchWiget(true)

        //增加封面
        val imageView = ImageView(this)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        GlideUtils.loadImage(this, itemData.data?.cover?.feed!!, imageView)
        mVideoView.thumbImageView = imageView

        mVideoView.setVideoAllCallBack(object : GSYSampleCallBack() {
            override fun onPrepared(url: String?, vararg objects: Any?) {
                super.onPrepared(url, *objects)
                Log.e("VideoDetail", "--onPrepared--")
                //开始播放了，才能旋转和全屏
                orientationUtils?.isEnable = true
                isPlay = true
            }

            override fun onAutoComplete(url: String?, vararg objects: Any?) {
                super.onAutoComplete(url, *objects)
                Log.e("VideoDetail", "--onAutoComplete--")
            }

            override fun onPlayError(url: String?, vararg objects: Any?) {
                super.onPlayError(url, *objects)
                toast("播放失败")
            }

            override fun onEnterFullscreen(url: String?, vararg objects: Any?) {
                super.onEnterFullscreen(url, *objects)
                Log.e("VideoDetail", "--onEnterFullscreen--")
            }

            override fun onQuitFullscreen(url: String?, vararg objects: Any?) {
                super.onQuitFullscreen(url, *objects)
                Log.e("VideoDetail", "--onQuitFullscreen--")
                //列表返回的样式判断
                orientationUtils?.backToProtVideo()
            }
        })
        //设置返回按键功能
        mVideoView.backButton.setOnClickListener { onBackPressed() }
        //设置全屏按键功能
        mVideoView.fullscreenButton.setOnClickListener {
            //设置横屏
            orientationUtils?.resolveByClick()
            //第一个true是否需要隐藏actionBar,第二个true是否需要隐藏statusBar
            mVideoView.startWindowFullscreen(this, true, true)
        }

        //锁屏事件
        mVideoView.setLockClickListener { _, lock ->
            orientationUtils?.isEnable = !lock
        }

    }

    private fun saveWatchVideoHistoryInfo(watchItem: HomeBean.Issue.Item) {
        //保存之前要先查询sp中是否有该value的记录，有则删除，这样保证搜索历史记录不会有重复条目
        val historyMap = WatchHistoryUtils.getAll(
            Constants.FILE_WATCH_HISTORY_NAME,
            BaseApplication.context
        ) as Map<*, *>
        for ((key, _) in historyMap) {
            if (watchItem == WatchHistoryUtils.getObject(
                    Constants.FILE_WATCH_HISTORY_NAME,
                    BaseApplication.context,
                    key as String
                )
            ) {
                WatchHistoryUtils.remove(
                    Constants.FILE_WATCH_HISTORY_NAME,
                    BaseApplication.context,
                    key
                )
            }
        }
        WatchHistoryUtils.putObject(
            Constants.FILE_WATCH_HISTORY_NAME, BaseApplication.context, watchItem,
            "" + mFormat.format(Date())
        )

    }

    private fun initTransition() {
        if (isTransition && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            postponeEnterTransition()
            ViewCompat.setTransitionName(mVideoView, IMG_TRANSITION)
            addTransitionListener()
            startPostponedEnterTransition()
        } else {
            loadVideoInfo()
        }


    }

    override fun hideLoading() {
//        mRefreshLayout.finishRefresh()
    }


    /**
     * description: 加载视频信息
     * params:
     * creatTime:  2019/10/11 9:26
     */
    private fun loadVideoInfo() {
        mPresenter.loadVideoInfo(itemData)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun addTransitionListener() {
        transition = window.sharedElementEnterTransition
        transition?.addListener(object : Transition.TransitionListener {
            override fun onTransitionEnd(p0: Transition?) {
                Log.e("VideoDetail", "--onTransitionEnd--")
                loadVideoInfo()
                transition?.removeListener(this)
            }

            override fun onTransitionResume(p0: Transition?) {
            }

            override fun onTransitionPause(p0: Transition?) {
            }

            override fun onTransitionCancel(p0: Transition?) {
            }

            override fun onTransitionStart(p0: Transition?) {
            }

        })

    }


    /**
     * 设置播放视频 URL
     */
    override fun setVideo(url: String) {
        mVideoView.setUp(url, false, "")
        //开始自动播放
        mVideoView.startPlayLogic()
    }

    /**
     * 设置视频信息
     */
    override fun setVideoInfo(itemInfo: HomeBean.Issue.Item) {
        itemData = itemInfo
        mAdapter.addOneData(itemInfo)

        //请求相关的最新等视频
        mPresenter.requestRelatedVideo(itemInfo.data?.id ?: 0)
    }

    /**
     * 设置相关的数据视频
     */
    override fun setRecentRelatedVideo(itemList: ArrayList<HomeBean.Issue.Item>) {
        mAdapter.addDataAll(itemList)
        this.itemList = itemList
    }

    /**
     * 设置背景颜色
     */
    override fun setBackground(url: String) {
        Glide.with(this)
            .load(url)
            .centerCrop()
            .format(DecodeFormat.PREFER_ARGB_8888)
            .transition(DrawableTransitionOptions().crossFade())
            .into(mVideoBackground)
    }

    /**
     * 设置错误信息
     */
    override fun setErrorMsg(errorMsg: String) {
        toast(errorMsg)
    }


    /**
     * 监听返回键
     */
    override fun onBackPressed() {

        //先返回正常状态
        if (orientationUtils?.screenType == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            mVideoView.fullscreenButton.performClick()
            return
        }

//        orientationUtils?.backToProtVideo()
//        if (GSYVideoManager.backFromWindowFull(this)) {
//            return
//        }

        //释放所有
        mVideoView.setVideoAllCallBack(null)
        GSYVideoManager.releaseAllVideos()
        if (isTransition && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            super.onBackPressed()
        } else {
            Handler().postDelayed(Runnable {
                finish()
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
            }, 500)
        }
    }

    private fun getCurPlay(): GSYVideoPlayer {
        return if (mVideoView.fullWindowPlayer != null) {
            mVideoView.fullWindowPlayer
        } else mVideoView
    }


    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (isPlay && !isPause) {
            mVideoView.onConfigurationChanged(this, newConfig, orientationUtils)
        }
    }

    override fun onPause() {
        super.onPause()
        getCurPlay().onVideoPause()
        isPause = true
    }

    override fun onResume() {
        super.onResume()
        getCurPlay().onVideoResume()
        isPause = false
    }


    override fun onDestroy() {
        CleanLeakUtils.fixInputMethodManagerLeak(this)
        super.onDestroy()
        GSYVideoManager.releaseAllVideos()
        orientationUtils?.releaseListener()
    }

}
