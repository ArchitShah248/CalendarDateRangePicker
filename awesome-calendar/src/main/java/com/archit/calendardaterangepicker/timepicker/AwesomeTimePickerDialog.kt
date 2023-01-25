package com.archit.calendardaterangepicker.timepicker

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.Window
import android.view.WindowManager.LayoutParams
import android.widget.TimePicker
import com.archit.calendardaterangepicker.R.id
import com.archit.calendardaterangepicker.R.layout
import com.archit.calendardaterangepicker.customviews.CustomTextView
import java.util.Calendar

class AwesomeTimePickerDialog(context: Context, private val mTitle: String, private val onTimeChangedListener: TimePickerCallback) : Dialog(context) {
    private lateinit var tvDialogDone: CustomTextView
    private lateinit var tvDialogCancel: CustomTextView
    private var hours = 0
    private var minutes = 0

    interface TimePickerCallback {
        fun onTimeSelected(hours: Int, mins: Int)
        fun onCancel()
    }
    companion object {
        private val LOG_TAG = AwesomeTimePickerDialog::class.java.simpleName
    }

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setGravity(Gravity.BOTTOM)
        setCanceledOnTouchOutside(false)
        initView()
        setListeners()

        //Grab the window of the dialog, and change the width
        val lp = LayoutParams()
        val window = this.window
        lp.copyFrom(window?.attributes)
        //This makes the dialog take up the full width
        lp.width = LayoutParams.MATCH_PARENT
        lp.height = LayoutParams.WRAP_CONTENT
        window?.attributes = lp
    }

    private fun initView() {
        setContentView(layout.dialog_time_picker)
        val tvHeaderTitle: CustomTextView = findViewById(id.tvHeaderTitle)
        tvDialogDone = findViewById(id.tvHeaderDone)
        tvDialogCancel = findViewById(id.tvHeaderCancel)
        val timePicker = findViewById<TimePicker>(id.timePicker)
        timePicker.setOnTimeChangedListener { _, i, i1 ->
            hours = i
            minutes = i1
        }
        tvHeaderTitle.text = mTitle
    }

    private fun setListeners() {
        tvDialogCancel.setOnClickListener {
            onTimeChangedListener.onCancel()
            dismiss()
        }
        tvDialogDone.setOnClickListener {
            onTimeChangedListener.onTimeSelected(hours, minutes)
            dismiss()
        }
    }

    fun showDialog() {
        hours = Calendar.getInstance()[Calendar.HOUR_OF_DAY]
        minutes = Calendar.getInstance()[Calendar.MINUTE]
        show()
    }
}