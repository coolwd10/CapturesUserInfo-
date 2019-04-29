package com.example.android.mvp.main.login.mvp;

import com.example.android.mvp.core.base.BaseScreen;

public interface ILoginView extends BaseScreen {

    void sendVerificationCode(String no);

}
