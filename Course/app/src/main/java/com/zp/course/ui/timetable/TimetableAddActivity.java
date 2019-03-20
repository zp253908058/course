package com.zp.course.ui.timetable;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.zp.course.R;
import com.zp.course.app.ToolbarActivity;
import com.zp.course.util.Toaster;

import java.util.Calendar;
import java.util.Locale;

import androidx.annotation.Nullable;

/**
 * Class description:
 *
 * @author zp
 * @version 1.0
 * @see TimetableAddActivity
 * @since 2019/3/20
 */
public class TimetableAddActivity extends ToolbarActivity implements DatePickerDialog.OnDateSetListener {

    private static final String DATE_FORMAT = "%1$d-%2$d-%3$d";

    public static void go(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, TimetableAddActivity.class);
        context.startActivity(intent);
    }

    private EditText mDateText;

    private DatePickerDialog mDialog;

    int i = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable_add_layout);
        showFloatingButton(true);
        setFloatingIcon(getDrawable(R.drawable.ic_done_black));
        mDateText = findViewById(R.id.timetable_add_date);
        Calendar calendar = Calendar.getInstance();
        mDialog = new DatePickerDialog(this, R.style.AlertDialogStyle, this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    public void onClick(View view) {
        mDialog.show();
    }

    @Override
    public void onFloatingClick() {
        attemptAdd();
    }

    private void attemptAdd() {

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = String.format(Locale.getDefault(), DATE_FORMAT, year, month + 1, dayOfMonth);
        mDateText.setText(date);

    }
}

