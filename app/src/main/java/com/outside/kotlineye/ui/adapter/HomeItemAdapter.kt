package com.outside.kotlineye.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.outside.kotlineye.R
import com.outside.kotlineye.mvp.model.bean.HomeBean
import com.outside.kotlineye.ui.fragment.HomeFragment
import com.outside.kotlineye.utils.BannerImageLoader
import com.outside.kotlineye.utils.DateUtils
import com.outside.kotlineye.utils.GlideUtils
import com.youth.banner.Banner
import com.youth.banner.BannerConfig
import io.reactivex.Observable


/**
 * className:    HomeItemAdapter
 * description:  描述
 * author:       CLW2018
 * creatTime:    2019/9/17 14:14
 */

class HomeItemAdapter(val context: Context,val homeFragment: HomeFragment) :
    BaseRecyclerViewAdapter<HomeBean.Issue.Item>(context) {

    protected var mInflater: LayoutInflater? = null

    // banner 作为 RecyclerView 的第一项
    var bannerItemSize = 0

    init {
        mInflater = LayoutInflater.from(mContext)
    }


    companion object {
        private const val ITEM_TYPE_BANNER = 1    //Banner 类型
        private const val ITEM_TYPE_TEXT_HEADER = 2   //textHeader
        private const val ITEM_TYPE_CONTENT = 3    //item
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            ITEM_TYPE_BANNER ->
                ViewHolder(inflaterView(R.layout.item_home_banner, parent))
            ITEM_TYPE_TEXT_HEADER ->
                ViewHolder(inflaterView(R.layout.item_home_header, parent))
            else ->
                ViewHolder(inflaterView(R.layout.item_home_content, parent))
        }

    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            //Banner
            ITEM_TYPE_BANNER -> {

                val bannerItemData: ArrayList<HomeBean.Issue.Item> =
                    dataList.take(bannerItemSize).toCollection(ArrayList())
                val bannerFeedList = ArrayList<String>()
                val bannerTitleList = ArrayList<String>()
                //取出banner 显示的 img 和 Title
                Observable.fromIterable(bannerItemData)
                    .subscribe { list ->
                        bannerFeedList.add(list.data?.cover?.feed ?: "")
                        bannerTitleList.add(list.data?.title ?: "")
                    }

                //设置 banner
                with(holder) {
                    getView<Banner>(R.id.mBanner).run {
                        setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE)
                        setImages(bannerFeedList)
                        setImageLoader(BannerImageLoader())
                        setBannerTitles(bannerTitleList)
                        setDelayTime(3000)
                        isAutoPlay(true)
                        setIndicatorGravity(BannerConfig.RIGHT)
                        start()

                    }

                    getView<Banner>(R.id.mBanner).setOnBannerListener {

                        homeFragment.goToVideoPlayer(
                            getView<Banner>(R.id.mBanner),
                            bannerItemData[it]
                        )
                    }
                }


            }
            //TextHeader
            ITEM_TYPE_TEXT_HEADER -> {
                holder.setText(
                    R.id.tvHeader,
                    dataList[position + bannerItemSize - 1].data?.text ?: ""
                )
            }

            //content
            ITEM_TYPE_CONTENT -> {
                setVideoItem(holder, dataList[position + bannerItemSize - 1])
            }


        }

    }

    /**
     * 加载 content item
     */
    private fun setVideoItem(holder: ViewHolder, item: HomeBean.Issue.Item) {
        val itemData = item.data

        val defAvatar = R.drawable.placeholder_banner
        val cover = itemData?.cover?.feed
        var avatar = itemData?.author?.icon
        var tagText: String? = "#"

        // 作者出处为空，就显获取提供者的信息
        if (avatar.isNullOrEmpty()) {
            avatar = itemData?.provider?.icon
        }
        // 加载封页图
        GlideUtils.loadImagetCrossFade(mContext, cover!!, holder.getView(R.id.iv_cover_feed))


        // 如果提供者信息为空，就显示默认
        if (avatar.isNullOrEmpty()) {
            GlideUtils.loadImagetCrossFade(
                mContext,
                defAvatar.toString(),
                holder.getView(R.id.iv_avatar)
            )
        } else {
            GlideUtils.loadImagetCrossFade(mContext, avatar!!, holder.getView(R.id.iv_avatar))
        }
        holder.setText(R.id.tv_title, itemData?.title ?: "")

        //遍历标签
        itemData?.tags?.take(4)?.forEach {
            tagText += (it.name + "/")
        }
        // 格式化时间
        val timeFormat = DateUtils.durationFormat(itemData?.duration)

        tagText += timeFormat

        holder.setText(R.id.tv_tag, tagText!!)

        holder.setText(R.id.tv_category, "#" + itemData?.category)



        holder.itemView.setOnClickListener {
            mClickListener?.let {
                it(holder.getView(R.id.iv_cover_feed), item)
            }
        }
    }

    var mClickListener:((view: View, item: HomeBean.Issue.Item) -> Unit)? = null

    fun setClick(listener: ((view: View, item: HomeBean.Issue.Item) -> Unit)) {
        this.mClickListener = listener
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            position == 0 -> ITEM_TYPE_BANNER
            dataList[position + bannerItemSize - 1].type == "textHeader" -> ITEM_TYPE_TEXT_HEADER
            else -> ITEM_TYPE_CONTENT
        }

    }

    /**
     * 设置 Banner 大小
     */
    fun setBannerSize(count: Int) {
        bannerItemSize = count
    }


    /**
     *  得到 RecyclerView Item 数量（Banner 作为一个 item）
     */
    override fun getItemCount(): Int {
        return when {
            dataList.size > bannerItemSize -> dataList.size - bannerItemSize + 1
            dataList.isEmpty() -> 0
            else -> 1
        }
    }

    /**
     * 加载布局
     */
    private fun inflaterView(mLayoutId: Int, parent: ViewGroup): View {
        //创建view
        val view = mInflater?.inflate(mLayoutId, parent, false)
        return view ?: View(parent.context)
    }





}