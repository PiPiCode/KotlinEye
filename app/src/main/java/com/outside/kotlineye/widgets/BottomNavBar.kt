package com.outside.kotlineye.widgets

import android.content.Context
import android.util.AttributeSet
import com.ashokvarma.bottomnavigation.BottomNavigationBar
import com.ashokvarma.bottomnavigation.BottomNavigationItem
import com.outside.kotlineye.R

/**
 * className:    BottomNavBar
 * description:  描述
 * author:       CLW2018
 * creatTime:    2019/9/17 10:11
 */

class BottomNavBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BottomNavigationBar(context, attrs, defStyleAttr) {

    init {
        val homeItem = BottomNavigationItem(
            R.drawable.ic_home_selected,
            "每日精选"
        )
            .setInactiveIconResource(R.drawable.ic_home_normal)
            .setActiveColorResource(R.color.color_black)
            .setInActiveColorResource(R.color.color_gray)


        val findItem = BottomNavigationItem(
            R.drawable.ic_discovery_selected,
            "发现"
        )
            .setInactiveIconResource(R.drawable.ic_discovery_normal)
            .setActiveColorResource(R.color.color_black)
            .setInActiveColorResource(R.color.color_gray)

        val hotItem = BottomNavigationItem(
            R.drawable.ic_hot_selected,
            "热门"
        )
            .setInactiveIconResource(R.drawable.ic_hot_normal)
            .setActiveColorResource(R.color.color_black)
            .setInActiveColorResource(R.color.color_gray)


        val mineItem = BottomNavigationItem(
            R.drawable.ic_mine_selected,
            "发现"
        )
            .setInactiveIconResource(R.drawable.ic_mine_normal)
            .setActiveColorResource(R.color.color_black)
            .setInActiveColorResource(R.color.color_gray)


        setMode(MODE_FIXED)

        setBackgroundStyle(BACKGROUND_STYLE_DEFAULT)

        setBarBackgroundColor(R.color.color_title_bg)


        addItem(homeItem)
            .addItem(findItem)
            .addItem(hotItem)
            .addItem(mineItem)
            .setFirstSelectedPosition(0)
            .initialise()
    }
}