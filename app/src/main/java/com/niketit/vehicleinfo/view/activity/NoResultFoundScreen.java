package com.niketit.vehicleinfo.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
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
import com.niketit.vehicleinfo.R;
import com.niketit.vehicleinfo.utils.AdIDSingleton;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

public class NoResultFoundScreen extends BaseActivity implements View.OnClickListener {

    private Context mContext;
    private LinearLayout bannerContainer;
    private LinearLayout admobContainer;
    private InterstitialAd fbInterstitialAd;
    private com.google.android.gms.ads.InterstitialAd googleInterstitialAd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vehicle_not_found);
        mContext = NoResultFoundScreen.this;
        initUIView();
        try {
            getLoadAds();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void showFBInterstitial() {
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
                getPreviousScreen();
            }
        });
    }

    private void showGoogleAdMobInterstitial() {
        googleInterstitialAd = new com.google.android.gms.ads.InterstitialAd(mContext);
        googleInterstitialAd.setAdUnitId(AdIDSingleton.INSTANCE.getAdmobInterstitialId());
        googleInterstitialAd.loadAd(new AdRequest.Builder().build());
        googleInterstitialAd.setAdListener(new AdListener() {

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
            }

            @Override
            public void onAdClosed() {
                getContactUsMail();
            }
        });
    }

    private void getLoadAds() {
        //**make fb AdRequest & show Banner

        //set fb banner ad
        if (AdIDSingleton.INSTANCE.getFbAdStatus() && AdIDSingleton.INSTANCE.getFacebookBannerAdStatus()) {
            com.facebook.ads.AdView adViewFB = new com.facebook.ads.AdView(mContext, AdIDSingleton.INSTANCE.getFacebookBannerId(), AdSize.BANNER_HEIGHT_50);
            //AdSettings.addTestDevice(getString(R.string.testdevice_fb));
            bannerContainer.addView(adViewFB);
            adViewFB.loadAd();
        }

        //set fb interstitial ad
        if (AdIDSingleton.INSTANCE.getFbAdStatus() && AdIDSingleton.INSTANCE.getFacebookInterstitialAdStatus())
            showFBInterstitial();

        //**make gms AdRequest & show Banner

        //set gms banner ad
        if (AdIDSingleton.INSTANCE.getAdMobStatus() && AdIDSingleton.INSTANCE.getAdmobBannerAdStatus()) {
            AdView adView = new AdView(mContext);
            adView.setAdSize(com.google.android.gms.ads.AdSize.BANNER);
            adView.setAdUnitId(AdIDSingleton.INSTANCE.getAdmobBannerId());
            AdRequest adRequest = new AdRequest.Builder().build();
            admobContainer.addView(adView);
            adView.loadAd(adRequest);
        }

        //set gms interstitial ad
        if (AdIDSingleton.INSTANCE.getAdMobStatus() && AdIDSingleton.INSTANCE.getAdmobInterstitialAdStatus())
            showGoogleAdMobInterstitial();
    }

    private void initUIView() {
        bannerContainer = findViewById(R.id.banner_container_error);
        admobContainer = findViewById(R.id.admob_container_error);
        AppCompatTextView resultNotFound = findViewById(R.id.result_not_found_tv);
        AppCompatButton contactUs = findViewById(R.id.contact_us_bt);
        AppCompatButton searchOther = findViewById(R.id.search_other_bt);
        AppCompatImageView back_bt = findViewById(R.id.back_bt);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/bahnschrift.ttf");
        resultNotFound.setTypeface(typeface);
        contactUs.setOnClickListener(this);
        searchOther.setOnClickListener(this);
        back_bt.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.contact_us_bt:
                if (AdIDSingleton.INSTANCE.getAdMobStatus() && AdIDSingleton.INSTANCE.getAdmobInterstitialAdStatus()) {
                    if (googleInterstitialAd != null && googleInterstitialAd.isLoaded()) {
                        googleInterstitialAd.show();
                        showGoogleAdMobInterstitial();
                    } else
                        getContactUsMail();
                } else
                    getContactUsMail();
                break;
            case R.id.search_other_bt:
                if (AdIDSingleton.INSTANCE.getFbAdStatus() && AdIDSingleton.INSTANCE.getFacebookInterstitialAdStatus()) {
                    if (fbInterstitialAd != null && fbInterstitialAd.isAdLoaded()) {
                        fbInterstitialAd.show();
                        showFBInterstitial();
                    } else
                        getPreviousScreen();
                } else
                    getPreviousScreen();
                break;
            case R.id.back_bt:
                getPreviousScreen();
                break;
        }
    }

    public void getPreviousScreen() {
        startActivity(new Intent(mContext, MainActivity.class));
        finish();
    }

    private void getContactUsMail() {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("plain/text");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"niketitservices@yhaoo.com"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback Vehicle Info");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "");
        startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (fbInterstitialAd != null)
            fbInterstitialAd.destroy();
        if (googleInterstitialAd != null)
            googleInterstitialAd = null;
    }
}