package com.example.domain;

import com.example.data.ProductDataSource;
import com.example.data.model.Product;

public class HomeUseCaseController {

    ProductDataSource mProductDataSource;

    public HomeUseCaseController(ProductDataSource productDataSource) {
        mProductDataSource = productDataSource;
    }

    public void saveProfileInfo(String langId, String name, String address,String imagePath) {
        Product product = new Product();
        product.setId(langId);
        product.setName(name);
        product.setAddress(address);
        product.setImagePath(imagePath);

        mProductDataSource.insert(product);
    }

    public void updateProfileInfo(String langId, String name, String address, String imagePath) {
        Product product = new Product();
        product.setId(langId);
        product.setName(name);
        product.setAddress(address);
        product.setImagePath(imagePath);
        mProductDataSource.updateTask(product);
    }

    public Product readUserInfo(String lang) {
        return mProductDataSource.readUserInfo(lang);
    }


}
