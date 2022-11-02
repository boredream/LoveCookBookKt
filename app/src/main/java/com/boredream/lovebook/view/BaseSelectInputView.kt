package com.boredream.lovebook.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import com.boredream.lovebook.R
import com.boredream.lovebook.databinding.ViewBaseSelectInputBinding
import com.boredream.lovebook.listener.OnCall

abstract class BaseSelectInputView : LinearLayout {

    protected var dataBinding: ViewBaseSelectInputBinding

    var data: String = ""
        get() = dataBinding.tvContent.text.toString()
        set(value) {
            field = value

            dataBinding.tvContent.text = data
            onDataSelectListener?.call(data)
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
            dataBinding.tvName.text = name
            a.recycle()
        }

        dataBinding.tvContent.setOnClickListener { startSelect() }
    }

    protected abstract fun startSelect()

    // TODO: 和 field 的 set 绑定
    private var onDataSelectListener: OnCall<String?>? = null

    fun setOnDataSelectListener(onDataSelectListener: OnCall<String?>) {
        this.onDataSelectListener = onDataSelectListener
    }

}