package com.boredream.lovebook.ui.imagebrowser

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.blankj.utilcode.util.ScreenUtils
import com.boredream.lovebook.R
import com.boredream.lovebook.common.SimpleListAdapter
import com.boredream.lovebook.data.ImageInfo
import com.boredream.lovebook.databinding.ActivityImageBrowserBinding
import com.boredream.lovebook.databinding.ItemImageBrowserBinding


class ImageBrowserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityImageBrowserBinding
    private lateinit var images: ArrayList<ImageInfo>
    private var position = 0

    companion object {
        fun start(context: Context, images: ArrayList<ImageInfo>, position: Int) {
            val intent = Intent(context, ImageBrowserActivity::class.java)
            intent.putExtra("images", images)
            intent.putExtra("position", position % images.size)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        ScreenUtils.setFullScreen(this)

        super.onCreate(savedInstanceState)

        images = intent.getSerializableExtra("images") as ArrayList<ImageInfo>
        position = intent.getIntExtra("position", 0)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_image_browser)
        binding.vpImageBrower.adapter =
            SimpleListAdapter<ImageInfo, ItemImageBrowserBinding>(
                images, R.layout.item_image_browser
            )
        binding.vpImageBrower.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                setIndexText(position, images.size)
            }
        })
        binding.vpImageBrower.currentItem = position

        if (images.size > 1) {
            binding.tvImageIndex.visibility = View.VISIBLE
            setIndexText(position, images.size)
        } else {
            binding.tvImageIndex.visibility = View.GONE
        }
    }

    private fun setIndexText(position: Int, total: Int) {
        binding.tvImageIndex.text = String.format("%d/%d", position + 1, total)
    }

}