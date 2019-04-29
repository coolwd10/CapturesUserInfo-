package com.example.android.di.module;


//import androidx.room.Room;


import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;


import com.example.android.utils.SharedPreferencesManager;
import com.example.data.ProductDao;
import com.example.data.ProductDataSource;
import com.example.data.ProductDatabase;
import com.example.data.ProductRepository;

import javax.inject.Singleton;

import androidx.room.Room;
import dagger.Module;
import dagger.Provides;

@Module
public class RoomModule {
    private static final String SHARED_PREF_FILENAME = "PROFILE_PREFS";

    private ProductDatabase demoDatabase;

    public RoomModule(Application mApplication) {
        demoDatabase = Room.databaseBuilder(mApplication, ProductDatabase.class, "demo-db").build();
    }

    @Singleton
    @Provides
    ProductDatabase providesRoomDatabase() {
        return demoDatabase;
    }

    @Singleton
    @Provides
    ProductDao providesProductDao(ProductDatabase demoDatabase) {
        return demoDatabase.getProductDao();
    }

    @Singleton
    @Provides
    ProductRepository productRepository(ProductDao productDao) {
        return new ProductDataSource(productDao);
    }

    @Provides
    @Singleton
    SharedPreferences provideSharedPrefs(Application application) {
        return application.getSharedPreferences(SHARED_PREF_FILENAME, Context.MODE_PRIVATE);
    }

    @Provides @Singleton
    SharedPreferencesManager provideSharedPreferencesManager(SharedPreferences sharedPreferences) {
        return new SharedPreferencesManager(sharedPreferences);
    }
}
