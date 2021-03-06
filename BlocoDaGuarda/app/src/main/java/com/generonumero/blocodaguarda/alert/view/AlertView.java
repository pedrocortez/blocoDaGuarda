package com.generonumero.blocodaguarda.alert.view;

import com.generonumero.blocodaguarda.permission.view.PermissionView;

public interface AlertView extends PermissionView {

    void showNetworkButton();

    void hideNetworkButton();

    void goToNetworkScreen();

    void showNetworkPopup();

    void showSafeScreen(int time);

    void dismissSafeScreen();

    void disclaimerSMS();

    void showAlertLocationPermissionDisable();

    void showAlertLocationPermissionDenied();
}
