package com.tensor.tensortest.Utils;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
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
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss Z", Locale.ROOT);
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat resultFormatter = new SimpleDateFormat("dd MMM HH:mm", Locale.ROOT);
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
        SimpleDateFormat  formatter = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss Z", Locale.ROOT);
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            return formatter.parse(str).getTime();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return System.currentTimeMillis();
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
