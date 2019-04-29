package com.example.android.di.component;


import com.example.android.di.module.AppModule;
import com.example.android.di.module.RoomModule;
import com.example.android.mvp.main.detail.mvp.DetailActivity;
import com.example.android.mvp.main.home.HomeActivity;
import com.example.android.mvp.main.home.mvp.HomePresenter;
import com.example.android.mvp.main.landingPageActivity.LandingActivity;
import com.example.android.mvp.main.login.LoginActivity;
import com.example.android.mvp.main.login.mvp.LoginPresenter;
import com.example.android.utils.SharedPreferencesManager;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, RoomModule.class})
public interface AppComponent {
    SharedPreferencesManager getSharedPreferencesManager();
    void inject(LandingActivity landingActivity);

    void inject(LoginActivity loginActivity);
    void inject(LoginPresenter registrationPresenter);
    void inject(HomeActivity homeActivity);
    void inject(HomePresenter homePresenter);

    void inject(DetailActivity profileView);


}
