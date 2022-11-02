package com.boredream.lovebook.vm;

import androidx.databinding.BindingAdapter;

import com.boredream.lovebook.view.BaseSelectInputView;

/**
 * 用于解决自定义组件的DataBinding问题
 */
public class BindingAdapterContract {

   @BindingAdapter("app:select")
   public static void setBaseSelectInputViewSelect(BaseSelectInputView view, String select) {
      view.setData(select);
   }

}
