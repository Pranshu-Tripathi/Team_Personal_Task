package com.example.todolist;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import java.util.logging.Handler;

class PleaseWaitDialog {

    private Activity activity;
    private AlertDialog dialog;

    PleaseWaitDialog(Activity myActivity){
        activity = myActivity;
    }

    void startLoadingDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custom_dialog,null));
        builder.setCancelable(false);

        dialog = builder.create();
        dialog.show();
    }

    void dismissDialog(){
        dialog.dismiss();

    }
}
