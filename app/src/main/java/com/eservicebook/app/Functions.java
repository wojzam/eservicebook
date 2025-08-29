package com.eservicebook.app;

import android.content.Context;
import android.util.DisplayMetrics;

public class Functions {
    public static int dpToPixel(float dp, Context context) {
        return (int) (dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT));
    }
}
