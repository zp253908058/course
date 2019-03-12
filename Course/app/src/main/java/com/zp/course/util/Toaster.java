package com.zp.course.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

/**
 * Class description:
 *
 * @author zp
 * @version 1.0
 * @see Toaster
 * @since 2019/3/11
 */
public class Toaster {

    private static Toast sToast;

    public static void showToast(String text) {
        sToast.cancel();
        sToast.setText(text);
        sToast.show();
    }

    @SuppressLint("ShowToast")
    public static void init(Context context) {
        if (sToast != null) {
            return;
        }
        sToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
    }
}
