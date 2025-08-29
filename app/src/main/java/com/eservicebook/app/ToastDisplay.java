package com.eservicebook.app;

import android.content.Context;
import android.widget.Toast;


public class ToastDisplay {

    private static volatile ToastDisplay instance = null;
    private Context context;
    private Toast toast;

    private ToastDisplay() {
        if (instance != null) {
            throw new RuntimeException("Not allowed. Please use getInstance() method");
        }
    }

    public static ToastDisplay getInstance() {
        if (instance == null) {
            synchronized (ToastDisplay.class) {
                if (instance == null) {
                    instance = new ToastDisplay();
                }
            }
        }
        return instance;
    }

    public void initialize(Context context) {
        this.context = context;
    }

    public void show(String message) {
        if (context != null) {
            if (toast != null) {
                toast.cancel();
            }
            toast = Toast.makeText(context, message, getToastDurationLength(message));
            toast.show();
        }
    }

    private int getToastDurationLength(String message) {
        if (message.length() > 15) {
            return Toast.LENGTH_LONG;
        }
        return Toast.LENGTH_SHORT;
    }
}
