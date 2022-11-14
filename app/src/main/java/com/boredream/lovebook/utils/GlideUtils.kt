package com.boredream.lovebook.utils

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.ImageView
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
        // TODO: 配置
        glide.load(url)
            .placeholder(ColorDrawable(Color.GRAY))
            .error(ColorDrawable(Color.GRAY))
            .into(view)
    }

}