package com.tensor.tensortest;

import android.app.Activity;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import com.tensor.tensortest.fragments.NewsListFragment;


public class MainActivity extends AppCompatActivity {

    private static Resources res;
    private Fragment currentFragment;
    private static Activity activity;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = this;

        DisplayMetrics displaymetrics = getResources().getDisplayMetrics();
        App.setWidthScreen(displaymetrics.widthPixels);
        App.setHeightScreen(displaymetrics.heightPixels);

        res = getResources();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolbarTitle(R.string.news);
        setSupportActionBar(toolbar);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        App.getDataSource().open();

        openNewsFragment();

    }

    /**
     * Открытие фрагмента со списком новостей
     */
    public void openNewsFragment() {
        Log.d(Settings.UI_TAG, "Open News List Fragment");
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        currentFragment = CurrentNewsFragment.newInstance(news);
        transaction.add(R.id.fragmentContainer, currentFragment);
        transaction.addToBackStack("Current news fragment");
        transaction.commit();
    }

    /**
     * Показ спинера загружки
     * @param visible - мод показа
     */
    public void setProgressBarVisibility(int visible) {
        progressBar.setVisibility(visible);
    }


    @Override
    public void onBackPressed() {
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
            if(getSupportFragmentManager().getBackStackEntryCount() == 0)
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Появилось соединение с интернетом
     */
    public void appearInternet() {
        if(currentFragment instanceof NewsListFragment) {
            ((NewsListFragment)currentFragment).update();
        }
    }

    /**
     * Устанавливает title на toolbar
     * @param stringId
     */
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

    public static Resources getRes() {
        return res;
    }

    public static Activity getActivity() {
        return activity;
    }
}
