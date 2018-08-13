package com.archit.calendardaterangepicker.customviews;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

public class CustomTextView extends AppCompatTextView {

    public CustomTextView(Context context) {
        super(context);
        initViews(context, null, 0);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context, attrs, 0);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initViews(context, attrs, defStyle);
    }

    private void initViews(Context context, AttributeSet attrs, int defStyle) {

        if (isInEditMode()) {
            return;
        }
        setFonts(attrs, defStyle);

    }

    private void setFonts(AttributeSet attrs, int defStyle) {

//        Typeface robotoTypeface = CustomTypefaceManager.obtainTypeface(mContext, attrs, defStyle);
//        setTypeface(robotoTypeface);

    }


}
