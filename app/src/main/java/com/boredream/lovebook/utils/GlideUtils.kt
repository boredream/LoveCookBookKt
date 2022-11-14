package com.boredream.lovebook.utils

import android.graphics.drawable.ColorDrawable
import android.widget.ImageView
import com.blankj.utilcode.util.ColorUtils
import com.boredream.lovebook.R
import com.bumptech.glide.RequestManager

// https://muyangmin.github.io/glide-docs-cn/
object GlideUtils {

    // TODO: glide 升级

    /**
     * 加载图片
     *
     * @param glide Glide.with()
     */
    fun load(glide: RequestManager, url: String?, view: ImageView) {
        val defColor = ColorUtils.getColor(R.color.bg_gray)
        glide.load(url)
            .placeholder(ColorDrawable(defColor))
            .error(ColorDrawable(defColor))
            .into(view)
    }

}