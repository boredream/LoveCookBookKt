package com.boredream.lovebook.ui.diary

import com.boredream.lovebook.R
import com.boredream.lovebook.base.BaseListAdapter
import com.boredream.lovebook.data.Diary
import com.boredream.lovebook.databinding.ItemDiaryBinding

class DiaryListAdapter(dataList: ArrayList<Diary>) :
    BaseListAdapter<Diary, ItemDiaryBinding>(dataList) {

    override fun getItemLayoutId() = R.layout.item_diary

}