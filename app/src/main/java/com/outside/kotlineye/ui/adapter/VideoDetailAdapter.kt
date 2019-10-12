package com.outside.kotlineye.ui.adapter

import android.content.Context
import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.outside.kotlineye.R
import com.outside.kotlineye.base.BaseApplication
import com.outside.kotlineye.mvp.model.bean.HomeBean
import com.outside.kotlineye.utils.DateUtils.durationFormat
import com.outside.kotlineye.utils.GlideUtils

/**
 * className:    VideoDetailAdapter
 * description:  描述
 * author:       CLW2018
 * creatTime:    2019/10/11 10:34
 */

class VideoDetailAdapter(mContext:Context,val data: ArrayList<HomeBean.Issue.Item>):BaseRecyclerViewAdapter<HomeBean.Issue.Item>(mContext) {



    private var mInflater: LayoutInflater? = null
    private var textTypeface:Typeface?=null

    init {
        mInflater = LayoutInflater.from(mContext)
        textTypeface = Typeface.createFromAsset(BaseApplication.context.assets, "fonts/FZLanTingHeiS-L-GB-Regular.TTF")
    }

    companion object {
        private const val ITEM_VIDEO_DETAIL_INFO = 1    //detail_info 类型
        private const val ITEM_VIDEO_TEXT_CARD = 2   //videoSmallCard
        private const val  ITEM_VIDEO_SMALL_CARD= 3    //item
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            position == 0 -> ITEM_VIDEO_DETAIL_INFO

            data[position].type == "textCard" -> ITEM_VIDEO_TEXT_CARD

            data[position].type == "videoSmallCard" -> ITEM_VIDEO_SMALL_CARD
            else ->
                throw IllegalAccessException("Api 解析出错了，出现其他类型")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return when (viewType) {
            ITEM_VIDEO_DETAIL_INFO ->
                ViewHolder(inflaterView(R.layout.item_video_detail_info, parent))
            ITEM_VIDEO_TEXT_CARD ->
                ViewHolder(inflaterView(R.layout.item_video_text_card, parent))
            else ->
                ViewHolder(inflaterView(R.layout.item_video_small_card, parent))
        }
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        when (getItemViewType(position)) {
            ITEM_VIDEO_DETAIL_INFO -> {
                setVideoDetailInfo(item, holder)
            }
            ITEM_VIDEO_TEXT_CARD->{
                holder.setText(R.id.tv_text_card, item.data?.text!!)
                //设置方正兰亭细黑简体
                holder.getView<TextView>(R.id.tv_text_card).typeface =textTypeface
            }
            ITEM_VIDEO_SMALL_CARD->{
                with(holder){
                    setText(R.id.tv_title, item.data?.title!!)
                    setText(R.id.tv_tag, "#${item.data.category} / ${durationFormat(item.data.duration)}")
                    setImagePath(R.id.iv_video_small_card, object : ViewHolder.HolderImageLoader(item.data.cover.detail) {
                        override fun loadImage(iv: ImageView, path: String) {
                            GlideUtils.loadImagePlaceholder(mContext,path,iv,R.drawable.placeholder_banner)
                        }
                    })
                }

                holder.itemView.setOnClickListener {
                    mItemClickListener?.invoke(item,position)
                }
            }
        }
    }

    private fun setVideoDetailInfo(item: HomeBean.Issue.Item, holder: ViewHolder) {

        item.data?.title?.let { holder.setText(R.id.tv_title,it) }
        //视频简介
        item.data?.description?.let { holder.setText(R.id.expandable_text,it) }
        //标签
        holder.setText(R.id.tv_tag,"#${item.data?.category} / ${durationFormat(item.data?.duration)}")
        //喜欢
        holder.setText(R.id.tv_action_favorites, item.data?.consumption?.collectionCount.toString())
        //分享
        holder.setText(R.id.tv_action_share, item.data?.consumption?.shareCount.toString())
        //评论
        holder.setText(R.id.tv_action_reply, item.data?.consumption?.replyCount.toString())

        if (item.data?.author != null) {
            with(holder) {
                setText(R.id.tv_author_name, item.data.author.name)
                setText(R.id.tv_author_desc, item.data.author.description)
                setImagePath(
                    R.id.iv_avatar,
                    object : ViewHolder.HolderImageLoader(item.data.author.icon) {
                        override fun loadImage(iv: ImageView, path: String) {
                            //加载头像
                            GlideUtils.loadImagePlaceholderCirle(
                                mContext,
                                path,
                                iv,
                                R.drawable.default_avatar
                            )
                        }
                    })
            }
        } else {
            holder.setViewVisibility(R.id.layout_author_view, View.GONE)
        }

        with(holder) {
            getView<TextView>(R.id.tv_action_favorites).setOnClickListener {
                Toast.makeText(BaseApplication.context, "喜欢", Toast.LENGTH_SHORT).show()
            }
            getView<TextView>(R.id.tv_action_share).setOnClickListener {
                Toast.makeText(BaseApplication.context, "分享", Toast.LENGTH_SHORT).show()
            }
            getView<TextView>(R.id.tv_action_reply).setOnClickListener {
                Toast.makeText(BaseApplication.context, "评论", Toast.LENGTH_SHORT).show()
            }
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

    /**
     * 添加视频的详细信息
     */
    fun addOneData(item: HomeBean.Issue.Item) {
        data.clear()
        notifyDataSetChanged()
        data.add(item)
        notifyItemInserted(0)

    }

    fun addDataAll(item: ArrayList<HomeBean.Issue.Item>) {
        Log.e("adapter",item.size.toString())
        data.addAll(item)
        notifyItemRangeInserted(1, item.size)
    }


    override fun getItemCount(): Int {
        return data.size
    }



}