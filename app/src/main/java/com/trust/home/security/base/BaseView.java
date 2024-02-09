package com.trust.home.security.base;

public interface BaseView {
    void showLoading();
    void hideLoading();
    void showMessage(String message);
    void showMessage(Exception exception);
    void showToast(String message);
    void onBackPress();
}
