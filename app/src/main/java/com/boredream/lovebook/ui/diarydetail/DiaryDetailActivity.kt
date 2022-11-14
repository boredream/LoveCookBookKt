package com.boredream.lovebook.ui.diarydetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.boredream.lovebook.R
import com.boredream.lovebook.base.BaseRequestActivity
import com.boredream.lovebook.data.Diary
import com.boredream.lovebook.data.constant.BundleKey
import com.boredream.lovebook.databinding.ActivityDiaryDetailBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DiaryDetailActivity : BaseRequestActivity<Diary, DiaryDetailViewModel, ActivityDiaryDetailBinding>() {

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
        viewModel.load(data)
    }

}