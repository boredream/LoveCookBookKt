package com.boredream.lovebook

import android.app.Application
import com.boredream.lovebook.utils.DataStoreUtils
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        DataStoreUtils.init(this)
        initRefresh()
    }

    private fun initRefresh() {
        //设置全局的Header构建器
//        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
//            layout.setPrimaryColorsId(
//                R.color.refresh,
//                R.color.text_color
//            )//全局设置主题颜色  CustomRefreshHeader   ClassicsHeader
//            ClassicsHeader(context)//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
//        }
//        //设置全局的Footer构建器
//        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, _ ->
//            //指定为经典Footer，默认是 BallPulseFooter
//            ClassicsFooter(context).setDrawableSize(20f)
//        }
    }

}