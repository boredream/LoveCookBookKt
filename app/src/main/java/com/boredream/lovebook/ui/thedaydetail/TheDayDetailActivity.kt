package com.boredream.lovebook.ui.thedaydetail

import android.os.Bundle
import com.boredream.lovebook.R
import com.boredream.lovebook.data.TheDay
import com.boredream.lovebook.data.constant.BundleKey
import com.boredream.lovebook.databinding.ActivityLoginBinding
import com.boredream.lovebook.ui.BaseActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TheDayDetailActivity : BaseActivity<TheDayDetailViewModel, ActivityLoginBinding>() {

    override fun getLayoutId() = R.layout.activity_the_day_detail

    override fun getViewModelClass() = TheDayDetailViewModel::class.java

    private var theDay: TheDay? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent.extras?.let {
            theDay = it.getParcelable(BundleKey.DATA)
        }

        viewModel.uiState.observe(this@TheDayDetailActivity) {

        }
        viewModel.load(theDay)
    }

}