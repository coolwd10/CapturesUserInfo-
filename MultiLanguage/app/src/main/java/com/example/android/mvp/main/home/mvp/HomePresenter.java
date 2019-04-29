package com.example.android.mvp.main.home.mvp;


import android.os.AsyncTask;

import com.example.data.ProductDataSource;
import com.example.data.model.Product;
import com.example.domain.HomeUseCaseController;

import javax.inject.Inject;


public class HomePresenter  {

    private static String TAG = HomePresenter.class.getSimpleName();
    private IHomeView mHomeView;
    private HomeUseCaseController mHomeUseCaseController;
    @Inject
    public HomePresenter(ProductDataSource productDataSource) {
        mHomeUseCaseController =  new HomeUseCaseController(productDataSource);
    }

    public void attachScreen(IHomeView view) {
        this.mHomeView = view;
    }

    public void create(String language, String name, String address, String ImagePath) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                mHomeUseCaseController.saveProfileInfo(language, name, address,ImagePath);
                mHomeView.showToastMessage();
                return null;
            }
        }.execute();
    }

    public void update(String language, String name, String address, String ImagePath) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                mHomeUseCaseController.updateProfileInfo(language, name, address,ImagePath);
                mHomeView.showToastMessage();
                return null;
            }
        }.execute();
    }

    public void read(String language) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Product product = mHomeUseCaseController.readUserInfo(language);
                mHomeView.setUserInfo(product);
                return null;
            }
        }.execute();
    }

}
