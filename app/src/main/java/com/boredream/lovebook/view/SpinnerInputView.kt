package com.boredream.lovebook.view

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import com.boredream.lovebook.R

@RequiresApi(Build.VERSION_CODES.N)
class SpinnerInputView : BaseSelectInputView, DialogInterface.OnClickListener {

    private lateinit var items: Array<CharSequence>
    private lateinit var dialog: AlertDialog

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        val a = context?.obtainStyledAttributes(attrs, R.styleable.SpinnerInputView, defStyleAttr, 0)
        a?.let {
            val items = it.getTextArray(R.styleable.SpinnerInputView_items)
            setItem(items)
            a.recycle()
        }

        dataBinding.tvContent.setOnClickListener { startSelect() }
    }

    fun setItem(arrayId: Int) {
        setItem(context.resources.getTextArray(arrayId))
    }

    fun setItem(items: Array<CharSequence>) {
        this.items = items
        dialog = AlertDialog.Builder((context))
            .setItems(items, this)
            .setNegativeButton("取消", null)
            .create()
    }

    override fun startSelect() {
        dialog.show()
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {
        data = items[which].toString()
    }
}