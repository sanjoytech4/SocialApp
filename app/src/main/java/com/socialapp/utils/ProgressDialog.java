package com.socialapp.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.socialapp.R;


/**
 * Created by priyanka on 8/17/2015.
 */
public class ProgressDialog extends Dialog {
    static String progressText="";

    /**
     * Create a Dialog window that uses the default dialog frame style.
     *
     * @param context The Context the Dialog is to run it.  In particular, it
     *                uses the window manager and theme in this context to
     *                present its UI.
     */
    public ProgressDialog(Context context, String progressText) {
        super(context);
        this.progressText=progressText;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // This is the layout XML file that describes your Dialog layout
        this.setContentView(R.layout.progress_dialog);
         TextView progressTextView=(TextView)findViewById(R.id.progress_text);
        MaterialProgressBar progressView=(MaterialProgressBar)findViewById(R.id.progress);
        progressView.setColorSchemeColors(getContext().getResources().getColor(R.color.default_control_activated_color));
        progressTextView.setText(progressText);
    }




}