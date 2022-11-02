package com.boredream.lovebook.view

import android.app.DatePickerDialog
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.DatePicker
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import com.boredream.lovebook.R
import com.boredream.lovebook.databinding.ViewDateInputBinding
import com.boredream.lovebook.listener.OnCall

@RequiresApi(Build.VERSION_CODES.N)
class DateInputView : LinearLayout, DatePickerDialog.OnDateSetListener {

    private lateinit var dataBinding: ViewDateInputBinding
    private lateinit var dialog: DatePickerDialog

    constructor(context: Context?) : super(context) {
        initView()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initView()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView()
    }

    init {
        print("init")
    }

    private fun initView() {
        print("initView")
        dataBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.view_date_input,
            this,
            true)

        dialog = DatePickerDialog(context)
        dialog.setOnDateSetListener(this)

        dataBinding.root.setOnClickListener {
            showDatePickDialog()
        }
    }

    fun setDate(date: String?) {
        dataBinding.tvContent.text = date
        onDateSelectListener?.call(date)
    }

    private fun showDatePickDialog() {
        dialog.show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        setDate(String.format("%d-%d-%d", year, month, dayOfMonth))
    }

    private var onDateSelectListener: OnCall<String?>? = null

    fun setOnDateSelectListener(onDateSelectListener: OnCall<String?>) {
        this.onDateSelectListener = onDateSelectListener
    }

}