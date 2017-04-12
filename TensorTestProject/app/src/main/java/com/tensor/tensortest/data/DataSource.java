package com.tensor.tensortest.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.tensor.tensortest.Utils.Settings;
import com.tensor.tensortest.app.App;
import com.tensor.tensortest.beans.News;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by develop on 12.04.2017.
 */

public class DataSource {

    private SQLiteDatabase database;
    private NewsDbHelper dbHelper;
    private String[] allColumns = { NewsContract.NewsEntry._ID, NewsContract.NewsEntry.COLUMN_TITLE, NewsContract.NewsEntry.COLUMN_SHORT_DESCRIPTION,
            NewsContract.NewsEntry.COLUMN_DESCRIPTION, NewsContract.NewsEntry.COLUMN_PUB_DATE, NewsContract.NewsEntry.COLUMN_IMAGE_TITLE, NewsContract.NewsEntry.COLUMN_READY, NewsContract.NewsEntry.COLUMN_IMAGE};

    public DataSource(Context context) {
        dbHelper = new NewsDbHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void addNews(News news) {
        ContentValues values = new ContentValues();
        values.put(NewsContract.NewsEntry.COLUMN_TITLE, news.getTitle());
        values.put(NewsContract.NewsEntry.COLUMN_SHORT_DESCRIPTION, news.getShortDescription());
        values.put(NewsContract.NewsEntry.COLUMN_DESCRIPTION, news.getDescription());
        values.put(NewsContract.NewsEntry.COLUMN_PUB_DATE, news.getPubDate());
        values.put(NewsContract.NewsEntry.COLUMN_IMAGE_TITLE, news.getImageTitle());
        values.put(NewsContract.NewsEntry.COLUMN_READY, news.getReady().toString());
        values.put(NewsContract.NewsEntry.COLUMN_IMAGE, Settings.getBytes(news.getImage()));

        long insertId = database.insert(NewsContract.NewsEntry.TABLE_NAME, null, values);
        if (insertId == -1) {
            Log.d(Settings.TAG, "Ошибка добавления новости в базу");
        } else {
            Log.d(Settings.TAG, "Новость успешно добавлена в базу");
        }

        Cursor cursor = database.query(NewsContract.NewsEntry.TABLE_NAME, allColumns, NewsContract.NewsEntry._ID + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        cursor.close();
    }

    public List<News> getAllNews() {
        List<News> listNews = new ArrayList<>();

        Cursor cursor = database.query(NewsContract.NewsEntry.TABLE_NAME,
                allColumns, null, null, null, null, null);


        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            News result = cursorToNews(cursor);
            listNews.add(result);
            cursor.moveToNext();
        }
        cursor.close();
        return listNews;
    }

    private News cursorToNews(Cursor cursor) {
        News news = new News();
        news.setTitle(cursor.getString(1));
        news.setShortDescription(cursor.getString(2));
        news.setDescription(cursor.getString(3));
        news.setPubDate(cursor.getString(4));
        news.setImageTitle(cursor.getString(5));
        news.setReady(Boolean.valueOf(cursor.getString(6)));
        news.setImage(Settings.getImage(cursor.getBlob(7)));
        return news;
    }
}