package com.myproject.myvehicleapp.Utilities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.core.content.ContextCompat;

import com.myproject.myvehicleapp.R;


public class Tools {

    /**
     * Sets the system bar color of the activity.
     * This method is used to change the color of the status bar (navigation bar) on devices running Android Lollipop (API 21) and above.
     *
     * @param act The activity for which the system bar color should be set.
     */
    public static void setSystemBarColor(Activity act) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = act.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(act.getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    /**
     * Sets the system bar color of the activity with a custom color.
     * This method is used to change the color of the status bar (navigation bar) on devices running Android Lollipop (API 21) and above.
     *
     * @param act   The activity for which the system bar color should be set.
     * @param color The color resource ID to be used for the system bar color.
     */
    public static void setSystemBarColor(Activity act, @ColorRes int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = act.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(act.getResources().getColor(color));
        }
    }
}

