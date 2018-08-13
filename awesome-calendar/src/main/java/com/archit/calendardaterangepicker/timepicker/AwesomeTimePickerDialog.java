package com.archit.calendardaterangepicker.timepicker;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TimePicker;

import com.archit.calendardaterangepicker.R;
import com.archit.calendardaterangepicker.customviews.CustomTextView;

import java.util.Calendar;

public class AwesomeTimePickerDialog extends Dialog {

    private static final String LOG_TAG = AwesomeTimePickerDialog.class.getSimpleName();

    private CustomTextView tvHeaderTitle, tvDialogDone, tvDialogCancel;

    private String mTitle;
    private int hours, minutes;

    private TimePicker timePicker;

    public interface TimePickerCallback {
        void onTimeSelected(int hours, int mins);

        void onCancel();
    }

    private TimePickerCallback onTimeChangedListener;

    public AwesomeTimePickerDialog(Context context, String title, TimePickerCallback timePickerCallback) {
        super(context);
        mTitle = title;
        this.onTimeChangedListener = timePickerCallback;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().setGravity(Gravity.BOTTOM);
        setCanceledOnTouchOutside(false);
        initView();
        setListeners();

        //Grab the window of the dialog, and change the width
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = this.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
    }

    private void initView() {

        setContentView(R.layout.dialog_time_picker);

        tvHeaderTitle = (CustomTextView) findViewById(R.id.tvHeaderTitle);
        tvDialogDone = (CustomTextView) findViewById(R.id.tvHeaderDone);
        tvDialogCancel = (CustomTextView) findViewById(R.id.tvHeaderCancel);

        timePicker = (TimePicker) findViewById(R.id.timePicker);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                hours = i;
                minutes = i1;
            }
        });

        tvHeaderTitle.setText(mTitle);

    }

    private void setListeners() {

        tvDialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onTimeChangedListener != null) {
                    onTimeChangedListener.onCancel();
                }
                AwesomeTimePickerDialog.this.dismiss();
            }
        });

        tvDialogDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onTimeChangedListener != null) {
                    onTimeChangedListener.onTimeSelected(hours, minutes);
                }
                AwesomeTimePickerDialog.this.dismiss();
            }
        });

    }

    public void showDialog() {

        hours = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        minutes = Calendar.getInstance().get(Calendar.MINUTE);
        this.show();
    }

}
