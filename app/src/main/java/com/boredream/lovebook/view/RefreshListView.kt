package com.boredream.lovebook.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.boredream.lovebook.R
import com.boredream.lovebook.base.BaseListAdapter
import com.boredream.lovebook.base.refreshlist.RefreshUiState
import com.boredream.lovebook.databinding.ViewRefreshListBinding
import com.scwang.smart.refresh.layout.SmartRefreshLayout

class RefreshListView : FrameLayout {

    var dataBinding: ViewRefreshListBinding
    val refresh: SmartRefreshLayout
    val rv: RecyclerView
    private var adapter: BaseListAdapter<Any, ViewDataBinding>? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        dataBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.view_refresh_list,
            this,
            true
        )

        refresh = dataBinding.refreshListRefresh
        rv = dataBinding.refreshListRv
    }

    fun setup(
        adapter: BaseListAdapter<Any, ViewDataBinding>,
        enableLoadMore: Boolean = false,
        onLoadMore: () -> Unit = {},
        enableRefresh: Boolean = true,
        onRefresh: () -> Unit = {},
        layoutManager: LinearLayoutManager = LinearLayoutManager(context),
        itemDecoration: ItemDecoration = DividerItemDecoration(context, VERTICAL),
    ) {
        refresh.setEnableLoadMore(enableLoadMore)
        refresh.setOnLoadMoreListener { onLoadMore.invoke() }
        refresh.setEnableRefresh(enableRefresh)
        refresh.setOnRefreshListener { onRefresh.invoke() }

        rv.layoutManager = layoutManager
        rv.addItemDecoration(itemDecoration)
        rv.adapter = adapter

        this.adapter = adapter
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(refreshList: RefreshUiState) {
        refresh.setEnableLoadMore(refreshList.enableLoadMore)
        if (refreshList.showRefresh) refresh.autoRefresh() else refresh.finishRefresh()
        if (refreshList.showLoadMore) refresh.autoLoadMore() else refresh.finishLoadMore()

        adapter?.let { adapter ->
            adapter.dataList.clear()
            refreshList.list?.let { adapter.dataList.addAll(it) }
            adapter.notifyDataSetChanged()
        }
    }

}
