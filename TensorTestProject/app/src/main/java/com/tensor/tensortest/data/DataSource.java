package com.tensor.tensortest.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

import com.tensor.tensortest.Utils.Settings;
import com.tensor.tensortest.app.App;
import com.tensor.tensortest.async.GetImageFromSrc;
import com.tensor.tensortest.beans.News;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by develop on 12.04.2017.
 */

public class DataSource {

    private SQLiteDatabase database;
    private NewsDbHelper dbHelper;
    private String[] allColumns = { NewsContract.NewsEntry._ID, NewsContract.NewsEntry.COLUMN_NAME, NewsContract.NewsEntry.COLUMN_TITLE, NewsContract.NewsEntry.COLUMN_SHORT_DESCRIPTION,
            NewsContract.NewsEntry.COLUMN_DESCRIPTION, NewsContract.NewsEntry.COLUMN_PUB_DATE, NewsContract.NewsEntry.COLUMN_IMAGE_SRC};

    public DataSource(Context context) {
        dbHelper = new NewsDbHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    /**
     * Добавить новость в базу
     * @param news - новость
     */
    public void addNews(News news) {
        if(App.checkIsNewsInList(getAllNews(), news.getName()) && database.isOpen()) {
            ContentValues values = new ContentValues();
            values.put(NewsContract.NewsEntry.COLUMN_NAME, news.getName());
            values.put(NewsContract.NewsEntry.COLUMN_TITLE, news.getTitle());
            values.put(NewsContract.NewsEntry.COLUMN_SHORT_DESCRIPTION, news.getShortDescription());
            values.put(NewsContract.NewsEntry.COLUMN_DESCRIPTION, news.getDescription());
            values.put(NewsContract.NewsEntry.COLUMN_PUB_DATE, news.getPubDate());
            values.put(NewsContract.NewsEntry.COLUMN_IMAGE_SRC, news.getImageSrc());

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
    }


    /**
     * Получаем все данные из базы
     * @return - list данных
     */
    public List<News> getAllNews() {
        List<News> listNews = new ArrayList<>();
        if (database.isOpen()) {

            Cursor cursor = database.query(NewsContract.NewsEntry.TABLE_NAME,
                    allColumns, null, null, null, null, null);


            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                News result = cursorToNews(cursor);
                listNews.add(result);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return listNews;
    }

    /**
     * Запись данных новости из базы данных
     * @param cursor - позиция в базе
     * @return - готовую новость
     */
    private News cursorToNews(Cursor cursor) {
        News news = new News();
        news.setName(cursor.getString(1));
        news.setTitle(cursor.getString(2));
        news.setShortDescription(cursor.getString(3));
        news.setDescription(cursor.getString(4));
        news.setPubDate(cursor.getString(5));
        news.setImageSrc(cursor.getString(6));
        return news;
    }
}
