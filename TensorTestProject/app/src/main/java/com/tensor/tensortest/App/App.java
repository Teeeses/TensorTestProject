package com.tensor.tensortest.App;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by develop on 24.03.2017.
 */

public class App extends Application {


    private static Context context;
    private static Bundle bundle;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Bundle getBundle() {
        return bundle;
    }

    public static void setBundle(Bundle bundle) {
        App.bundle = bundle;
    }

    public static Context getContext() {
        return context;
    }
}
