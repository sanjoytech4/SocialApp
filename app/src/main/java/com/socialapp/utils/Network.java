package com.socialapp.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;

import com.socialapp.R;

public class Network
{
    private Context context;
    public Network(Context context)
    {
        this.context=context;
    }

    public boolean isAvailable(boolean showMessage)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if(activeNetworkInfo == null || !activeNetworkInfo.isConnected())
        {
            if(showMessage)
            {
                showNetworkProblemDialog();
            }
            return false;
        }
        return true;
    }

    /*called to show network problem to user */
    private void showNetworkProblemDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(context.getResources().getString(R.string.check_network));
        builder.setPositiveButton(context.getResources().getString(R.string.app_close_dialog_button_ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
}
