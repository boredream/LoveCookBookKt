package com.boredream.lovebook.view

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.CollectionUtils
import com.blankj.utilcode.util.PathUtils
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.UriUtils
import com.boredream.lovebook.R
import com.boredream.lovebook.base.BaseListAdapter
import com.boredream.lovebook.common.BindingViewHolder
import com.boredream.lovebook.data.ImageInfo
import com.boredream.lovebook.databinding.ItemGridImageBinding
import com.boredream.lovebook.listener.OnCall
import com.boredream.lovebook.ui.imagebrowser.ImageBrowserActivity
import com.boredream.lovebook.utils.GlideEngine
import com.boredream.lovebook.utils.GlideUtils
import com.boredream.lovebook.view.itemdecoration.GridItemDecoration
import com.bumptech.glide.Glide
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import java.io.File


class ImageGridView : RecyclerView {

    companion object {
        const val ITEM_VIEW_TYPE_IMAGE = 1
        const val ITEM_VIEW_TYPE_ADD = 2

        // 双向数据绑定 data -> view
        @BindingAdapter("gridImages")
        @JvmStatic
        fun setData(view: ImageGridView, newValue: String?) {
            // Important to break potential infinite loops.
            if (view.getImages() != newValue) {
                view.setImages(newValue)
            }
        }

        // 双向数据绑定 view -> data
        @InverseBindingAdapter(attribute = "gridImages")
        @JvmStatic
        fun getData(view: ImageGridView): String? {
            return view.getImages()
        }

        // 双向数据绑定 view add listener
        @BindingAdapter("gridImagesAttrChanged")
        @JvmStatic
        fun setDataChangeListeners(
            view: ImageGridView,
            attrChange: InverseBindingListener
        ) {
            // Set a listener for click, focus, touch, etc.
            view.dataChangeListener = { attrChange.onChange() }
        }
    }

    private val dataList: ArrayList<ImageInfo> = ArrayList()
    var dataChangeListener: ((ArrayList<ImageInfo>) -> Unit)? = null

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

    fun setImages(images: String?) {
        if (images == null) return
        dataList.clear()
        for (image in images.split(",")) {
            if (StringUtils.isEmpty(image)) continue
            if (image.startsWith("http")) {
                dataList.add(ImageInfo(url = image))
            } else {
                dataList.add(ImageInfo(path = image))
            }
        }
    }

    fun getImages(): String? {
        val sb = StringBuilder()
        dataList.forEach {
            if (it.url != null) {
                sb.append(",").append(it.url)
            } else if (it.path != null) {
                sb.append(",").append(it.path)
            }
        }
        if(sb.isEmpty()) return null
        return sb.substring(1)
    }

    inner class Adapter(dataList: ArrayList<ImageInfo>) :
        BaseListAdapter<ImageInfo, ItemGridImageBinding>(dataList) {

        override fun getItemLayoutId() = R.layout.item_grid_image

        override fun getItemCount() = super.getItemCount() + 1

        override fun getItemViewType(position: Int): Int {
            return if (position == itemCount - 1) ITEM_VIEW_TYPE_ADD else ITEM_VIEW_TYPE_IMAGE
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
            // TODO 使用外部存储空间，防止app卸载后，拍照图片丢失
            val cameraDir = PathUtils.getInternalAppFilesPath() + File.pathSeparator + "camera"
            PictureSelector.create(context)
                .openGallery(SelectMimeType.ofImage())
                .setOutputCameraDir(cameraDir)
                .setImageEngine(GlideEngine)
                .forResult(object : OnResultCallbackListener<LocalMedia> {
                    override fun onResult(result: ArrayList<LocalMedia>) {
                        addImages(result)
                    }

                    override fun onCancel() {}
                })
        }

        @SuppressLint("NotifyDataSetChanged")
        private fun addImages(images: ArrayList<LocalMedia>) {
            if (CollectionUtils.isEmpty(images)) return
            images.forEach { dataList.add(ImageInfo(path = it.path)) }
            notifyDataSetChanged()
            dataChangeListener?.invoke(dataList)
        }
    }

}