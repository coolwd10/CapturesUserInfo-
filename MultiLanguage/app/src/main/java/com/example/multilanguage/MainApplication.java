package com.example.multilanguage;

import android.app.Application;

import com.example.android.di.component.AppComponent;
import com.example.android.di.component.DaggerAppComponent;
import com.example.android.di.module.AppModule;
import com.example.android.di.module.RoomModule;

public class MainApplication extends Application {

    public AppComponent getmAppComponent() {
        return mAppComponent;
    }

    AppComponent mAppComponent;
    @Override
    public void onCreate() {
        super.onCreate();
        mAppComponent = DaggerAppComponent.builder().appModule(new AppModule(this))
                .roomModule(new RoomModule(this)).build();
    }



}
