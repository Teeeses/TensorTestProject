package com.tensor.tensortest.Utils;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by develop on 23.03.2017.
 */

public class Settings {

    public final static String UI_TAG = "UiLog";
    public final static String TAG = "Log";

    /**
     * Перевод строки времени <EEE, dd MMM yyyy hh:mm:ss Z> в нужным нам формат
     * @param str - время
     * @return - отформатированное время
     */
    public static String timeToString(String str){
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss Z", Locale.ENGLISH);
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat resultFormatter = new SimpleDateFormat("dd MMM HH:mm", Locale.ENGLISH);
        try {
            Date result = formatter.parse(str);
            return resultFormatter.format(result.getTime());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    /**
     * Переводит время в формате <EEE, dd MMM yyyy hh:mm:ss Z> в миллисекунды
     * @param str - время
     * @return - миллисекунды
     */
    public static long stringToMills(String str) {
        SimpleDateFormat  formatter = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss Z", Locale.ENGLISH);
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            return formatter.parse(str).getTime();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return System.currentTimeMillis();
    }

    /**
     * Ковертируем миллисекунды в дату
     * @param milliSeconds - миллисекунды
     * @return - дата в заданном формате
     */
    public static String getDate(long milliSeconds)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM HH:mm", Locale.ENGLISH);
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }


    public static Bitmap drawableToBitmap(Drawable drawable) {
        return ((BitmapDrawable) drawable).getBitmap();
    }

    public static Drawable drawableFromUrl(String url) throws IOException {
        Bitmap x;

        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.connect();
        InputStream input = connection.getInputStream();


        x = BitmapFactory.decodeStream(input);
        return new BitmapDrawable(x);
    }

    /**
     * Забераем байты картинки
     * @param url
     * @return
     * @throws IOException
     */
    public static byte[] bytesFromUrl(String url) throws IOException {

        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.connect();
        InputStream input = connection.getInputStream();

        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = input.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }

        return byteBuffer.toByteArray();
    }

    public static  Bitmap bytesToBitmap(byte[] arrey) {
        return BitmapFactory.decodeByteArray(arrey, 0, arrey.length);
    }

    public static Bitmap resize(Drawable image, int width, int height) {
        Bitmap b = ((BitmapDrawable)image).getBitmap();
        Bitmap bitmapResized = Bitmap.createScaledBitmap(b, width, height, false);
        return bitmapResized;
    }

    // convert from bitmap to byte array
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }


}
