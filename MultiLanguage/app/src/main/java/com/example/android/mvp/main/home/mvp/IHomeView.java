package com.example.android.mvp.main.home.mvp;

import com.example.android.mvp.core.base.BaseScreen;
import com.example.data.model.Product;

public interface IHomeView extends BaseScreen {

    void sendVerificationCode(String no);

    void setUserInfo(Product product);

    void showToastMessage();
}
