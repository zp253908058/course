package com.zp.course.pop;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.LayoutInflater;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.zp.course.R;
import com.zp.course.app.ListViewAdapter;
import com.zp.course.app.ViewHolder;
import com.zp.course.util.Validator;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.ArrayRes;
import androidx.appcompat.app.AlertDialog;

/**
 * Class description:
 *
 * @author zp
 * @version 1.0
 * @see DialogFactory
 * @since 2019/3/11
 */
public class DialogFactory {

    public static ProgressDialog showProgressDialog(Context context) {
        String message = context.getResources().getString(R.string.prompt_load_message);
        return ProgressDialog.show(context, "", message);
    }

    public static AlertDialog createAlertDialog(Context context, String title, String message, DialogButton positiveButton, DialogButton negativeButton, DialogButton neutralButton) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogStyle);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        if (Validator.isNotNull(positiveButton)) {
            builder.setPositiveButton(positiveButton.text, positiveButton.listener);
        }
        if (Validator.isNotNull(negativeButton)) {
            builder.setNegativeButton(negativeButton.text, negativeButton.listener);
        }
        if (Validator.isNotNull(neutralButton)) {
            builder.setNeutralButton(neutralButton.text, neutralButton.listener);
        }
        return builder.create();
    }

    public static AlertDialog createAlertDialog(Context context, String message, DialogInterface.OnClickListener positiveButton) {
        return createAlertDialog(context, context.getString(R.string.text_prompt), message, new DialogButton(context.getString(R.string.text_ok), positiveButton), new DialogButton(context.getString(R.string.text_cancel), null), null);
    }

    public static <T> AlertDialog createSingleChoiceDialog(Context context, String title, T[] objects, DialogInterface.OnClickListener listener, DialogButton positiveButton, DialogButton negativeButton, DialogButton neutralButton) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogStyle);
        builder.setTitle(title);
        //list of items
        ListAdapter adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_single_choice, objects);
        builder.setSingleChoiceItems(adapter, -1, listener);
        if (Validator.isNotNull(positiveButton)) {
            builder.setPositiveButton(positiveButton.text, positiveButton.listener);
        }
        if (Validator.isNotNull(negativeButton)) {
            builder.setNegativeButton(negativeButton.text, negativeButton.listener);
        }
        if (Validator.isNotNull(neutralButton)) {
            builder.setNeutralButton(neutralButton.text, neutralButton.listener);
        }
        return builder.create();
    }

    public static <T> AlertDialog createSingleChoiceDialog(Context context, String title, T[] objects, DialogInterface.OnClickListener listener) {
        return createSingleChoiceDialog(context, title, objects, listener, null, null, null);
    }

    public static <T> AlertDialog createMultipleChoiceDialog(Context context, String title, List<T> items, DialogButton positiveButton, DialogButton negativeButton, DialogButton neutralButton) {
        SimpleMultipleChoiceAdapter<T> adapter = new SimpleMultipleChoiceAdapter<>(items);
        return createMultipleChoiceDialog(context, title, adapter, positiveButton, negativeButton, neutralButton);
    }

    public static <T> AlertDialog createMultipleChoiceDialog(Context context, String title, List<T> items, DialogInterface.OnClickListener listener) {
        return createMultipleChoiceDialog(context, title, items, new DialogButton(context.getString(R.string.text_ok), listener), new DialogButton(context.getString(R.string.text_cancel), null), null);
    }

    public static <T> AlertDialog createMultipleChoiceDialog(Context context, String title, ListViewAdapter<T> adapter, DialogInterface.OnClickListener listener) {
        return createMultipleChoiceDialog(context, title, adapter, new DialogButton(context.getString(R.string.text_ok), listener), new DialogButton(context.getString(R.string.text_cancel), null), null);
    }

    public static <T> AlertDialog createMultipleChoiceDialog(Context context, String title, ListViewAdapter<T> adapter, DialogButton positiveButton, DialogButton negativeButton, DialogButton neutralButton) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogStyle);
        builder.setTitle(title);
        ListView listView = (ListView) LayoutInflater.from(context).inflate(R.layout.common_list_view, null);
        listView.setAdapter(adapter);
        listView.setDividerHeight(0);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        builder.setView(listView);

        if (Validator.isNotNull(positiveButton)) {
            builder.setPositiveButton(positiveButton.text, positiveButton.listener);
        }
        if (Validator.isNotNull(negativeButton)) {
            builder.setNegativeButton(negativeButton.text, negativeButton.listener);
        }
        if (Validator.isNotNull(neutralButton)) {
            builder.setNeutralButton(neutralButton.text, neutralButton.listener);
        }
        return builder.create();
    }

    public static AlertDialog createOptionMenuDialog(Context context, BaseAdapter adapter, AdapterView.OnItemClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogStyle_NoTitle);
        ListView listView = (ListView) LayoutInflater.from(context).inflate(R.layout.common_list_view, null);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(listener);
        builder.setView(listView);
        return builder.create();
    }

    public static <T> AlertDialog createOptionMenuDialog(Context context, List<T> list, AdapterView.OnItemClickListener listener) {
        SimpleOptionAdapter<T> adapter = new SimpleOptionAdapter<>(list);
        return createOptionMenuDialog(context, adapter, listener);
    }

    public static <T> AlertDialog createOptionMenuDialog(Context context, @ArrayRes int arrayRes, AdapterView.OnItemClickListener listener) {
        List<String> strings = Arrays.asList(context.getResources().getStringArray(arrayRes));
        SimpleOptionAdapter<String> adapter = new SimpleOptionAdapter<>(strings);
        return createOptionMenuDialog(context, adapter, listener);
    }

    @SuppressLint("ObsoleteSdkInt")
    public static DatePickerDialog createDatePickerDialog(Context context, DatePickerDialog.OnDateSetListener listener) {
        Calendar calendar = Calendar.getInstance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return new DatePickerDialog(context, R.style.AlertDialogStyle, listener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        }
        return new DatePickerDialog(context, listener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    public static class DialogButton {
        private String text;
        private DialogInterface.OnClickListener listener;

        public DialogButton(String text, DialogInterface.OnClickListener listener) {
            this.text = text;
            this.listener = listener;
        }
    }

    private static class SimpleOptionAdapter<T> extends ListViewAdapter<T> {

        public SimpleOptionAdapter(List<T> items) {
            super(items);
        }

        @Override
        protected int getLayout() {
            return android.R.layout.simple_list_item_1;
        }

        @Override
        protected void onBindViewHolder(ViewHolder holder, int position, T item) {
            TextView view = holder.get(android.R.id.text1);
            if (item != null) {
                view.setText(item.toString());
            }
        }
    }

    private static class SimpleMultipleChoiceAdapter<T> extends ListViewAdapter<T> {

        public SimpleMultipleChoiceAdapter(List<T> items) {
            super(items);
        }

        @Override
        protected int getLayout() {
            return android.R.layout.simple_list_item_multiple_choice;
        }

        @Override
        protected void onBindViewHolder(ViewHolder holder, int position, T item) {
            TextView view = holder.get(android.R.id.text1);
            if (item != null) {
                view.setText(item.toString());
            }
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
