package com.niketit.vehicleinfo.controller.apiconnection;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.niketit.vehicleinfo.controller.interfaces.ApiResponseCallback;
import com.niketit.vehicleinfo.view.activity.BaseActivity;

import java.net.ConnectException;

import androidx.annotation.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * ApiConnection to make request for API
 *
 * @param <T>
 */
public class ApiConnection<T> {
    private ApiResponseCallback apiResponseCallback;
    private Context mContext;

    public ApiConnection(ApiResponseCallback apiResponseCallback) {
        this.apiResponseCallback = apiResponseCallback;
    }

    public void makeServiceConnection(Call<T> call, Context context, final String apiName) {
        mContext = context;
        if (isInternetAvailable()) {
            BaseActivity.showLoader(context);
            final Callback<T> callback = new Callback<T>() {
                @Override
                public void onResponse(@NonNull Call<T> call, Response<T> response) {
                    if (response.isSuccessful()) {
                        Object responseObject = response.body();
                        BaseActivity.stopLoader();
                        apiResponseCallback.onResponse(responseObject, apiName);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<T> call, Throwable t) {
                    BaseActivity.stopLoader();
                    t.printStackTrace();
                    if (t instanceof ConnectException) {
                        String message = "Server is not responding,Please try again!";
                        apiResponseCallback.onErrorMessage(message);
                    }
                }
            };
            call.enqueue(callback);
        } else {
            apiResponseCallback.showNetworkToast("No Internet Connection");
        }
    }

    /**
     * @return To Check Internet is Connected or Not.
     */
    private boolean isInternetAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = null;
        if (connectivityManager != null) {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}