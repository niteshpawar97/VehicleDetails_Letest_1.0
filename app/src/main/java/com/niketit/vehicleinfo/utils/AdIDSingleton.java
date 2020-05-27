package com.niketit.vehicleinfo.utils;

public enum AdIDSingleton {
    INSTANCE;
    private Integer versionCode;
    private String appLink;

    //status of FB and gms ads
    private Boolean adMobStatus;
    private Boolean fbAdStatus;
    private Boolean admobBannerAdStatus;
    private Boolean admobInterstitialAdStatus;
    private Boolean admobNativeAdStatus;
    private Boolean facebookBannerAdStatus;
    private Boolean facebookInterstitialAdStatus;
    private Boolean facebookNativeAdStatus;
    private Boolean facebookNativeBannerAdStatus;

    private String admobAppId;
    private String admobBannerId;
    private String admobInterstitialId;
    private String admobNativeBannerId;
    private String facebookBannerId;
    private String facebookInterstitialId;
    private String facebookNativeId;
    private String facebookNativeBannerId;
    private Boolean isDetailShown = false;

    private String setVehicleNumber;

    public Integer getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(Integer versionCode) {
        this.versionCode = versionCode;
    }

    public String getAppLink() {
        return appLink;
    }

    public void setAppLink(String appLink) {
        this.appLink = appLink;
    }

    public Boolean getAdMobStatus() {
        return adMobStatus;
    }

    public void setAdMobStatus(Boolean adMobStatus) {
        this.adMobStatus = adMobStatus;
    }

    public Boolean getFbAdStatus() {
        return fbAdStatus;
    }

    public void setFbAdStatus(Boolean fbAdStatus) {
        this.fbAdStatus = fbAdStatus;
    }

    public String getAdmobAppId() {
        return admobAppId;
    }

    public void setAdmobAppId(String admobAppId) {
        this.admobAppId = admobAppId;
    }

    public String getAdmobBannerId() {
        return admobBannerId;
    }

    public void setAdmobBannerId(String admobBannerId) {
        this.admobBannerId = admobBannerId;
    }

    public String getAdmobInterstitialId() {
        return admobInterstitialId;
    }

    public void setAdmobInterstitialId(String admobInterstitialId) {
        this.admobInterstitialId = admobInterstitialId;
    }

    public String getAdmobNativeBannerId() {
        return admobNativeBannerId;
    }

    public void setAdmobNativeBannerId(String admobNativeBannerId) {
        this.admobNativeBannerId = admobNativeBannerId;
    }

    public String getFacebookBannerId() {
        return facebookBannerId;
    }

    public void setFacebookBannerId(String facebookBannerId) {
        this.facebookBannerId = facebookBannerId;
    }

    public String getFacebookInterstitialId() {
        return facebookInterstitialId;
    }

    public void setFacebookInterstitialId(String facebookInterstitialId) {
        this.facebookInterstitialId = facebookInterstitialId;
    }

    public String getFacebookNativeId() {
        return facebookNativeId;
    }

    public void setFacebookNativeId(String facebookNativeId) {
        this.facebookNativeId = facebookNativeId;
    }

    public String getFacebookNativeBannerId() {
        return facebookNativeBannerId;
    }

    public void setFacebookNativeBannerId(String facebookNativeBannerId) {
        this.facebookNativeBannerId = facebookNativeBannerId;
    }

    public void setDetailShown(Boolean detailShown) {
        isDetailShown = detailShown;
    }

    public Boolean getDetailShown() {
        return isDetailShown;
    }

    public Boolean getAdmobBannerAdStatus() {
        return admobBannerAdStatus;
    }

    public void setAdmobBannerAdStatus(Boolean admobBannerAdStatus) {
        this.admobBannerAdStatus = admobBannerAdStatus;
    }

    public Boolean getAdmobInterstitialAdStatus() {
        return admobInterstitialAdStatus;
    }

    public void setAdmobInterstitialAdStatus(Boolean admobInterstitialAdStatus) {
        this.admobInterstitialAdStatus = admobInterstitialAdStatus;
    }

    public Boolean getAdmobNativeAdStatus() {
        return admobNativeAdStatus;
    }

    public void setAdmobNativeAdStatus(Boolean admobNativeAdStatus) {
        this.admobNativeAdStatus = admobNativeAdStatus;
    }

    public Boolean getFacebookBannerAdStatus() {
        return facebookBannerAdStatus;
    }

    public void setFacebookBannerAdStatus(Boolean facebookBannerAdStatus) {
        this.facebookBannerAdStatus = facebookBannerAdStatus;
    }

    public Boolean getFacebookInterstitialAdStatus() {
        return facebookInterstitialAdStatus;
    }

    public void setFacebookInterstitialAdStatus(Boolean facebookInterstitialAdStatus) {
        this.facebookInterstitialAdStatus = facebookInterstitialAdStatus;
    }

    public Boolean getFacebookNativeAdStatus() {
        return facebookNativeAdStatus;
    }

    public void setFacebookNativeAdStatus(Boolean facebookNativeAdStatus) {
        this.facebookNativeAdStatus = facebookNativeAdStatus;
    }

    public Boolean getFacebookNativeBannerAdStatus() {
        return facebookNativeBannerAdStatus;
    }

    public void setFacebookNativeBannerAdStatus(Boolean facebookNativeBannerAdStatus) {
        this.facebookNativeBannerAdStatus = facebookNativeBannerAdStatus;
    }

    public String getVehicleNumber() {
        return setVehicleNumber;
    }

    public void setVehicleNumber(String setVehicleNumber) {
        this.setVehicleNumber = setVehicleNumber;
    }
}