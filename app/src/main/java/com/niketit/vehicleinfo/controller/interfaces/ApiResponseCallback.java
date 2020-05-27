package com.niketit.vehicleinfo.controller.interfaces;

public interface ApiResponseCallback {

    void onResponse(Object response, String type);

    void onErrorMessage(Object errorMessage);

    void showNetworkToast(String message);
}
