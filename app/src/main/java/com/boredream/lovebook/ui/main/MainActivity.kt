package com.boredream.lovebook.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.boredream.lovebook.R
import com.boredream.lovebook.base.BaseActivity
import com.boredream.lovebook.base.BaseFragment
import com.boredream.lovebook.databinding.ActivityMainBinding
import com.boredream.lovebook.service.SyncDataService
import com.boredream.lovebook.ui.FragmentController
import com.boredream.lovebook.ui.diary.DiaryFragment
import com.boredream.lovebook.ui.mine.MineFragment
import com.boredream.lovebook.ui.theday.TheDayFragment
import com.boredream.lovebook.ui.todogroup.TodoGroupFragment
import com.boredream.lovebook.ui.trace.recordlist.TraceRecordListFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }

    // TODO: 无需 vm ？

    override fun getLayoutId() = R.layout.activity_main
    override fun getViewModelClass() = MainViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val navView: BottomNavigationView = binding.navView
        val fragmentList = ArrayList<BaseFragment<*, *>>()
        fragmentList.add(TheDayFragment())
        fragmentList.add(DiaryFragment())
        fragmentList.add(TraceRecordListFragment())
        fragmentList.add(TodoGroupFragment())
        fragmentList.add(MineFragment())

        val controller = FragmentController(navView, supportFragmentManager, R.id.fl_fragment, fragmentList)
        controller.initFragment()

        SyncDataService.startSync(this)
    }

}