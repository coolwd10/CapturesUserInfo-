package com.example.android.di.module;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private final Application mContext;

    public AppModule(Application context) {
        mContext = context;
    }

    @Singleton
    @Provides
    public Application provideContext() {
        return mContext;
    }
}