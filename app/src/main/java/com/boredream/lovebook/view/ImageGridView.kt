package com.boredream.lovebook.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.CollectionUtils
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.UriUtils
import com.boredream.lovebook.R
import com.boredream.lovebook.base.BaseImagePickActivity
import com.boredream.lovebook.base.BaseImagePickActivity.Companion.REQUEST_CODE_CHOOSE
import com.boredream.lovebook.base.BaseListAdapter
import com.boredream.lovebook.common.BindingViewHolder
import com.boredream.lovebook.data.ImageInfo
import com.boredream.lovebook.databinding.ItemGridImageBinding
import com.boredream.lovebook.listener.OnCall
import com.boredream.lovebook.ui.imagebrowser.ImageBrowserActivity
import com.boredream.lovebook.utils.GlideUtils
import com.boredream.lovebook.utils.PermissionSettingUtil
import com.boredream.lovebook.view.itemdecoration.GridItemDecoration
import com.bumptech.glide.Glide
import com.yanzhenjie.permission.AndPermission
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine


class ImageGridView : RecyclerView, OnCall<List<Uri>> {

    private val dataList: ArrayList<ImageInfo> = ArrayList()

    companion object {
        const val ITEM_VIEW_TYPE_IMAGE = 1
        const val ITEM_VIEW_TYPE_ADD = 2
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        val spanCount = 3
        layoutManager = GridLayoutManager(context, spanCount)
        addItemDecoration(GridItemDecoration(spanCount, SizeUtils.dp2px(8f), true))
        adapter = Adapter(dataList)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        if (context is BaseImagePickActivity<*, *>) {
            (context as BaseImagePickActivity<*, *>).onImageResultCall = this
        }
    }

    class Adapter(val dataList: ArrayList<ImageInfo>) :
        BaseListAdapter<ImageInfo, ItemGridImageBinding>(dataList) {

        override fun getItemLayoutId() = R.layout.item_grid_image

        override fun getItemCount() = super.getItemCount() + 1

        override fun getItemViewType(position: Int): Int {
            return if (position == itemCount - 1) ITEM_VIEW_TYPE_ADD else ITEM_VIEW_TYPE_IMAGE
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): BindingViewHolder<ItemGridImageBinding> {
            val holder = super.onCreateViewHolder(parent, viewType)
            return holder
        }

        override fun onBindViewHolder(
            holder: BindingViewHolder<ItemGridImageBinding>,
            position: Int
        ) {
            if (getItemViewType(position) == ITEM_VIEW_TYPE_ADD) {
                holder.binding.ivImage.setImageResource(R.drawable.ic_baseline_add_24)
                holder.itemView.setOnClickListener { pickImage(holder.itemView.context) }
            } else {
                val data = dataList[position]
                GlideUtils.load(
                    Glide.with(holder.itemView),
                    data.getImageSource(),
                    holder.binding.ivImage
                )
                holder.itemView.setOnClickListener {
                    ImageBrowserActivity.start(holder.itemView.context, dataList, position)
                }
            }
        }

        private fun pickImage(context: Context) {
            if (context !is BaseImagePickActivity<*, *>) {
                return
            }

            AndPermission.with(context)
                .runtime()
                .permission(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .onGranted {
                    Matisse.from(context)
                        .choose(MimeType.ofImage())
                        .countable(true)
                        .maxSelectable(9)
                        .imageEngine(GlideEngine())
                        .forResult(REQUEST_CODE_CHOOSE)
                }
                .onDenied { permissions ->
                    if (AndPermission.hasAlwaysDeniedPermission(
                            context,
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        )
                    ) {
                        PermissionSettingUtil.showSetting(context, permissions)
                    }
                }
                .start()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun call(t: List<Uri>) {
        if (CollectionUtils.isEmpty(t)) return
        for (uri in t) {
            val path = UriUtils.uri2File(uri)?.absolutePath
            dataList.add(ImageInfo(path = path))
        }
        adapter?.notifyDataSetChanged()
    }

}