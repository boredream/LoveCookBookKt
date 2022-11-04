package com.boredream.lovebook.ui

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.boredream.lovebook.R
import com.boredream.lovebook.base.BaseFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class FragmentController(
    private val nav: BottomNavigationView,
    private val fm: FragmentManager,
    private val containerId: Int,
    private val fragmentList: ArrayList<BaseFragment<*, *>>
) {

    fun initFragment() {
        val ft: FragmentTransaction = fm.beginTransaction()
        for (i in 0 until fragmentList.size) {
            ft.add(containerId, fragmentList[i], i.toString())
        }
        ft.commitAllowingStateLoss()
        nav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    showFragment(0)
                    return@setOnItemSelectedListener true
                }
                R.id.navigation_dashboard -> {
                    showFragment(1)
                    return@setOnItemSelectedListener true
                }
                R.id.navigation_notifications -> {
                    showFragment(2)
                    return@setOnItemSelectedListener true
                }
            }
            return@setOnItemSelectedListener false
        }
        showFragment(0)
    }

    fun showFragment(position: Int) {
        hideFragments()
        val fragment = fragmentList[position]
        val ft: FragmentTransaction = fm.beginTransaction()
        ft.show(fragment)
        ft.commitAllowingStateLoss()
    }

    fun hideFragments() {
        val ft: FragmentTransaction = fm.beginTransaction()
        for (fragment in fragmentList) {
            ft.hide(fragment)
        }
        ft.commit()
    }

    fun getFragment(position: Int): BaseFragment<*, *> {
        return fragmentList[position]
    }
}