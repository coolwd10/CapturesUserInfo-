package com.example.data;

import com.example.data.model.Product;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public abstract class ProductDao {
    private static final String TAG = ProductDao.class.getSimpleName();
    private static final String selectAllProducts = "SELECT * FROM Product";

    @Insert
    abstract long insert(Product product);

    @Update
    abstract void update(Product product);

    @Query("SELECT * FROM Product WHERE id >= :id")
    abstract Product findUserInfo(String id);

    @Delete
    public abstract int delete(Product product);

//    @Query("DELETE FROM Product")
//    abstract void deleteAllProducts();
//
//    @Transaction
//    public void clearDb() {
//        deleteAllProducts();
//    }
}
