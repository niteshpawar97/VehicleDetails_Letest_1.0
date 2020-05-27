package com.niketit.vehicleinfo.controller.interfaces;

import com.niketit.vehicleinfo.BuildConfig;
import com.niketit.vehicleinfo.model.AdDetailInfo;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {

    @FormUrlEncoded
    @POST(BuildConfig.GET_VEHICLE_INFO)
    Call<String> getInfoFromNumber(@FieldMap Map<String, String> params);

    @GET(BuildConfig.GET_AD_DETAILS)
    Call<AdDetailInfo> getAdDetails();
}