package com.boredream.lovebook.vm;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseMethod;

import com.boredream.lovebook.data.TheDay;
import com.boredream.lovebook.utils.GlideUtils;
import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;

import java.util.Objects;

/**
 * 用于解决自定义组件的DataBinding问题
 */
public class BindingContract {

    @BindingAdapter("android:src")
    public static void setImageViewGlideUrl(ImageView iv, String newValue) {
        System.out.println("load image " + newValue);
        GlideUtils.INSTANCE.load(Glide.with(iv), newValue, iv);
    }

    @BindingAdapter("android:src")
    public static void setPhotoViewImageSource(PhotoView photoView, String imageSource) {
        GlideUtils.INSTANCE.load(Glide.with(photoView), imageSource, photoView);
    }

    // 用于解决需要转换的数据转换问题
    public static class Convert {

        @InverseMethod("notifyTypeStringToInt")
        public static String notifyTypeIntToString(Integer value) {
            return value != null && value == TheDay.NOTIFY_TYPE_YEAR_COUNT_DOWN ? "按年倒计天数" : "累计天数";
        }

        public static Integer notifyTypeStringToInt(String value) {
            return Objects.equals(value, "累计天数")
                    ? TheDay.NOTIFY_TYPE_TOTAL_COUNT
                    : TheDay.NOTIFY_TYPE_YEAR_COUNT_DOWN;
        }

    }

}
