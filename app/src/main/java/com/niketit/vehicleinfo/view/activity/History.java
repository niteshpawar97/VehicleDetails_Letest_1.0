package com.niketit.vehicleinfo.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import com.facebook.ads.AdSize;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.NativeExpressAdView;
import com.niketit.vehicleinfo.BuildConfig;
import com.niketit.vehicleinfo.R;
import com.niketit.vehicleinfo.controller.SetResponseData;
import com.niketit.vehicleinfo.controller.adapter.HistoryAdapter;
import com.niketit.vehicleinfo.controller.apiconnection.ApiConnection;
import com.niketit.vehicleinfo.controller.apiconnection.RetrofitApiClient;
import com.niketit.vehicleinfo.controller.interfaces.ApiInterface;
import com.niketit.vehicleinfo.controller.interfaces.ApiResponseCallback;
import com.niketit.vehicleinfo.controller.interfaces.OnClickHistoryItem;
import com.niketit.vehicleinfo.utils.AdIDSingleton;
import com.niketit.vehicleinfo.utils.MCrypt;
import com.niketit.vehicleinfo.utils.database.EventEntity;
import com.niketit.vehicleinfo.view.RTOApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class History extends BaseActivity implements
        ApiResponseCallback, OnClickHistoryItem, View.OnClickListener {

    private RecyclerView history;
    private Context mContext;
    private ApiInterface apiService;
    private String numberVehicle;
    private SpinKitView spin_kit;
    private LinearLayout bannerContainer;
    private LinearLayout admobContainer;
    private ArrayList<Object> historyInfoArrayList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);
        mContext = History.this;
        initUIView();
        try {
            getLoadAds();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        getDataFromDB();
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
    }

    public void getDataFromDB() {
        new Thread(() -> {
            //get data from database if any exist
            List<EventEntity> eventEntities = RTOApplication.getsInstance().getDatabase().rtoEventDao().getAllSavedVehicles();
            if (!eventEntities.isEmpty()) {
                setSavedData(eventEntities);
            } else {
                showResultNotFound();
            }

        }).start();
    }

    private void showResultNotFound() {
        new Handler(Looper.getMainLooper()).post(() -> toastMsg("No previous Search history found!"));
    }

    private void getVehicleDetails(String numberInfo) {
        if (apiService == null) {
            apiService = RetrofitApiClient.getClient().create(ApiInterface.class);
        }
        Call<String> call = apiService.getInfoFromNumber(getParams(numberInfo));
        new ApiConnection<String>(History.this).makeServiceConnection(call, mContext, BuildConfig.GET_VEHICLE_INFO);
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

    private void setSavedData(List<EventEntity> eventEntities) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                historyInfoArrayList.clear();
                for (int i = 0; i < eventEntities.size(); i++) {
                    EventEntity eventEntity = new EventEntity();
                    eventEntity.setVehicleNumber(eventEntities.get(i).getVehicleNumber());
                    eventEntity.setVehicleID(eventEntities.get(i).getVehicleID());
                    eventEntity.setOwnerName(eventEntities.get(i).getOwnerName());
                    historyInfoArrayList.add(eventEntity);
                }

                HistoryAdapter eventsAdapter = new HistoryAdapter(History.this, mContext, historyInfoArrayList);
                history.setAdapter(eventsAdapter);
                if (AdIDSingleton.INSTANCE.getAdMobStatus() && AdIDSingleton.INSTANCE.getAdmobNativeAdStatus())
                    addNativeExpressAds();
                eventsAdapter.notifyDataSetChanged();
            }
        });
    }

    private void addNativeExpressAds() {
        int mid = historyInfoArrayList.size() / 2;
        NativeExpressAdView adView = new NativeExpressAdView(mContext);
        historyInfoArrayList.add((mid), adView);
        setUpAndLoadNativeExpressAds(mid);
    }

    private void setUpAndLoadNativeExpressAds(int mid) {
        history.post(() -> {
            final NativeExpressAdView adView = (NativeExpressAdView) historyInfoArrayList.get(mid);
            adView.setAdSize(com.google.android.gms.ads.AdSize.MEDIUM_RECTANGLE);
            adView.setAdUnitId(AdIDSingleton.INSTANCE.getAdmobNativeBannerId());
            loadNativeExpressAd(mid);
        });
    }

    private void initUIView() {
        history = findViewById(R.id.history_lv);
        spin_kit = findViewById(R.id.spin_kit);
        bannerContainer = findViewById(R.id.banner_container_history);
        admobContainer = findViewById(R.id.admob_container_history);
        AppCompatImageView back_bt = findViewById(R.id.back_bt);
        back_bt.setOnClickListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        history.setLayoutManager(linearLayoutManager);
    }

    private void loadNativeExpressAd(final int index) {
        if (index >= historyInfoArrayList.size()) {
            return;
        }
        Object item = historyInfoArrayList.get(index);
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

    @Override
    public void onResponse(Object response, String type) {
        if (type.equalsIgnoreCase(BuildConfig.GET_VEHICLE_INFO)) {
            if (response != null) {
                String result = String.valueOf(response);
                if (!TextUtils.isEmpty(result)) {

                    SetResponseData setResponseData = new SetResponseData(numberVehicle);
                    setResponseData.setDataResponse(result);

                    spin_kit.setVisibility(View.INVISIBLE);

                    Intent intentNumberInfo = new Intent(mContext, NumberDetails.class);
                    intentNumberInfo.putExtra("numberInfo", numberVehicle);
                    intentNumberInfo.putExtra("isActionFromHistory", true);
                    startActivity(intentNumberInfo);
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

    public void toastMsg(String message) {
        makeText(mContext, message, LENGTH_SHORT).show();
    }

    @Override
    public void historyItem(EventEntity eventEntity) {
        if (eventEntity != null) {
            numberVehicle = eventEntity.getVehicleNumber();
            if (!TextUtils.isEmpty(numberVehicle)) {
                spin_kit.setVisibility(View.VISIBLE);
                getVehicleDetails(numberVehicle);
            } else
                toastMsg("Vehicle number not found!");
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.back_bt:
                startActivity(new Intent(mContext, MainActivity.class));
                break;
        }
    }
}