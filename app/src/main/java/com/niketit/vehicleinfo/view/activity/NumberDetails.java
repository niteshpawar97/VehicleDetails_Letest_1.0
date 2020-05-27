package com.niketit.vehicleinfo.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSize;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.NativeExpressAdView;
import com.niketit.vehicleinfo.BuildConfig;
import com.niketit.vehicleinfo.R;
import com.niketit.vehicleinfo.controller.adapter.VehicleDetailAdapter;
import com.niketit.vehicleinfo.model.VehicleInfo;
import com.niketit.vehicleinfo.utils.AdIDSingleton;
import com.niketit.vehicleinfo.view.RTOApplication;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Set;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class NumberDetails extends BaseActivity implements View.OnClickListener {

    private Context mContext;
    private RecyclerView numberDetailLv;
    private StringBuilder stringBuilder;
    private boolean isCallFromHistory = false;
    private InterstitialAd fbInterstitialAd;
    private LinearLayout bannerFBContainer;
    private LinearLayout bannerGMSContainer;
    private ArrayList<Object> vehicleInfoArrayList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.number_details);
        mContext = NumberDetails.this;
        initUIView();
        try {
            getLoadAds();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        setVehicleDetails();
    }

    private void setVehicleDetails() {
        LinkedHashMap<String, String> vehicleInfoTable = RTOApplication.getsInstance().getNumberDetails();
        if (!vehicleInfoTable.isEmpty()) {
            Set<String> keys = vehicleInfoTable.keySet();
            stringBuilder = new StringBuilder();
            stringBuilder.append("Vehicle Registration Details:" + "\n\n");
            for (String key : keys) {
                VehicleInfo vehicleInfo = new VehicleInfo();
                vehicleInfo.setName(key);
                vehicleInfo.setValue(vehicleInfoTable.get(key));
                String shareDetail = " \u2022 " + key + " " + vehicleInfoTable.get(key) + "\n";
                stringBuilder.append(shareDetail);
                vehicleInfoArrayList.add(vehicleInfo);
            }
            VehicleDetailAdapter detailAdapter = new VehicleDetailAdapter(mContext, vehicleInfoArrayList);
            numberDetailLv.setAdapter(detailAdapter);
            if (AdIDSingleton.INSTANCE.getAdMobStatus() && AdIDSingleton.INSTANCE.getAdmobNativeAdStatus())
                addNativeExpressAds();
            detailAdapter.notifyDataSetChanged();
        }
    }

    private void initUIView() {
        AppCompatTextView setNumber = findViewById(R.id.set_number_tv);
        numberDetailLv = findViewById(R.id.number_detail_lv);
        LinearLayout shareLy = findViewById(R.id.share_ly);
        bannerFBContainer = findViewById(R.id.fb_banner_container_dtls);
        bannerGMSContainer = findViewById(R.id.gms_banner_container);
        setNumber.setText(AdIDSingleton.INSTANCE.getVehicleNumber());
        if (getIntent() != null) {
            isCallFromHistory = getIntent().getBooleanExtra("isActionFromHistory", false);
        }
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/LicensePlate.ttf");
        setNumber.setTypeface(typeface);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        numberDetailLv.setLayoutManager(linearLayoutManager);
        shareLy.setOnClickListener(this);
    }

    private void getLoadAds() {
        //**make fb AdRequest & show Banner

        //set fb banner ad
        if (AdIDSingleton.INSTANCE.getFbAdStatus() && AdIDSingleton.INSTANCE.getFacebookBannerAdStatus()) {
            com.facebook.ads.AdView adViewFB = new com.facebook.ads.AdView(mContext, AdIDSingleton.INSTANCE.getFacebookBannerId(), AdSize.BANNER_HEIGHT_50);
            //AdSettings.addTestDevice(getString(R.string.testdevice_fb));
            bannerFBContainer.addView(adViewFB);
            adViewFB.loadAd();
        }

        //set gms banner ad
        if (AdIDSingleton.INSTANCE.getAdMobStatus() && AdIDSingleton.INSTANCE.getAdmobBannerAdStatus()) {
            AdView adView = new AdView(mContext);
            adView.setAdSize(com.google.android.gms.ads.AdSize.BANNER);
            adView.setAdUnitId(AdIDSingleton.INSTANCE.getAdmobBannerId());
            AdRequest adRequest = new AdRequest.Builder().build();
            bannerGMSContainer.addView(adView);
            adView.loadAd(adRequest);
        }

        //set fb interstitial ad
        if (AdIDSingleton.INSTANCE.getFbAdStatus() && AdIDSingleton.INSTANCE.getFacebookInterstitialAdStatus())
            showFbInterstitialAd();
    }

    private void showFbInterstitialAd() {
        fbInterstitialAd = new InterstitialAd(mContext, AdIDSingleton.INSTANCE.getFacebookInterstitialId());
        fbInterstitialAd.loadAd();
        fbInterstitialAd.setAdListener(new InterstitialAdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
            }

            @Override
            public void onAdLoaded(Ad ad) {
            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }

            @Override
            public void onInterstitialDisplayed(Ad ad) {

            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                getShare();
            }
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.share_ly:
                if (AdIDSingleton.INSTANCE.getFbAdStatus() && AdIDSingleton.INSTANCE.getFacebookInterstitialAdStatus()) {
                    if (fbInterstitialAd != null && fbInterstitialAd.isAdLoaded()) {
                        fbInterstitialAd.show();
                        showFbInterstitialAd();
                    } else
                        getShare();
                } else
                    getShare();
                break;
        }
    }

    public void getShare() {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        share.putExtra(Intent.EXTRA_SUBJECT, "RTO Vehicle Info");
        stringBuilder.append("For information of other Vehicles download\"Vehicle Info\" app" + "\n\n");
        try {
            URL appURL = new URL("https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
            stringBuilder.append(appURL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        share.putExtra(Intent.EXTRA_TEXT, stringBuilder.toString());
        startActivity(Intent.createChooser(share, getString(R.string.share_information)));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (fbInterstitialAd != null)
            fbInterstitialAd.destroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (isCallFromHistory)
            startActivity(new Intent(mContext, History.class));
        else
            startActivity(new Intent(mContext, MainActivity.class));
    }

    private void addNativeExpressAds() {
        int mid = vehicleInfoArrayList.size() / 2;
        NativeExpressAdView adView = new NativeExpressAdView(mContext);
        vehicleInfoArrayList.add((mid), adView);
        setUpAndLoadNativeExpressAds(mid);
    }

    private void setUpAndLoadNativeExpressAds(int mid) {
        numberDetailLv.post(() -> {
            final NativeExpressAdView adView = (NativeExpressAdView) vehicleInfoArrayList.get(mid);
            adView.setAdSize(com.google.android.gms.ads.AdSize.MEDIUM_RECTANGLE);
            adView.setAdUnitId(AdIDSingleton.INSTANCE.getAdmobNativeBannerId());
            loadNativeExpressAd(mid);
        });
    }

    private void loadNativeExpressAd(final int index) {
        if (index >= vehicleInfoArrayList.size()) {
            return;
        }
        Object item = vehicleInfoArrayList.get(index);
        if (!(item instanceof NativeExpressAdView)) {
            throw new ClassCastException("Expected item at index " + index + " to be a Native"
                    + " Express ad.");
        }
        NativeExpressAdView adView = (NativeExpressAdView) item;
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                loadNativeExpressAd(index);
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Log.e("MainActivity", "The previous Native Express ad failed to load. Attempting to"
                        + " load the next Native Express ad in the items list.");
                loadNativeExpressAd(index);
            }
        });
        adView.loadAd(new AdRequest.Builder().build());
    }
}