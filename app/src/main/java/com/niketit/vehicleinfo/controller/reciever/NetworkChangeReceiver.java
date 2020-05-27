package com.niketit.vehicleinfo.controller.reciever;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;


import com.niketit.vehicleinfo.utils.NetworkUtil;

import org.greenrobot.eventbus.EventBus;

import androidx.annotation.RequiresApi;

/**
 * Network change Receiver helps to detect
 * any n/w connection changes
 */
public class NetworkChangeReceiver extends BroadcastReceiver {

    EventBus busNwReciever = EventBus.getDefault();

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(final Context context, final Intent intent) {
        String status = NetworkUtil.getConnectivityStatusString(context);
        if (status != null) {
            if (status.equals("Not connected to Internet")) {
                try {
                    busNwReciever.post("Disconnected");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    busNwReciever.post("Connected");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}