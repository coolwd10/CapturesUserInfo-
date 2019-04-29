package com.example.android.mvp.main.login.mvp;


import com.example.android.utils.Constants;

import javax.inject.Inject;


public class LoginPresenter  {

    private static String TAG = LoginPresenter.class.getSimpleName();
    private ILoginView mLoginView;

    @Inject
    public LoginPresenter() {

    }

    public void attachScreen(ILoginView view) {
//        super.attachScreen(view);
        this.mLoginView = view;
    }


    public void doLogin(String mobile) {
        mLoginView.showProgress();
        if(validNo(mobile)){
            mLoginView.sendVerificationCode(mobile);
        }else {
            mLoginView.hideProgress();
            mLoginView.showError(Constants.ERROR_TYPE_MOB_NUM);
        }
    }

    private boolean validNo(String no){
        if(no.isEmpty() || no.length() < 10){
            return false;
        }
        return true;
    }
}
