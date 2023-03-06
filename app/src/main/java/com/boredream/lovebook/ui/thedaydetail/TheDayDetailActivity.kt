package com.boredream.lovebook.ui.thedaydetail

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.blankj.utilcode.util.ToastUtils
import com.boredream.lovebook.R
import com.boredream.lovebook.base.BaseActivity
import com.boredream.lovebook.common.SimpleUiStateObserver
import com.boredream.lovebook.data.TheDay
import com.boredream.lovebook.data.constant.BundleKey
import com.boredream.lovebook.databinding.ActivityTheDayDetailBinding
import com.boredream.lovebook.utils.AlarmUtils
import com.yanzhenjie.permission.AndPermission
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TheDayDetailActivity : BaseActivity<TheDayDetailViewModel, ActivityTheDayDetailBinding>() {

    override fun getLayoutId() = R.layout.activity_the_day_detail

    override fun getViewModelClass() = TheDayDetailViewModel::class.java

    private var theDay: TheDay? = null

    companion object {
        fun start(context: Context, theDay: TheDay? = null) {
            val intent = Intent(context, TheDayDetailActivity::class.java)
            intent.putExtra(BundleKey.DATA, theDay)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent.extras?.let {
            theDay = it.getSerializable(BundleKey.DATA) as TheDay?
        }

        initObserver()
        viewModel.load(theDay)
    }

    private fun initObserver() {
        SimpleUiStateObserver.setRequestObserver(
            this,
            this,
            viewModel.commitVMCompose
        )

        viewModel.commitSuccessEvent.observe(this) {
            // 提交成功后，提示是否同步更新系统日历
            AlertDialog.Builder(this)
                .setTitle("提醒")
                .setMessage("是否将日期提醒同步到系统日历中？")
                .setPositiveButton("同步") { _, _ -> syncSysCalendar(it!!) }
                .setNegativeButton("取消") { _, _ -> finish() }
                .show()
        }
    }

    private fun syncSysCalendar(theDay: TheDay) {
        AndPermission.with(this)
            .runtime()
            .permission(
                Manifest.permission.READ_CALENDAR,
                Manifest.permission.WRITE_CALENDAR
            )
            .onGranted {
                AlarmUtils.insertOrUpdateCalendarEvent(this, theDay) {
                    if (it != null) {
                        ToastUtils.showShort(it)
                    } else {
                        ToastUtils.showShort("日历同步成功！")
                    }
                    finish()
                }
            }
            .onDenied {
                val sbMsg = StringBuilder()
                sbMsg.append("请打开权限：")
                for (permissionName in it) {
                    sbMsg.append("、").append(permissionName)
                }
                sbMsg.append(" 否则无法同步日历")
                ToastUtils.showLong(sbMsg.toString())
                finish()
            }
            .start()
    }

}