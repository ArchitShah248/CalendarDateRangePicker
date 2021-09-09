package com.archit.calendardaterangepicker.customviews

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class CustomTextView : AppCompatTextView {
    constructor(context: Context) : super(context) {
        initViews(context, null, 0)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initViews(context, attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        initViews(context, attrs, defStyle)
    }

    private fun initViews(context: Context, attrs: AttributeSet?, defStyle: Int) {
        if (isInEditMode) {
            return
        }
        setFonts(attrs, defStyle)
    }

    private fun setFonts(attrs: AttributeSet?, defStyle: Int) {

//        Typeface robotoTypeface = CustomTypefaceManager.obtainTypeface(mContext, attrs, defStyle);
//        setTypeface(robotoTypeface);
    }
}