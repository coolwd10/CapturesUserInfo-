package com.example.android.mvp.main.detail.mvp;


import android.os.AsyncTask;

import com.example.android.mvp.main.home.mvp.IHomeView;
import com.example.data.ProductDataSource;
import com.example.data.model.Product;
import com.example.domain.HomeUseCaseController;
import com.example.domain.ProfileViewUseCaseController;

import javax.inject.Inject;


public class DetailPresenter {

    private static String TAG = DetailPresenter.class.getSimpleName();
    private IDetailView mDetailView;
    private ProfileViewUseCaseController mProfileViewUseCaseController;
    @Inject
    public DetailPresenter(ProductDataSource productDataSource) {
        mProfileViewUseCaseController =  new ProfileViewUseCaseController(productDataSource);
    }

    public void attachScreen(IDetailView view) {
        this.mDetailView = view;
    }

    public void read(String language) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Product product = mProfileViewUseCaseController.readUserInfo(language);
                mDetailView.setUserInfo(product);
                return null;
            }
        }.execute();
    }

}
