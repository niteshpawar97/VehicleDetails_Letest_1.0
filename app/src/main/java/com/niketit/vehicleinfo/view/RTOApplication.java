package com.niketit.vehicleinfo.view;

import android.app.Application;


import com.facebook.ads.AdSettings;
import com.facebook.ads.AudienceNetworkAds;
import com.niketit.vehicleinfo.utils.database.RTODatabase;

import java.util.LinkedHashMap;

import androidx.room.Room;

public class RTOApplication extends Application {

    private static RTOApplication sInstance;

    private LinkedHashMap<String, String> numberDetails = new LinkedHashMap<>();

    private RTODatabase rtoDatabase;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        rtoDatabase = Room.databaseBuilder(getApplicationContext(), RTODatabase.class, "RTODatabase").build();

        //init Facebook Native ads SDK
        AudienceNetworkAds.initialize(this);
        AdSettings.setMultiprocessSupportMode(AdSettings.MultiprocessSupportMode.MULTIPROCESS_SUPPORT_MODE_OFF);
        if (AudienceNetworkAds.isInAdsProcess(this)) {
            return;
        }
    }

    public static RTOApplication getsInstance() {
        return sInstance;
    }

    public void setNumberDetails(LinkedHashMap<String, String> numberDetails) {
        this.numberDetails = numberDetails;
    }

    public LinkedHashMap<String, String> getNumberDetails() {
        return numberDetails;
    }

    public RTODatabase getDatabase() {
        return rtoDatabase;
    }
}