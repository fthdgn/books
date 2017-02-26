package tr.name.fatihdogan.books.utils;

import android.util.Log;

import tr.name.fatihdogan.books.BuildConfig;

@SuppressWarnings({"WeakerAccess", "unused"})
public class LogUtils {

    public static void logInfo(String tag, String message) {
        if (BuildConfig.DEBUG)
            Log.i(tag, message);
    }

    public static void logCodeLocation(String message) {
        logInfo("CodeLocation", message);
    }

}
