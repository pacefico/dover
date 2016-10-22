package com.example.fugliara.ccs002.views.fragments;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by fugliara on 10/12/2015.
 */
public class ProgressWait extends ProgressDialog {
    public ProgressWait(Context context, String message) {
        super(context);
        setMessage(message);
        setCancelable(false);
    }
}
