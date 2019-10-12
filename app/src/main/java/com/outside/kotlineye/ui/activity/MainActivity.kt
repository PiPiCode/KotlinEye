package com.outside.kotlineye.ui.activity


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.ashokvarma.bottomnavigation.BottomNavigationBar
import com.outside.kotlineye.R
import com.outside.kotlineye.base.BaseActivity
import com.outside.kotlineye.ui.fragment.HomeFragment
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast

class MainActivity : BaseActivity() {


    private var mHomeFragment: HomeFragment? = null
    private var mHomeFragment2: HomeFragment? = null
    private var mHomeFragment3: HomeFragment? = null
    private var mHomeFragment4: HomeFragment? = null

    private val mTitles = arrayOf("每日精选", "发现", "热门", "我的")


    //默认为0
    private var mIndex = 0


    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            mIndex = savedInstanceState.getInt("currTabIndex")
        }
        super.onCreate(savedInstanceState)
    }



    @SuppressLint("MissingSuperCall")
    override fun onSaveInstanceState(outState: Bundle) {
//        showToast("onSaveInstanceState->"+mIndex)
//        super.onSaveInstanceState(outState)
        //记录fragment的位置,防止崩溃 activity被系统回收时，fragment错乱
        if (mBottomNavBar != null) {
            outState.putInt("currTabIndex", mIndex)
        }
    }


    override fun initView() {
        initBottomNavBar()
        mBottomNavBar.setFirstSelectedPosition(mIndex)
        switchFragment(mIndex)
    }

    private fun initBottomNavBar() {

        mBottomNavBar.setTabSelectedListener(object : BottomNavigationBar.OnTabSelectedListener {
            override fun onTabReselected(position: Int) {

            }

            override fun onTabUnselected(position: Int) {
            }

            override fun onTabSelected(position: Int) {
                switchFragment(position)
            }
        })

    }


    /**
     * 切换Fragment
     * @param position 下标
     */
    private fun switchFragment(position: Int) {
        val transaction = supportFragmentManager.beginTransaction()
        hideFragments(transaction)
        when (position) {
            0 // 首页
            -> mHomeFragment?.let {
                transaction.show(it)
            } ?: HomeFragment.getInstance(mTitles[position]).let {
                mHomeFragment = it
                transaction.add(R.id.fl_container, it, "home")
            }


            1  //发现
            -> mHomeFragment2?.let {
                transaction.show(it)
            } ?: HomeFragment.getInstance(mTitles[position]).let {
                mHomeFragment2 = it
                transaction.add(R.id.fl_container, it, "discovery")
            }
            2  //热门
            -> mHomeFragment3?.let {
                transaction.show(it)
            } ?: HomeFragment.getInstance(mTitles[position]).let {
                mHomeFragment3 = it
                transaction.add(R.id.fl_container, it, "hot")
            }
            3 //我的
            -> mHomeFragment4?.let {
                transaction.show(it)
            } ?: HomeFragment.getInstance(mTitles[position]).let {
                mHomeFragment4 = it
                transaction.add(R.id.fl_container, it, "mine")
            }

            else -> {

            }
        }

        mIndex = position
        mBottomNavBar.setFirstSelectedPosition(mIndex)
        transaction.commitAllowingStateLoss()
    }


    /**
     * 隐藏所有的Fragment
     * @param transaction transaction
     */
    private fun hideFragments(transaction: FragmentTransaction) {
        mHomeFragment?.let { transaction.hide(it) }
        mHomeFragment2?.let { transaction.hide(it) }
        mHomeFragment3?.let { transaction.hide(it) }
        mHomeFragment4?.let { transaction.hide(it) }
    }


    private var mExitTime: Long = 0

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis().minus(mExitTime) <= 2000) {
                finish()
            } else {
                mExitTime = System.currentTimeMillis()
                toast("再按一次退出程序")
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }


}
