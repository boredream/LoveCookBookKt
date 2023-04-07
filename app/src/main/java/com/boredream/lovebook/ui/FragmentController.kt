package com.boredream.lovebook.ui

import android.os.Bundle
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

    fun initFragment(savedInstanceState: Bundle?) {
        val ft: FragmentTransaction = fm.beginTransaction()
        for (i in 0 until fragmentList.size) {
            val tag = i.toString()
            if (savedInstanceState == null || fm.findFragmentByTag(tag) == null) {
                ft.add(containerId, fragmentList[i], tag)
            }
        }
        ft.commitAllowingStateLoss()
        nav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_the_day -> {
                    showFragment(0)
                    return@setOnItemSelectedListener true
                }
                R.id.navigation_diary -> {
                    showFragment(1)
                    return@setOnItemSelectedListener true
                }
                R.id.navigation_trace -> {
                    showFragment(2)
                    return@setOnItemSelectedListener true
                }
                R.id.navigation_todo_list -> {
                    showFragment(3)
                    return@setOnItemSelectedListener true
                }
                R.id.navigation_mine -> {
                    showFragment(4)
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