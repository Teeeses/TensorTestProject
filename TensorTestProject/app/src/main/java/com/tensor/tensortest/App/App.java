package com.tensor.tensortest.app;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.tensor.tensortest.beans.News;
import com.tensor.tensortest.data.DataSource;
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

    private static DataSource dataSource;


    private static float widthScreen;
    private static float heightScreen;

    /**
     * Есть интернет или нет
     */
    private static boolean networkStatus;

    /**
     * Текущая страница откуда загружаем новости
     */
    private static int currentPage = 1;

    @Override
    public void onCreate() {
        super.onCreate();
        dataSource = new DataSource(getApplicationContext());
        networkStatus = (NetworkUtil.getConnectivityStatusString(getApplicationContext()) == NetworkUtil.TYPE_NOT_CONNECTED ? false : true);
    }

    public static boolean isNetworkStatus() {
        return networkStatus;
    }

    public static void setNetworkStatus(boolean networkStatus) {
        App.networkStatus = networkStatus;
    }

    public static DataSource getDataSource() {
        return dataSource;
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

    public static void setHeightScreen(float heightScreen) {
        App.heightScreen = heightScreen;
    }

    public static int getCurrentPage() {
        return currentPage;
    }

    public static void addCurrentPage() {
        App.currentPage++;
    }

    /**
     * Проверка, есть такая новость в списке или нет
     * @param name - уникальное имя
     * @return - есть(true) или нет(false)
     */
    public static boolean checkIsNewsInList(List<News> list, String name) {
        for(News current: list) {
            if(current.getName().equals(name)) {
                return false;
            }
        }
        return true;
    }
}
