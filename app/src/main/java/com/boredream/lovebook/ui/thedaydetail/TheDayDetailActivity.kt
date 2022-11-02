package com.boredream.lovebook.ui.thedaydetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.boredream.lovebook.R
import com.boredream.lovebook.data.TheDay
import com.boredream.lovebook.data.constant.BundleKey
import com.boredream.lovebook.databinding.ActivityLoginBinding
import com.boredream.lovebook.databinding.ActivityTheDayDetailBinding
import com.boredream.lovebook.ui.BaseActivity
import com.boredream.lovebook.ui.main.MainActivity
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

        viewModel.uiState.observe(this@TheDayDetailActivity) {
            // TODO 需要转一层的数据，如何MVVM？
            binding.sivType.data = if (it.notifyType == TheDay.NOTIFY_TYPE_TOTAL_COUNT)
                "累计天数" else "按年倒计天数"
        }
        viewModel.load(theDay)
    }

}