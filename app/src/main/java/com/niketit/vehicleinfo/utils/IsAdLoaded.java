package com.niketit.vehicleinfo.utils;

public enum IsAdLoaded {
    INSTANCE;
    private Boolean randomNumberSearch=false;

    private Boolean randomNumberHistory=false;

    public Boolean getRandomNumberSearch() {
        return randomNumberSearch;
    }

    public void setRandomNumberSearch(Boolean randomNumberSearch) {
        this.randomNumberSearch = randomNumberSearch;
    }

    public Boolean getRandomNumberHistory() {
        return randomNumberHistory;
    }

    public void setRandomNumberHistory(Boolean randomNumberHistory) {
        this.randomNumberHistory = randomNumberHistory;
    }
}