package com.example.android.mvp.main.landingPageActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.android.mvp.core.base.BaseActivity;
import com.example.android.mvp.main.blankViewActivity.BlankDummyActivity;
import com.example.android.mvp.main.home.HomeActivity;
import com.example.android.mvp.main.login.LoginActivity;
import com.example.android.utils.SharedPreferencesManager;
import com.example.multilanguage.MainApplication;
import com.example.multilanguage.R;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class LandingActivity extends BaseActivity {

    private static final String TAG = "LandingActivity";

    @Inject
    SharedPreferencesManager sharedPreferencesManager;

    @OnClick(R.id.btn_english)
    public void onSignUpButtonClicked() {
        loadLoginView("en");
    }

    @OnClick(R.id.btn_hindi)
    public void onRegistrationClicked() {
        loadLoginView("hi");
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDI();
        ButterKnife.bind(this);

        boolean isLangSelected = sharedPreferencesManager.getBoolean("languageSel", false);
        boolean isMobVerificationDone = sharedPreferencesManager.getBoolean("MobVerificationDone", false);
        if (isMobVerificationDone) {
            startHomeActivity();
        } else if (isLangSelected) {
            startLoginActivity();
        }



    }

    private void startLoginActivity() {
        Intent i = new Intent(LandingActivity.this,
                LoginActivity.class);
        startActivity(i);
        finish();
    }

    private void startHomeActivity() {
        Intent i = new Intent(LandingActivity.this,
                HomeActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public int layoutId() {
        return R.layout.landing_login;
    }

    private void initDI() {
        ((MainApplication) getApplicationContext()).getmAppComponent().inject(this);
    }

    private void loadLoginView(String langType) {
        setLanguage(langType);
        sharedPreferencesManager.putBoolean("languageSel", true);
        startActivity(new Intent(this, LoginActivity.class));

    }

}
