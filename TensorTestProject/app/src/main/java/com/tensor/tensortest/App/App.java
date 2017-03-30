package com.tensor.tensortest.App;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.tensor.tensortest.beans.News;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by develop on 24.03.2017.
 */

public class App extends Application {


    private static Context context;
    /**
     * Список новостей
     */
    private static List<News> news = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static List<News> getNews() {
        return news;
    }

    public static Context getContext() {
        return context;
    }
}
