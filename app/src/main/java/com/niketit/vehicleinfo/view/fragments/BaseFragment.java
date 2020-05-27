package com.niketit.vehicleinfo.view.fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;

import com.niketit.vehicleinfo.R;

import java.util.Objects;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class BaseFragment extends Fragment {

    private FragmentManager fragmentManager;
    private Context mContext;
    private Dialog dialog = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    public void addFragment(Fragment fragment) {
        fragmentManager = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fragmentManager).beginTransaction();
        ft.add(R.id.frame_container, fragment);
        ft.commitAllowingStateLoss();
    }

    public void replaceFragment(Fragment fragment) {
        String backStateName = fragment.getClass().getName();
        fragmentManager = getFragmentManager();
        FragmentTransaction ft = Objects.requireNonNull(fragmentManager).beginTransaction();
        ft.replace(R.id.frame_container, fragment, backStateName);
        ft.addToBackStack(backStateName);
        ft.commit();
    }

    private void clearBackStack() {
        while (fragmentManager.getBackStackEntryCount() != 0) {
            fragmentManager.popBackStackImmediate();
        }
    }

    //validate email
    public boolean validateEmail(String email) {
        boolean flagOfEmailValidation = true;
        int atPos = email.indexOf("@");
        int dotPos = email.lastIndexOf(".");
        if (atPos < 1 || dotPos < atPos + 2 || dotPos + 2 >= email.length()) {
            flagOfEmailValidation = false;
        }
        return flagOfEmailValidation;
    }

    /**
     * @return To Check Internet is Connected or Not.
     */
    public boolean isInternetAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = null;
        if (connectivityManager != null) {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    public void toastMsg(String message) {
        makeText(mContext, message, LENGTH_SHORT).show();
    }

    public void showLoader(Context context) {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            dialog = new Dialog(context, android.R.style.Theme_DeviceDefault_Light_NoActionBar_TranslucentDecor);
        } else {
            dialog = new Dialog(context);
        }
        if (dialog.getWindow() != null)
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_loader);
        dialog.setCancelable(false);
        dialog.show();
    }

    public void stopLoader() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}
