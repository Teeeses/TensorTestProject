package com.tensor.tensortest;

import android.content.DialogInterface;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.tensor.tensortest.Utils.Settings;
import com.tensor.tensortest.fragments.GeneralFragment;
import com.tensor.tensortest.fragments.NewsFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Fragment currentFragment;

    private NavigationView navigationView;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        openGeneralFragment();
    }

    /**
     * Открытие генерального фрагмента
     */
    public void openGeneralFragment() {
        Log.d(Settings.UI_TAG, "Open General Fragment");
        setToolbarTitle(R.string.general);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        currentFragment = new GeneralFragment();
        transaction.replace(R.id.fragmentContainer, currentFragment);
        transaction.commit();
    }

    /**
     * Открытие новостного фрагмента
     */
    public void openNewsFragment() {
        Log.d(Settings.UI_TAG, "Open News Fragment");
        setToolbarTitle(R.string.news);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        currentFragment = new NewsFragment();
        transaction.replace(R.id.fragmentContainer, currentFragment);
        transaction.commit();
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            //Диалог подтверждения выхода
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Действительно выйти?");
            builder.setPositiveButton("ДА", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            });
            builder.setNegativeButton("НЕТ", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_general) {
            openGeneralFragment();
        } else if (id == R.id.nav_news) {
            openNewsFragment();
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setToolbarTitle(int stringId) {
        setTitle(getResources().getString(stringId));
    }
}
