package com.tensor.tensortest.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.tensor.tensortest.Utils.Settings;


/**
 * Created by develop on 03.04.2017.
 */

public class NewsDbHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "news.db";
    private static final int DATABASE_VERSION = 9;

    public NewsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(Settings.TAG, "Constructor dbHelper");
    }

    /**
     * Вызывается при создании таблицы
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(Settings.TAG, "dbHelper onCreate");
        // Строка для создания таблицы
        String SQL_CREATE_TABLE = "create table " + NewsContract.NewsEntry.TABLE_NAME + " ("
                + NewsContract.NewsEntry._ID + " integer primary key autoincrement, "
                + NewsContract.NewsEntry.COLUMN_NAME + " text not null, "
                + NewsContract.NewsEntry.COLUMN_TITLE + " text not null, "
                + NewsContract.NewsEntry.COLUMN_SHORT_DESCRIPTION + " text not null, "
                + NewsContract.NewsEntry.COLUMN_DESCRIPTION + " text not null, "
                + NewsContract.NewsEntry.COLUMN_PUB_DATE + " text not null, "
                + NewsContract.NewsEntry.COLUMN_READY + " text not null, "
                + NewsContract.NewsEntry.COLUMN_IMAGE + " BLOB);";

        Log.d(Settings.TAG, SQL_CREATE_TABLE);

        // Запускаем создание таблицы
        db.execSQL(SQL_CREATE_TABLE);
    }

    /**
     * Вызывается при обновлении схемы базы данных
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(Settings.TAG, "dbHelper onUpgrade");
        db.execSQL("DROP TABLE IF EXISTS " + NewsContract.NewsEntry.TABLE_NAME);
        onCreate(db);
    }
}
