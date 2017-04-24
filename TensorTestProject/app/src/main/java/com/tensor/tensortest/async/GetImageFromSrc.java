package com.tensor.tensortest.async;

import android.os.AsyncTask;
import android.util.Log;

import com.tensor.tensortest.Utils.Settings;
import com.tensor.tensortest.beans.News;

/**
 * Created by develop on 24.04.2017.
 */

public class GetImageFromSrc extends AsyncTask<News, Void, Void> {

    @Override
    protected Void doInBackground(News... newses) {

        Log.d(Settings.TAG, "GetImageFromSrc async start");
        News news = newses[0];
        try {
                news.setImage(Settings.bytesFromUrl(news.getImageSrc()));
                news.setReady(true);
                Log.d(Settings.TAG, "GetImageFromSrc - картинка загружена для новости с name - " + news.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
