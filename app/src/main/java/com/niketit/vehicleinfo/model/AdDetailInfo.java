package com.niketit.vehicleinfo.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AdDetailInfo {

    @SerializedName("ads_info")
    @Expose
    private List<AdsInfo> adsInfo = null;

    public List<AdsInfo> getAdsInfo() {
        return adsInfo;
    }

    public void setAdsInfo(List<AdsInfo> adsInfo) {
        this.adsInfo = adsInfo;
    }

    public class AdsInfo {
        @SerializedName("app_versionCode")
        @Expose
        private Integer appVersionCode;
        @SerializedName("app_link")
        @Expose
        private String appLink;
        @SerializedName("admob_ad_status")
        @Expose
        private Boolean admobAdStatus;
        @SerializedName("facebook_ad_status")
        @Expose
        private Boolean facebookAdStatus;
        @SerializedName("admob_banner_ad_status")
        @Expose
        private Boolean admobBannerAdStatus;
        @SerializedName("admob_interstitial_ad_status")
        @Expose
        private Boolean admobInterstitialAdStatus;
        @SerializedName("admob_native_ad_status")
        @Expose
        private Boolean admobNativeAdStatus;
        @SerializedName("facebook_banner_ad_status")
        @Expose
        private Boolean facebookBannerAdStatus;
        @SerializedName("facebook_interstitial_ad_status")
        @Expose
        private Boolean facebookInterstitialAdStatus;
        @SerializedName("facebook_native_ad_status")
        @Expose
        private Boolean facebookNativeAdStatus;
        @SerializedName("facebook_native_banner_ad_status")
        @Expose
        private Boolean facebookNativeBannerAdStatus;
        @SerializedName("admob_app_id")
        @Expose
        private String admobAppId;
        @SerializedName("admob_banner_id")
        @Expose
        private String admobBannerId;
        @SerializedName("admob_interstitial_id")
        @Expose
        private String admobInterstitialId;
        @SerializedName("admob_native_banner_id")
        @Expose
        private String admobNativeBannerId;
        @SerializedName("facebook_banner_id")
        @Expose
        private String facebookBannerId;
        @SerializedName("facebook_interstitial_id")
        @Expose
        private String facebookInterstitialId;
        @SerializedName("facebook_native_id")
        @Expose
        private String facebookNativeId;
        @SerializedName("facebook_native_banner_id")
        @Expose
        private String facebookNativeBannerId;

        public Integer getAppVersionCode() {
            return appVersionCode;
        }

        public void setAppVersionCode(Integer appVersionCode) {
            this.appVersionCode = appVersionCode;
        }

        public String getAppLink() {
            return appLink;
        }

        public void setAppLink(String appLink) {
            this.appLink = appLink;
        }

        public Boolean getAdmobAdStatus() {
            return admobAdStatus;
        }

        public void setAdmobAdStatus(Boolean admobAdStatus) {
            this.admobAdStatus = admobAdStatus;
        }

        public Boolean getFacebookAdStatus() {
            return facebookAdStatus;
        }

        public void setFacebookAdStatus(Boolean facebookAdStatus) {
            this.facebookAdStatus = facebookAdStatus;
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
    }
}