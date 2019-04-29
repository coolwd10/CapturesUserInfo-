package com.example.data;


import com.example.data.model.Product;

import javax.inject.Inject;

public class ProductDataSource implements ProductRepository{

    private ProductDao productDao;

    @Inject
    public ProductDataSource(ProductDao productDao) {
        this.productDao = productDao;
    }

    @Override
    public long insert(Product product) {
        return productDao.insert(product);
    }

    @Override
    public void updateTask(Product product) {
        productDao.update(product);
    }

    @Override
    public Product readUserInfo(String lang) {
        return productDao.findUserInfo(lang);
    }


}
