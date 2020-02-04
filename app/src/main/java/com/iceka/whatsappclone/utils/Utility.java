package com.iceka.whatsappclone.utils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

public class Utility {

    public static void setMargins(View view) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(0, 0, 8, 0);
            view.requestLayout();
        }
    }

    public static void setProgressMax(ProgressBar pb) {
        pb.setMax(100 * 100);
    }

}
