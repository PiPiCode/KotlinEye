package com.outside.kotlineye.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gyf.immersionbar.ImmersionBar

import com.outside.kotlineye.R
import com.outside.kotlineye.base.BaseMvpFragment
import com.outside.kotlineye.common.Constants
import com.outside.kotlineye.mvp.model.bean.HomeBean
import com.outside.kotlineye.mvp.presenter.HomePresenter
import com.outside.kotlineye.mvp.view.HomeView
import com.outside.kotlineye.ui.activity.VideoDetailActivity
import com.outside.kotlineye.ui.adapter.HomeItemAdapter
import com.outside.kotlineye.utils.DateUtils
import com.outside.kotlineye.widgets.MultiStateView
import com.scwang.smart.refresh.header.MaterialHeader
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.ArrayList

/**
 * className:    HomeFragment
 * description:  描述
 * author:       CLW2018
 * creatTime:    2019/9/17 9:35
 */

class HomeFragment : BaseMvpFragment<HomePresenter>(), HomeView {

    private var mTitle: String? = null

    override fun initImmersionBar() {
        ImmersionBar.with(this).transparentBar().init()
    }

    companion object {
        fun getInstance(title: String): HomeFragment {
            val fragment = HomeFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.mTitle = title
            return fragment
        }
    }

    private var mAdapter: HomeItemAdapter? = null
    private var num: Int = 1
    private var loadingMore = false
    private var isRefresh = false

    private val linearLayoutManager by lazy {
        LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    }


    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun injectComponent() {

        fragmentComponent.inject(this)
        mPresenter.mView = this

    }

    override fun initView() {
        mAdapter = HomeItemAdapter(context!!,this)
        mRecyclerView.adapter = mAdapter
        mRecyclerView.layoutManager = LinearLayoutManager(context)
        mRecyclerView.itemAnimator = DefaultItemAnimator()
        mRefreshLayout.setEnableHeaderTranslationContent(false)
        mRefreshLayout.setRefreshHeader(MaterialHeader(context))


        mRefreshLayout.setOnRefreshListener {
            //refreshlayout.finishRefresh(2000/*,false*/)//传入false表示刷新失败
            isRefresh = true
            mPresenter.requestHomeData(num)
        }



        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val childCount = mRecyclerView.childCount
                    val itemCount = mRecyclerView.layoutManager?.itemCount
                    val firstVisibleItem =
                        (mRecyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                    if (firstVisibleItem + childCount == itemCount) {
                        if (!loadingMore) {
                            loadingMore = true
                            mPresenter.loadMoreData()
                        }
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                Log.e("onScrolled", "dx:$dx>>dy:$dy")
                val currentVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition()

                if (currentVisibleItemPosition == 0) {
                    Log.e("onScrolled", "0")
                    //背景设置为透明
                    toolbar.setBackgroundResource(R.color.color_translucent)
                    iv_search.setImageResource(R.drawable.ic_action_search_white)

                    tv_header_title.text = ""
                } else {
                    Log.e("onScrolled", "!!0")
                    Log.e("onScrolled", "currentVisibleItemPosition:$currentVisibleItemPosition")
                    if (mAdapter?.dataList!!.size > 1) {
                        Log.e("onScrolled", ">1")
                        toolbar.setBackgroundResource(R.color.color_title_bg)
                        iv_search.setImageResource(R.drawable.ic_action_search_black)
                        val itemList = mAdapter!!.dataList
                        val item =
                            itemList[currentVisibleItemPosition + mAdapter!!.bannerItemSize - 1]
                        Log.e("item:", item.toString())
                        if (item.type == "textHeader") {
                            Log.e("onScrolled", ">1:textHeader" + item.data?.text)
                            tv_header_title.text = item.data?.text
                        } else {
                            Log.e(
                                "onScrolled",
                                ">1:!!textHeader" + DateUtils.brunchFormat(item.data?.date)
                            )
                            tv_header_title.text = DateUtils.brunchFormat(item.data?.date)
                        }
                    }
                }
            }

        })

        mAdapter?.let {
            it.setClick { view, item ->
                goToVideoPlayer(view, item)
            }
        }
    }


    fun goToVideoPlayer(view: View, itemData: HomeBean.Issue.Item) {

        activity?.let {
            val intent = Intent(activity, VideoDetailActivity::class.java)
            intent.putExtra(Constants.BUNDLE_VIDEO_DATA, itemData)
            intent.putExtra(VideoDetailActivity.TRANSITION, true)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                val pair = Pair(view, VideoDetailActivity.IMG_TRANSITION)
                val activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    it, pair
                )
                ActivityCompat.startActivity(it, intent, activityOptions.toBundle())
            } else {
                it.startActivity(intent)
                it.overridePendingTransition(R.anim.anim_in, R.anim.anim_out)
            }
        }

    }


    override fun lazyLoad() {
        mPresenter.requestHomeData(num)
    }

    override fun setHomeData(homeBean: HomeBean) {
        mRefreshLayout.finishRefresh()
        if (homeBean != null) {
            multipleStatusView.viewState = MultiStateView.ViewState.CONTENT
            mAdapter?.setBannerSize(homeBean.issueList[0].count)
            mAdapter?.setData(homeBean.issueList[0].itemList)

        } else {
            multipleStatusView.viewState = MultiStateView.ViewState.EMPTY
        }
    }

    override fun setMoreData(newItemList: ArrayList<HomeBean.Issue.Item>) {
        mRefreshLayout.finishLoadMore()
        loadingMore = false
        mAdapter?.addData(newItemList)
    }


}