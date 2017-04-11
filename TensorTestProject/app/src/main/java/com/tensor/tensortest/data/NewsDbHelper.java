package com.tensor.tensortest.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.tensor.tensortest.Utils.Settings;

import java.util.Set;

/**
 * Created by develop on 03.04.2017.
 */

public class NewsDbHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "news.db";
    private static final int DATABASE_VERSION = 1;

    public NewsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(Settings.TAG, "Constructor dbHelper");
    }

    /**
     * Вызывается при создании базы данных
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Строка для создания таблицы
        String SQL_CREATE_TABLE = "CREATE TABLE " + NewsContract.NewsEntry.TABLE_NAME + " ("
                + NewsContract.NewsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NewsContract.NewsEntry.COLUMN_TITLE + " TEXT NOT NULL, "
                + NewsContract.NewsEntry.COLUMN_SHORT_DESCRIPTION + " TEXT NOT NULL, "
                + NewsContract.NewsEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, "
                + NewsContract.NewsEntry.COLUMN_PUB_DATE + " TEXT NOT NULL, "
                + NewsContract.NewsEntry.COLUMN_IMAGE_TITLE + " TEXT NOT NULL);";

        Log.d(Settings.TAG, SQL_CREATE_TABLE);

        // Запускаем создание таблицы
        db.execSQL(SQL_CREATE_TABLE);
    }

    /**
     * Вызывается при обновлении схемы базы данных
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + NewsContract.NewsEntry.TABLE_NAME);
        onCreate(db);
    }
}
