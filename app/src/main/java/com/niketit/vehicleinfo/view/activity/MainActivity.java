package com.niketit.vehicleinfo.view.activity;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.niketit.vehicleinfo.BuildConfig;
import com.niketit.vehicleinfo.R;
import com.niketit.vehicleinfo.utils.AdIDSingleton;
import com.niketit.vehicleinfo.view.fragments.HomeScreenFragment;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUIView();
        addHomeScreenFragment();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Integer updatedVersion = AdIDSingleton.INSTANCE.getVersionCode();
        Integer appVersion = BuildConfig.VERSION_CODE;
        if (!appVersion.equals(updatedVersion)) {
            String updateLink = AdIDSingleton.INSTANCE.getAppLink();
            getAppUpdateAlert(updateLink);
        }
    }

    private void getAppUpdateAlert(String updateAppLink) {
        Dialog appUpdateDialog = new Dialog(MainActivity.this);
        if (appUpdateDialog.getWindow() != null)
            appUpdateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        appUpdateDialog.setContentView(R.layout.update_app_alert);
        appUpdateDialog.getWindow().getAttributes().windowAnimations = R.style.DialogFadeAnim;
        appUpdateDialog.setCancelable(false);
        AppCompatTextView updateNoticeTv = appUpdateDialog.findViewById(R.id.tv_update_notice);
        AppCompatButton appUpdateBt = appUpdateDialog.findViewById(R.id.bt_app_update);
        updateNoticeTv.setText(R.string.app_req_update);
        appUpdateBt.setOnClickListener(view -> {
            appUpdateDialog.dismiss();
            if (TextUtils.isEmpty(updateAppLink)) {
                startActivity(new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id="
                                + MainActivity.this.getPackageName())));
            } else {
                startActivity(new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(updateAppLink)));
            }
        });
        appUpdateDialog.show();
    }

    private void addHomeScreenFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        HomeScreenFragment homeScreenFragment = new HomeScreenFragment();
        fragmentTransaction.add(R.id.frame_container, homeScreenFragment, "homeScreen");
        fragmentTransaction.commit();
    }

    private void initUIView() {
        drawer = findViewById(R.id.drawer_layout);
        LinearLayout feedback = findViewById(R.id.feedback_ly);
        LinearLayout rateApp = findViewById(R.id.rate_app_ly);
        LinearLayout shareApp = findViewById(R.id.share_app_ly);
        LinearLayout followUs = findViewById(R.id.follow_us_ly);
        LinearLayout privacyPolicy = findViewById(R.id.privacy_policy_ly);

        feedback.setOnClickListener(this);
        rateApp.setOnClickListener(this);
        shareApp.setOnClickListener(this);
        followUs.setOnClickListener(this);
        privacyPolicy.setOnClickListener(this);

        AppCompatImageView menuIcon = findViewById(R.id.nav_menu_icon);
        menuIcon.setOnClickListener(this);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawer, null, 0, 0) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
            }
        };
        drawer.addDrawerListener(drawerToggle);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.nav_menu_icon:
                drawer.openDrawer(GravityCompat.START);
                break;
            case R.id.feedback_ly:
                drawer.closeDrawers();
                getOpenMail();
                break;
            case R.id.rate_app_ly:
                drawer.closeDrawers();
                getRateApp();
                break;
            case R.id.share_app_ly:
                drawer.closeDrawers();
                getShareURL();
                break;
            case R.id.follow_us_ly:
                drawer.closeDrawers();
                getFollowUs();
                break;
            case R.id.privacy_policy_ly:
                drawer.closeDrawers();
                getPrivacyPolicy();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        exitFromApp();
    }

    private void getPrivacyPolicy() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.privacy_policy_url)));
        startActivity(browserIntent);
    }

    private void getFollowUs() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.follow_us_url)));
        startActivity(browserIntent);
    }

    private void getShareURL() {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        share.putExtra(Intent.EXTRA_SUBJECT, "RTO Vehicle Info");

        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("For any Indian Car,Bike or Vehicle Owner,s detail and above,this licence is a plate number download" +
                "the vehicle information app below:"+"\n\n");
        stringBuilder.append("https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);

        share.putExtra(Intent.EXTRA_TEXT, stringBuilder.toString());
        startActivity(Intent.createChooser(share, getString(R.string.share_information)));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void getRateApp() {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent marketIntent = new Intent(Intent.ACTION_VIEW, uri);
        marketIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(marketIntent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
        }
    }

    private void getOpenMail() {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("plain/text");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"niketitservices@yhaoo.com"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback Vehicle Info");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "");
        startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    }
}