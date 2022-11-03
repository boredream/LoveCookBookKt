package com.boredream.lovebook.ui.thedaydetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.blankj.utilcode.util.ToastUtils
import com.boredream.lovebook.R
import com.boredream.lovebook.data.TheDay
import com.boredream.lovebook.data.constant.BundleKey
import com.boredream.lovebook.databinding.ActivityTheDayDetailBinding
import com.boredream.lovebook.ui.BaseActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TheDayDetailActivity : BaseActivity<TheDayDetailViewModel, ActivityTheDayDetailBinding>() {

    override fun getLayoutId() = R.layout.activity_the_day_detail

    override fun getViewModelClass() = TheDayDetailViewModel::class.java

    private var theDay: TheDay? = null

    companion object {
        fun start(context: Context, theDay: TheDay) {
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

        viewModel.commitUiState.observe(this) {
            when(it) {
                is CommitSuccess -> {
                    ToastUtils.showShort("提交成功")
                    finish()
                    // TODO: 发送event让其他地方更新，有repo后如何处理更优雅？
                }
                // TODO: 封装错误
                is CommitFail -> ToastUtils.showShort(it.reason)
            }
        }
        viewModel.load(theDay)
    }

}