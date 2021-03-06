package com.generonumero.blocodaguarda.menu.view.impl;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.generonumero.blocodaguarda.BDGApplication;
import com.generonumero.blocodaguarda.R;
import com.generonumero.blocodaguarda.about.view.AboutFragment;
import com.generonumero.blocodaguarda.alert.view.impl.AlertFragment;
import com.generonumero.blocodaguarda.configuration.view.impl.ConfigurationFragment;
import com.generonumero.blocodaguarda.login.view.impl.LoginActivity;
import com.generonumero.blocodaguarda.menu.presenter.MainPresenter;
import com.generonumero.blocodaguarda.menu.view.MainView;
import com.generonumero.blocodaguarda.network.view.impl.NetworkFragment;
import com.generonumero.blocodaguarda.webview.WebViewActivity;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainView {


    public final int MENU_MAIN = R.id.action_menu_main;
    public final int MENU_ABOUT = R.id.action_menu_about;
    public final int MENU_GN = R.id.action_menu_gn;
    public final int MENU_CONFIGURATION = R.id.action_menu_configuration;
    public final int MENU_NETWORK = R.id.action_menu_network;

    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @Bind(R.id.navigation)
    NavigationView navigationView;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    private MainPresenter mainPresenter;

    private Map<Integer, Fragment> fragments;


    public static void start(Activity activity) {
        Intent i = new Intent(activity.getApplicationContext(), MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        verifyDeepLink();

        mainPresenter = BDGApplication.getInstance().getMainPresenter(this);
        mainPresenter.initView();
    }

    private void verifyDeepLink() {
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.getExtras() != null) {
                Bundle extras = intent.getExtras();
                for (String key : extras.keySet()) {
                    String string = extras.getString(key);
                    if (string != null && string.contains("http")) {
                        WebViewActivity.start(this, extras.getString(key));
                        FirebaseMessaging.getInstance().unsubscribeFromTopic("push");
                    }
                }
            }
        }
    }

    @Override
    public void goToLoginView() {
        LoginActivity.start(this);
        finish();
    }

    @Override
    public void loadViews() {
        setupDrawerContent(navigationView);
        setFirstItemNavigationView();
    }

    @Override
    public void showFirstOpenAppDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.bdg_menu_firstopen_dialog_title));
        builder.setMessage(getString(R.string.bdg_menu_firstopen_dialog_text));
        builder.setPositiveButton(getString(R.string.bdg_menu_firstopen_dialog_positive), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                mainPresenter.clickDialogAddContacts();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(getString(R.string.bdg_menu_firstopen_dialog_negative), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                mainPresenter.clickDialogNotAddContacts();
                dialog.dismiss();
            }
        });

        final AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.bdg_separator));
            }
        });
        alertDialog.show();
    }

    public void goToNetworkView() {
        changeFragmentWithoutBackStack(getFragment(MENU_NETWORK));
    }

    public void goToHome() {
        changeFragmentWithoutBackStack(getFragment(MENU_MAIN));
    }


    private void setFirstItemNavigationView() {
        navigationView.setCheckedItem(MENU_MAIN);
        navigationView.getMenu().performIdentifierAction(MENU_MAIN, 0);
    }

    private void setupDrawerContent(NavigationView navigationView) {

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setIcon(R.drawable.logo_small);
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                drawerLayout.closeDrawers();
                onNavDrawerItemSelected(item);
                return false;
            }
        });

        // Initializing Drawer Layout and ActionBarToggle
        ActionBarDrawerToggle actionBarDrawerToggle = getActionBarDrawerToggle(toolbar);
        //Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    private ActionBarDrawerToggle getActionBarDrawerToggle(Toolbar toolbar) {
        return new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
    }

    private void onNavDrawerItemSelected(MenuItem menuItem) {
        Fragment fragment = getFragment(menuItem.getItemId());
        changeFragmentWithoutBackStack(fragment);
    }


    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            finish();
        } else {
            super.onBackPressed();
        }

    }

    private Fragment getFragment(int id) {
        switch (id) {
            case MENU_MAIN:
                if (getFragments().get(MENU_MAIN) == null) {
                    getFragments().put(MENU_MAIN, new AlertFragment());
                }
                toolbar.setTitle("  Braços Dados");
                break;
            case MENU_ABOUT:
                if (getFragments().get(MENU_ABOUT) == null) {
                    getFragments().put(MENU_ABOUT, AboutFragment.getInstanceFromAbout());
                }
                toolbar.setTitle("  Sobre o app");
                break;
            case MENU_GN:
                if (getFragments().get(MENU_GN) == null) {
                    getFragments().put(MENU_GN, AboutFragment.getInstanceFromGN());
                }
                toolbar.setTitle("  Gênero e Número");
                break;
            case MENU_CONFIGURATION:
                if (getFragments().get(MENU_CONFIGURATION) == null) {
                    getFragments().put(MENU_CONFIGURATION, new ConfigurationFragment());
                }
                toolbar.setTitle("  Configurações");
                break;
            case MENU_NETWORK:
                getFragments().put(MENU_NETWORK, new NetworkFragment());
                toolbar.setTitle("  Rede de Confiança");
                break;
        }
        return getFragments().get(id);
    }

    private void changeFragmentWithoutBackStack(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
    }

    private Map<Integer, Fragment> getFragments() {
        if (fragments == null) {
            fragments = new HashMap<>();
        }
        return fragments;
    }
}
