package com.boredream.lovebook.ui.diarydetail

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import com.blankj.utilcode.util.ToastUtils
import com.boredream.lovebook.R
import com.boredream.lovebook.base.BaseImagePickActivity
import com.boredream.lovebook.common.SimpleUiStateObserver
import com.boredream.lovebook.data.Diary
import com.boredream.lovebook.data.constant.BundleKey
import com.boredream.lovebook.databinding.ActivityDiaryDetailBinding
import com.boredream.lovebook.utils.PermissionSettingUtil
import com.yanzhenjie.permission.AndPermission
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DiaryDetailActivity :
    BaseImagePickActivity<DiaryDetailViewModel, ActivityDiaryDetailBinding>() {

    override fun getLayoutId() = R.layout.activity_diary_detail

    override fun getViewModelClass() = DiaryDetailViewModel::class.java

    private var data: Diary? = null

    companion object {
        fun start(context: Context, data: Diary? = null) {
            val intent = Intent(context, DiaryDetailActivity::class.java)
            intent.putExtra(BundleKey.DATA, data)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent.extras?.let {
            data = it.getSerializable(BundleKey.DATA) as Diary?
        }

        SimpleUiStateObserver.setCommitRequestObserver(viewModel, this)
        viewModel.load(data)

        val locationPermissions: ArrayList<String> = ArrayList()
        locationPermissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
        locationPermissions.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            locationPermissions.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        }

        AndPermission.with(this)
            .runtime()
            .permission(locationPermissions.toTypedArray())
            .onGranted { ToastUtils.showShort("权限获取成功") }
            .onDenied { permissions ->
                if (AndPermission.hasAlwaysDeniedPermission(this, locationPermissions)) {
                    PermissionSettingUtil.showSetting(this, permissions)
                }
            }
            .start()
    }

}