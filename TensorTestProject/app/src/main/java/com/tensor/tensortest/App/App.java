package com.tensor.tensortest.app;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.tensor.tensortest.beans.News;
import com.tensor.tensortest.data.NewsDbHelper;
import com.tensor.tensortest.network.NetworkUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by develop on 24.03.2017.
 */

public class App extends Application {


    /**
     * Список новостей
     */
    private static List<News> news = new ArrayList<>();

    private static NewsDbHelper dbHelper;


    private static float widthScreen;
    private static float heightScreen;

    /**
     * Есть интернет или нет
     */
    private static boolean networkStatus;

    @Override
    public void onCreate() {
        super.onCreate();
        dbHelper = new NewsDbHelper(getApplicationContext());
        networkStatus = (NetworkUtil.getConnectivityStatusString(getApplicationContext()) == NetworkUtil.TYPE_NOT_CONNECTED ? false : true);
    }

    public static boolean isNetworkStatus() {
        return networkStatus;
    }

    public static void setNetworkStatus(boolean networkStatus) {
        App.networkStatus = networkStatus;
    }

    public static NewsDbHelper getDbHelper() {
        return dbHelper;
    }

    public static List<News> getNews() {
        return news;
    }

    public static float getWidthScreen() {
        return widthScreen;
    }

    public static void setWidthScreen(float widthScreen) {
        App.widthScreen = widthScreen;
    }

    public static float getHeightScreen() {
        return heightScreen;
    }

    public static void setHeightScreen(float heightScreen) {
        App.heightScreen = heightScreen;
    }
}
