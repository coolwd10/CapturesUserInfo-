package com.example.android.mvp.main.detail.mvp;

import com.example.android.mvp.core.base.BaseScreen;
import com.example.data.model.Product;

public interface IDetailView extends BaseScreen {
    void setUserInfo(Product product);
}
