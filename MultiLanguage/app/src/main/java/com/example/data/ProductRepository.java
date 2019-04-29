package com.example.data;


import com.example.data.model.Product;

public interface ProductRepository {
    long insert(Product product);
    void updateTask(Product product);
     Product readUserInfo(String lang);
}
