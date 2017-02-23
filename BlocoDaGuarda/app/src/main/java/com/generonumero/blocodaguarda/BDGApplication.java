package com.generonumero.blocodaguarda;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.generonumero.blocodaguarda.analytics.BDGTracking;
import com.generonumero.blocodaguarda.login.presenter.LoginPresenter;
import com.generonumero.blocodaguarda.login.presenter.impl.LoginPresenterImpl;
import com.generonumero.blocodaguarda.login.service.FacebookLoginService;
import com.generonumero.blocodaguarda.login.view.LoginView;
import com.generonumero.blocodaguarda.menu.presenter.MainPresenter;
import com.generonumero.blocodaguarda.menu.presenter.impl.MainPresenterImpl;
import com.generonumero.blocodaguarda.menu.view.MainView;
import com.generonumero.blocodaguarda.network.presenter.NetworkPresenter;
import com.generonumero.blocodaguarda.network.presenter.impl.NetworkPresenterImpl;
import com.generonumero.blocodaguarda.network.repository.NetworkRepository;
import com.generonumero.blocodaguarda.network.repository.impl.NetworkRepositoryImpl;
import com.generonumero.blocodaguarda.network.view.NetworkView;
import com.generonumero.blocodaguarda.permission.PermissionService;
import com.generonumero.blocodaguarda.permission.impl.PermissionServiceImpl;
import com.squareup.otto.Bus;

public class BDGApplication extends Application {

    private static BDGApplication instance;

    private FacebookLoginService facebookLoginService;

    private Bus bus;

    private NetworkRepository networkRepository;

    private PermissionService permissionService;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        BDGTracking.initialize(getApplicationContext());

        FacebookSdk.sdkInitialize(getApplicationContext());

    }

    public static BDGApplication getInstance() {
        return instance;
    }

    public MainPresenter getMainPresenter(MainView mainView) {
        return new MainPresenterImpl(mainView);
    }

    public FacebookLoginService getFacebookLoginService() {
        if (facebookLoginService == null) {
            facebookLoginService = new FacebookLoginService(getApplicationContext(), getBus());
        }
        return facebookLoginService;
    }

    public LoginPresenter getLoginPresenter(LoginView loginView) {
        return new LoginPresenterImpl(loginView, getFacebookLoginService(), getBus());
    }

    public NetworkPresenter getNetworkPresenter(NetworkView networkView) {
        return new NetworkPresenterImpl(networkView, getNetworkRepository(), getPermissionService());
    }

    public NetworkRepository getNetworkRepository() {
        if (networkRepository == null) {
            networkRepository = new NetworkRepositoryImpl(getApplicationContext());
        }
        return networkRepository;
    }


    public Bus getBus() {
        if (bus == null) {
            bus = new Bus();
        }
        return bus;
    }

    public PermissionService getPermissionService() {
        if (permissionService == null) {
            permissionService = new PermissionServiceImpl();
        }
        return permissionService;
    }
}
