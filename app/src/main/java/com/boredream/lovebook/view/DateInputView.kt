package com.boredream.lovebook.view

import android.app.DatePickerDialog
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.widget.DatePicker
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.N)
class DateInputView : BaseSelectInputView, DatePickerDialog.OnDateSetListener {

    private var dialog: DatePickerDialog? = null

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        context?.let {
            dialog = DatePickerDialog(it)
            dialog?.setOnDateSetListener(this)
            dataBinding.tvContent.setOnClickListener { startSelect() }
        }
    }

    override fun startSelect() {
        dialog?.show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        data = String.format("%d-%d-%d", year, month + 1, dayOfMonth)
    }

}