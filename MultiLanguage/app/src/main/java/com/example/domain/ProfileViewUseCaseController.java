package com.example.domain;

import com.example.data.ProductDataSource;
import com.example.data.model.Product;

public class ProfileViewUseCaseController {

    ProductDataSource mProductDataSource;

    public ProfileViewUseCaseController(ProductDataSource productDataSource) {
        mProductDataSource = productDataSource;
    }
    public Product readUserInfo(String lang) {
        return mProductDataSource.readUserInfo(lang);
    }

}
