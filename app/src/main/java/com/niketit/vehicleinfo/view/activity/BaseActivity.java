package com.niketit.vehicleinfo.view.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.niketit.vehicleinfo.R;
import com.niketit.vehicleinfo.controller.reciever.NetworkChangeReceiver;
import com.niketit.vehicleinfo.utils.Constants;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class BaseActivity extends AppCompatActivity {

    private NetworkChangeReceiver myReceiver = new NetworkChangeReceiver();
    private static Dialog dialog = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        //register broadcast receiver
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(myReceiver,
                    new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    //validate email
    public boolean validateEmail(String email) {
        boolean flagOfEmailValidation = true;
        int atPos = email.indexOf("@");
        int dotPos = email.lastIndexOf(".");
        if (atPos < 1 || dotPos < atPos + 2 || dotPos + 2 >= email.length()) {
            flagOfEmailValidation = false;
        }
        return flagOfEmailValidation;
    }

    @Subscribe
    public void onEvent(String name) {
        if (name.equalsIgnoreCase("Disconnected")) {
            toastMsg("Device is offline!");
        }
    }

    public void toastMsg(String message) {
        makeText(getApplicationContext(), message, LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            unregisterReceiver(myReceiver);
        }
    }

    protected void exitFromApp() {
        Intent localIntent = new Intent("android.intent.action.MAIN");
        localIntent.addCategory("android.intent.category.HOME");
        localIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(localIntent);
        finish();
    }


    public static void showLoader(Context context) {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            dialog = new Dialog(context, android.R.style.Theme_DeviceDefault_Light_NoActionBar_TranslucentDecor);
        } else {
            dialog = new Dialog(context);
        }
        if (dialog.getWindow() != null)
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_loader);

    }

    public static void stopLoader() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public static void getLogd(Exception message) {
        Log.d(Constants.LOG_TAG, message.toString());
    }

    public static void getLogv(String message) {
        Log.v(Constants.LOG_TAG, message);
    }
}