package com.boredream.lovebook.vm;

import androidx.databinding.InverseMethod;

import com.boredream.lovebook.data.TheDay;
import com.boredream.lovebook.view.BaseSelectInputView;

import java.util.Objects;

/**
 * 用于解决自定义组件的DataBinding数据转换问题
 */
public class BindingConvert {

   @InverseMethod("notifyTypeStringToInt")
   public static String notifyTypeIntToString(int value) {
      return value == TheDay.NOTIFY_TYPE_TOTAL_COUNT ? "累计天数" : "按年倒计天数";
   }

   public static int notifyTypeStringToInt(String value) {
      return Objects.equals(value, "累计天数")
              ? TheDay.NOTIFY_TYPE_TOTAL_COUNT
              : TheDay.NOTIFY_TYPE_YEAR_COUNT_DOWN;
   }

}
