package com.tensor.tensortest;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.tensor.tensortest.Utils.Settings;
import com.tensor.tensortest.fragments.NewsFragment;

public class MainActivity extends AppCompatActivity {

    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        openNewsFragment();
    }

    /**
     * Открытие новостного фрагмента
     */
    public void openNewsFragment() {
        Log.d(Settings.UI_TAG, "Open News Fragment");
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        currentFragment = new NewsFragment();
        transaction.replace(R.id.fragmentContainer, currentFragment);
        transaction.commit();
    }
}
