package com.tensor.tensortest;

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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.tensor.tensortest.Utils.Settings;
import com.tensor.tensortest.app.App;
import com.tensor.tensortest.beans.News;
import com.tensor.tensortest.fragments.CurrentNewsFragment;
import com.tensor.tensortest.fragments.GeneralFragment;
import com.tensor.tensortest.fragments.NewsListFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Fragment currentFragment;

    private NavigationView navigationView;
    private DrawerLayout drawer;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DisplayMetrics displaymetrics = getResources().getDisplayMetrics();
        App.setWidthScreen(displaymetrics.widthPixels);
        App.setHeightScreen(displaymetrics.heightPixels);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

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
     * Открытие фрагмента со списком новостей
     */
    public void openNewsFragment() {
        Log.d(Settings.UI_TAG, "Open News List Fragment");
        setToolbarTitle(R.string.news);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        currentFragment = new NewsListFragment();
        transaction.replace(R.id.fragmentContainer, currentFragment);
        transaction.commit();
    }

    /**
     * Открытие фрагмента новсти на которую нажали в списке
     */
    public void openCurrentNewsFragment(News news) {
        Log.d(Settings.UI_TAG, "Open Current News Fragment");
        setToolbarTitle(R.string.news);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        currentFragment = CurrentNewsFragment.newInstance(news);
        transaction.add(R.id.fragmentContainer, currentFragment);
        transaction.addToBackStack("Current news fragment");
        transaction.commit();
    }

    public void setProgressBarVisibility(int visible) {
        progressBar.setVisibility(visible);
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
            builder.setPositiveButton("ДА", (dialog, which) -> {
                    dialog.dismiss();
                    finish();
            });
            builder.setNegativeButton("НЕТ", (dialog, which) -> dialog.dismiss());
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

    @Override
    protected void onResume() {
        super.onResume();
        App.getDataSource().open();
    }

    @Override
    protected void onPause() {
        super.onPause();
        App.getDataSource().close();
    }
}
