package com.niketit.vehicleinfo.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.gms.ads.MobileAds;
import com.niketit.vehicleinfo.BuildConfig;
import com.niketit.vehicleinfo.R;
import com.niketit.vehicleinfo.controller.apiconnection.ApiConnection;
import com.niketit.vehicleinfo.controller.apiconnection.RetrofitApiClient;
import com.niketit.vehicleinfo.controller.interfaces.ApiInterface;
import com.niketit.vehicleinfo.controller.interfaces.ApiResponseCallback;
import com.niketit.vehicleinfo.model.AdDetailInfo;
import com.niketit.vehicleinfo.utils.AdIDSingleton;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import retrofit2.Call;

public class SplashActivity extends BaseActivity implements ApiResponseCallback {

    private Context mContext;
    private ApiInterface apiService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mContext = SplashActivity.this;
        initUIView();
        AdIDSingleton.INSTANCE.setDetailShown(false);
        getAdDetails();
        getNextScreen();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void initUIView() {
        AppCompatTextView fav = findViewById(R.id.fav_tv);
        AppCompatTextView vehicleInfo = findViewById(R.id.vehicle_info_tv);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/bahnschrift.ttf");
        fav.setTypeface(typeface);
        vehicleInfo.setTypeface(typeface);
    }

    public void getNextScreen() {
        int splashTime = 2500;
        new Handler().postDelayed(() -> {
            startActivity(new Intent(mContext, MainActivity.class));
            finish();
        }, splashTime);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getNextScreen();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        exitFromApp();
    }

    private void getAdDetails() {
        if (apiService == null) {
            apiService = RetrofitApiClient.getClient().create(ApiInterface.class);
        }
        Call<AdDetailInfo> call = apiService.getAdDetails();
        new ApiConnection<AdDetailInfo>(SplashActivity.this).makeServiceConnection(call, mContext, BuildConfig.GET_AD_DETAILS);
    }

    @Override
    public void onResponse(Object response, String type) {
        if (type.equalsIgnoreCase(BuildConfig.GET_AD_DETAILS)) {
            if (response != null) {
                AdDetailInfo adDetailInfo = (AdDetailInfo) response;
                List<AdDetailInfo.AdsInfo> adsInfos = adDetailInfo.getAdsInfo();
                for (int i = 0; i < adsInfos.size(); i++) {
                    AdIDSingleton.INSTANCE.setVersionCode(adsInfos.get(i).getAppVersionCode());
                    AdIDSingleton.INSTANCE.setAppLink(adsInfos.get(i).getAppLink());

                    //set status of FB and gms ads
                    AdIDSingleton.INSTANCE.setAdMobStatus(adsInfos.get(i).getAdmobAdStatus());
                    AdIDSingleton.INSTANCE.setFbAdStatus(adsInfos.get(i).getFacebookAdStatus());
                    AdIDSingleton.INSTANCE.setAdmobBannerAdStatus(adsInfos.get(i).getAdmobBannerAdStatus());
                    AdIDSingleton.INSTANCE.setAdmobInterstitialAdStatus(adsInfos.get(i).getAdmobInterstitialAdStatus());
                    AdIDSingleton.INSTANCE.setAdmobNativeAdStatus(adsInfos.get(i).getAdmobNativeAdStatus());
                    AdIDSingleton.INSTANCE.setFacebookBannerAdStatus(adsInfos.get(i).getFacebookBannerAdStatus());
                    AdIDSingleton.INSTANCE.setFacebookInterstitialAdStatus(adsInfos.get(i).getFacebookInterstitialAdStatus());
                    AdIDSingleton.INSTANCE.setFacebookNativeAdStatus(adsInfos.get(i).getFacebookNativeAdStatus());
                    AdIDSingleton.INSTANCE.setFacebookNativeBannerAdStatus(adsInfos.get(i).getFacebookNativeBannerAdStatus());

                    AdIDSingleton.INSTANCE.setAdmobAppId(adsInfos.get(i).getAdmobAppId());
                    AdIDSingleton.INSTANCE.setAdmobBannerId((adsInfos.get(i).getAdmobBannerId()));
                    AdIDSingleton.INSTANCE.setAdmobInterstitialId(adsInfos.get(i).getAdmobInterstitialId());
                    AdIDSingleton.INSTANCE.setAdmobNativeBannerId(adsInfos.get(i).getAdmobNativeBannerId());
                    AdIDSingleton.INSTANCE.setFacebookBannerId(adsInfos.get(i).getFacebookBannerId());
                    AdIDSingleton.INSTANCE.setFacebookInterstitialId(adsInfos.get(i).getFacebookInterstitialId());
                    AdIDSingleton.INSTANCE.setFacebookNativeId(adsInfos.get(i).getFacebookNativeId());
                    AdIDSingleton.INSTANCE.setFacebookNativeBannerId(adsInfos.get(i).getFacebookNativeBannerId());

                    //init gms ads SDK
                    MobileAds.initialize(this, AdIDSingleton.INSTANCE.getAdmobAppId());
                }
            }
        }
    }

    @Override
    public void onErrorMessage(Object errorMessage) {
        toastMsg(errorMessage.toString());
    }

    @Override
    public void showNetworkToast(String message) {
        toastMsg(message);
    }
}