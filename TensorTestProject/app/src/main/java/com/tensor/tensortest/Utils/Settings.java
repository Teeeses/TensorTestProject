package com.tensor.tensortest.Utils;


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
}
