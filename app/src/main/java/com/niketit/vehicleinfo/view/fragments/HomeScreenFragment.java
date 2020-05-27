package com.niketit.vehicleinfo.view.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSize;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.snackbar.Snackbar;
import com.niketit.vehicleinfo.BuildConfig;
import com.niketit.vehicleinfo.R;
import com.niketit.vehicleinfo.controller.SetResponseData;
import com.niketit.vehicleinfo.controller.apiconnection.ApiConnection;
import com.niketit.vehicleinfo.controller.apiconnection.RetrofitApiClient;
import com.niketit.vehicleinfo.controller.interfaces.ApiInterface;
import com.niketit.vehicleinfo.controller.interfaces.ApiResponseCallback;
import com.niketit.vehicleinfo.utils.AdIDSingleton;
import com.niketit.vehicleinfo.utils.IsAdLoaded;
import com.niketit.vehicleinfo.utils.MCrypt;
import com.niketit.vehicleinfo.utils.database.EventEntity;
import com.niketit.vehicleinfo.view.RTOApplication;
import com.niketit.vehicleinfo.view.activity.History;
import com.niketit.vehicleinfo.view.activity.NoResultFoundScreen;
import com.niketit.vehicleinfo.view.activity.NumberDetails;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import retrofit2.Call;

public class HomeScreenFragment extends BaseFragment implements View.OnClickListener, ApiResponseCallback {

    private Context mContext;
    private AppCompatEditText vehicleNo;
    private ApiInterface apiService;
    private String numberInfo;
    private SpinKitView spinKitView;
    private LinearLayout searchNumber;
    private LinearLayout bannerContainer;
    private LinearLayout admobContainer;
    private com.facebook.ads.InterstitialAd fbInterstitialAd;
    private com.google.android.gms.ads.InterstitialAd googleInterstitialAd;
    private com.facebook.ads.InterstitialAd fbInterstitialAdAfterDetail;
    private List<String> searchMessages;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home_screen, container, false);
        mContext = getActivity();
        initUIView(rootView);
        searchMessages = new ArrayList<>();
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            getLoadAds();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        searchMessages.add("Connecting");
        searchMessages.add("Searching");
        searchMessages.add("Querying database");
    }

    private void getLoadAds() {
        //***make fb AdRequest & show Banner

        //check for fb Banner ad
        if (AdIDSingleton.INSTANCE.getFbAdStatus() && AdIDSingleton.INSTANCE.getFacebookBannerAdStatus()) {
            com.facebook.ads.AdView adViewFB = new com.facebook.ads.AdView(mContext, AdIDSingleton.INSTANCE.getFacebookBannerId(), AdSize.BANNER_HEIGHT_50);
            //AdSettings.addTestDevice(getString(R.string.testdevice_fb));
            bannerContainer.addView(adViewFB);
            adViewFB.loadAd();
        }

        //check for fb Interstitial ad
        if (AdIDSingleton.INSTANCE.getFbAdStatus() && AdIDSingleton.INSTANCE.getFacebookInterstitialAdStatus())
            showFBInterstitial();

        //***make gms AdRequest & show Banner

        //check for gms banner ad
        if (AdIDSingleton.INSTANCE.getAdMobStatus() && AdIDSingleton.INSTANCE.getAdmobBannerAdStatus()) {
            AdView adView = new AdView(mContext);
            adView.setAdSize(com.google.android.gms.ads.AdSize.BANNER);
            adView.setAdUnitId(AdIDSingleton.INSTANCE.getAdmobBannerId());
            AdRequest adRequest = new AdRequest.Builder().build();
            admobContainer.addView(adView);
            adView.loadAd(adRequest);
        }

        //check for gms Interstitial ad
        if (AdIDSingleton.INSTANCE.getAdMobStatus() && AdIDSingleton.INSTANCE.getAdmobInterstitialAdStatus())
            showGoogleAdMobInterstitial();
    }

    private void initUIView(View rootView) {
        spinKitView = rootView.findViewById(R.id.spin_kit);
        AppCompatTextView enterVehicle = rootView.findViewById(R.id.enter_vehicle_tv);
        vehicleNo = rootView.findViewById(R.id.vehicle_no_et);
        searchNumber = rootView.findViewById(R.id.search_number_ly);
        bannerContainer = rootView.findViewById(R.id.banner_container);
        admobContainer = rootView.findViewById(R.id.admob_container);

        LinearLayout history = rootView.findViewById(R.id.history_ly);
        AppCompatTextView termsConditions = rootView.findViewById(R.id.terms_text);

        SpannableString content = new SpannableString(getString(R.string.terms_and_conditions));
        content.setSpan(new UnderlineSpan(), 35, content.length(), 0);
        termsConditions.setText(content);

        Typeface typefaceLp = Typeface.createFromAsset(mContext.getAssets(), "fonts/LicensePlate.ttf");
        Typeface typefaceB = Typeface.createFromAsset(mContext.getAssets(), "fonts/bahnschrift.ttf");
        enterVehicle.setTypeface(typefaceB);
        termsConditions.setTypeface(typefaceB);
        vehicleNo.setTypeface(typefaceLp);

        searchNumber.setOnClickListener(this);
        history.setOnClickListener(this);
        termsConditions.setOnClickListener(this);

        vehicleNo.setOnEditorActionListener((v, actionId, event) -> {
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                getSearchNumberData();
            }
            return false;
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.search_number_ly:
                getSearchNumberData();
                break;
            case R.id.history_ly:
                checkIsDataAvailable();
                break;
            case R.id.terms_text:
                getPrivacyPolicy();
                break;
        }
    }

    private void getSearchNumberData() {
        hideKeyboard();
        Animation bounceEffectSearch = AnimationUtils.loadAnimation(mContext, R.anim.bounce_effect);
        if (!isInternetAvailable()) {
            showSnackBar(getString(R.string.check_internet));
        } else {
            searchNumber.startAnimation(bounceEffectSearch);
            numberInfo = Objects.requireNonNull(vehicleNo.getText()).toString().trim();
            if (!TextUtils.isEmpty(numberInfo)) {
                if (numberInfo.length() > 10 || numberInfo.length() < 5)
                    showSnackBar(getString(R.string.not_valid_number));
                else {
                    if (AdIDSingleton.INSTANCE.getFbAdStatus() && AdIDSingleton.INSTANCE.getFacebookInterstitialAdStatus()) {
                        int randomNumberInRange = getRandomNumberInRange();
                        if (randomNumberInRange == 1) {
                            if (fbInterstitialAd != null && fbInterstitialAd.isAdLoaded()) {
                                fbInterstitialAd.show();
                                showFBInterstitial();
                                IsAdLoaded.INSTANCE.setRandomNumberSearch(true);
                                IsAdLoaded.INSTANCE.setRandomNumberHistory(false);
                            } else {
                                spinKitView.setVisibility(View.VISIBLE);
                                getVehicleDetails(numberInfo);
                            }
                        } else {
                            if (googleInterstitialAd != null && googleInterstitialAd.isLoaded()) {
                                googleInterstitialAd.show();
                                showGoogleAdMobInterstitial();
                                IsAdLoaded.INSTANCE.setRandomNumberSearch(true);
                                IsAdLoaded.INSTANCE.setRandomNumberHistory(false);
                            } else {
                                spinKitView.setVisibility(View.VISIBLE);
                                getVehicleDetails(numberInfo);
                            }
                        }
                    } else {
                        spinKitView.setVisibility(View.VISIBLE);
                        getVehicleDetails(numberInfo);
                    }
                }
            } else
                showSnackBar(getString(R.string.please_enter_number));
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
                if (IsAdLoaded.INSTANCE.getRandomNumberSearch()) {
                    spinKitView.setVisibility(View.VISIBLE);
                    getVehicleDetails(numberInfo);
                }
                if (IsAdLoaded.INSTANCE.getRandomNumberHistory()) {
                    startActivity(new Intent(mContext, History.class));
                }
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
                if (IsAdLoaded.INSTANCE.getRandomNumberSearch()) {
                    spinKitView.setVisibility(View.VISIBLE);
                    getVehicleDetails(numberInfo);
                }
                if (IsAdLoaded.INSTANCE.getRandomNumberHistory()) {
                    startActivity(new Intent(mContext, History.class));
                }
            }
        });
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        Objects.requireNonNull(imm).hideSoftInputFromWindow(searchNumber.getWindowToken(), 0);
    }

    private void getPrivacyPolicy() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse((getString(R.string.privacy_policy_url))));
        startActivity(browserIntent);
    }

    private void checkIsDataAvailable() {
        new Thread(() -> {
            List<EventEntity> eventEntities = RTOApplication.getsInstance().getDatabase().rtoEventDao().getAllSavedVehicles();
            if (eventEntities.isEmpty()) {
                showSnackBar(getString(R.string.history_not_found));
            } else {
                if (AdIDSingleton.INSTANCE.getAdMobStatus() && AdIDSingleton.INSTANCE.getAdmobInterstitialAdStatus())
                    showHistoryData();
                else
                    startActivity(new Intent(mContext, History.class));
            }
        }).start();
    }

    private void showHistoryData() {
        new Handler(Looper.getMainLooper()).post(() -> {
            int randomNumberInRange = getRandomNumberInRange();
            if (randomNumberInRange == 1) {
                if (googleInterstitialAd != null && googleInterstitialAd.isLoaded()) {
                    googleInterstitialAd.show();
                    showGoogleAdMobInterstitial();
                    IsAdLoaded.INSTANCE.setRandomNumberHistory(true);
                    IsAdLoaded.INSTANCE.setRandomNumberSearch(false);
                } else {
                    startActivity(new Intent(mContext, History.class));
                }
            } else {
                if (fbInterstitialAd != null && fbInterstitialAd.isAdLoaded()) {
                    fbInterstitialAd.show();
                    showFBInterstitial();
                    IsAdLoaded.INSTANCE.setRandomNumberHistory(true);
                    IsAdLoaded.INSTANCE.setRandomNumberSearch(false);
                } else {
                    startActivity(new Intent(mContext, History.class));
                }
            }
        });
    }

    private void showSnackBar(String message) {
        Snackbar snackbar = Snackbar.make(Objects.requireNonNull(getActivity()).findViewById(android.R.id.content), message, 1500);
        snackbar.setAction("DISMISS", v -> snackbar.dismiss());
        snackbar.setActionTextColor(Color.RED);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(Color.WHITE);
        TextView textView = sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.BLUE);
        textView.setTextSize(12);
        snackbar.show();
    }

    private Map<String, String> getParams(String numberInfo) {
        HashMap<String, String> params = new HashMap<>();
        MCrypt mCrypt = new MCrypt();
        try {
            String encryptNumber = MCrypt.bytesToHex(mCrypt.encrypt(numberInfo));
            params.put("number", encryptNumber);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return params;
    }

    private void getVehicleDetails(String numberInfo) {
        if (apiService == null) {
            apiService = RetrofitApiClient.getClient().create(ApiInterface.class);
        }
        Call<String> call = apiService.getInfoFromNumber(getParams(numberInfo));
        new ApiConnection<String>(HomeScreenFragment.this).makeServiceConnection(call, mContext, BuildConfig.GET_VEHICLE_INFO);
    }

    @Override
    public void onResponse(Object response, String type) {
        if (type.equalsIgnoreCase(BuildConfig.GET_VEHICLE_INFO)) {
            spinKitView.setVisibility(View.INVISIBLE);
            if (response != null) {
                String result = String.valueOf(response);
                if (!TextUtils.isEmpty(result)) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(result);
                        if (jsonObject.has("info")) {
                            JSONObject jsonObjectInfo = jsonObject.optJSONObject("info");
                            if (jsonObjectInfo.optBoolean("error")) {
                                startActivity(new Intent(mContext, NoResultFoundScreen.class));
                                Objects.requireNonNull(getActivity()).finish();
                            } else {
                                SetResponseData setResponseData = new SetResponseData(numberInfo);
                                setResponseData.setDataResponse(result);
                                Intent intentNumberInfo = new Intent(mContext, NumberDetails.class);
                                AdIDSingleton.INSTANCE.setVehicleNumber(numberInfo);
                                startActivity(intentNumberInfo);
                            }
                        } else if (jsonObject.has("error")) {
                            if (jsonObject.optBoolean("error")) {
                                startActivity(new Intent(mContext, NoResultFoundScreen.class));
                            }
                        } else {
                            startActivity(new Intent(mContext, NoResultFoundScreen.class));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void onErrorMessage(Object errorMessage) {
        spinKitView.setVisibility(View.INVISIBLE);
        toastMsg(errorMessage.toString());
    }

    @Override
    public void showNetworkToast(String message) {
        spinKitView.setVisibility(View.INVISIBLE);
        toastMsg(message);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (fbInterstitialAd != null)
            fbInterstitialAd.destroy();
        if (fbInterstitialAdAfterDetail != null)
            fbInterstitialAdAfterDetail.destroy();
    }

    private void showFbInterstitialAdAfterDetail() {
        fbInterstitialAdAfterDetail = new InterstitialAd(mContext, AdIDSingleton.INSTANCE.getFacebookInterstitialId());
        fbInterstitialAdAfterDetail.loadAd();
        fbInterstitialAdAfterDetail.setAdListener(new InterstitialAdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {

            }

            @Override
            public void onAdLoaded(Ad ad) {
                if (fbInterstitialAdAfterDetail.isAdLoaded())
                    fbInterstitialAdAfterDetail.show();
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
                AdIDSingleton.INSTANCE.setDetailShown(false);
            }
        });
    }

    private static int getRandomNumberInRange() {
        int min = 1;
        int max = 2;
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }
}