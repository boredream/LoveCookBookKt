package com.boredream.lovebook.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.blankj.utilcode.util.LogUtils
import com.boredream.lovebook.R
import com.boredream.lovebook.databinding.ViewBaseSelectInputBinding
import com.boredream.lovebook.listener.OnCall

abstract class BaseSelectInputView : LinearLayout {

    protected var dataBinding: ViewBaseSelectInputBinding

    var data: String = ""
        get() = dataBinding.tvContent.text.toString()
        set(value) {
            field = value

            dataBinding.tvContent.text = value
            onDataSelectListener?.call(value)
            LogUtils.iTag("BaseSelectInputView", "setData $value")
        }

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        dataBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.view_base_select_input,
            this,
            true)

        val a = context?.obtainStyledAttributes(attrs, R.styleable.BaseSelectInputView, defStyleAttr, 0)
        a?.let {
            val name = it.getString(R.styleable.BaseSelectInputView_name)
            val select = it.getString(R.styleable.BaseSelectInputView_select)
            dataBinding.tvName.text = name
            dataBinding.tvContent.text = select
            a.recycle()
        }

        dataBinding.tvContent.setOnClickListener { startSelect() }
    }

    protected abstract fun startSelect()

    companion object {

        // 双向数据绑定 data -> view
        @BindingAdapter("inputSelect")
        @JvmStatic fun setInputSelect(view: BaseSelectInputView, newValue: String?) {
            // Important to break potential infinite loops.
            if (newValue != null && view.data != newValue) {
                view.data = newValue
            }
        }

        // 双向数据绑定 view -> data
        @InverseBindingAdapter(attribute = "inputSelect")
        @JvmStatic fun getInputSelect(view: BaseSelectInputView) : String {
            return view.data
        }

        // 双向数据绑定 view add listener
        @BindingAdapter("inputSelectAttrChanged")
        @JvmStatic fun setInputSelectListeners(
            view: BaseSelectInputView,
            attrChange: InverseBindingListener
        ) {
            // Set a listener for click, focus, touch, etc.
            view.setOnDataSelectListener(object : OnCall<String?> {
                override fun call(t: String?) {
                    attrChange.onChange()
                }
            })
        }
    }

    private var onDataSelectListener: OnCall<String?>? = null

    fun setOnDataSelectListener(onDataSelectListener: OnCall<String?>) {
        this.onDataSelectListener = onDataSelectListener
    }

}