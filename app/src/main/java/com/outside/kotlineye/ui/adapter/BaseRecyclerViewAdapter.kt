package com.outside.kotlineye.ui.adapter


import android.content.Context
import androidx.recyclerview.widget.RecyclerView

/*
    RecyclerViewAdapter基类
 */
abstract class BaseRecyclerViewAdapter<T>(var mContext: Context) : RecyclerView.Adapter<ViewHolder>() {


    //数据集合
    var dataList: MutableList<T> = mutableListOf()

    /*
        设置数据
        Presenter处理过为null的情况，所以为不会为Null
     */
    fun setData(sources: MutableList<T>) {
        dataList = sources
        notifyDataSetChanged()
    }

    /*
        添加数据

     */
    fun addData(sources: MutableList<T>) {
        dataList.addAll(sources)
        notifyDataSetChanged()
    }

    //ItemClick事件
    var mItemClickListener :((t:T,position:Int) -> Unit)? = null

    fun onItemClick(listener:((t:T,position:Int)->Unit)) {
        this.mItemClickListener = listener
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            mItemClickListener?.let {
                it(dataList[position], position)
            }

        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

}
