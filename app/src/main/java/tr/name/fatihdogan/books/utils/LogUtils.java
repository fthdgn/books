package tr.name.fatihdogan.books.utils;

import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;

import tr.name.fatihdogan.books.BuildConfig;

/**
 * Always use this class for logging purposes.
 * It disables logging on release builds.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class LogUtils {

    /**
     * VERBOSE log
     *
     * @param tag      Used to identify the source of a log msg.  It usually identifies
     *                 the class or activity where the log call occurs.
     * @param msg      The msg you would like logged.
     * @param firebase Also send log to firebase crash
     */
    public static void v(String tag, String msg, boolean firebase) {
        log(Log.VERBOSE, tag, msg, firebase);
    }

    /**
     * VERBOSE log
     *
     * @param tag Used to identify the source of a log msg.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The msg you would like logged.
     */
    public static void v(String tag, String msg) {
        log(Log.VERBOSE, tag, msg, false);
    }

    /**
     * DEBUG log
     *
     * @param tag      Used to identify the source of a log msg.  It usually identifies
     *                 the class or activity where the log call occurs.
     * @param msg      The msg you would like logged.
     * @param firebase Also send log to firebase crash
     */
    public static void d(String tag, String msg, boolean firebase) {
        log(Log.DEBUG, tag, msg, firebase);
    }

    /**
     * DEBUG log
     *
     * @param tag Used to identify the source of a log msg.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The msg you would like logged.
     */
    public static void d(String tag, String msg) {
        log(Log.DEBUG, tag, msg, false);
    }

    /**
     * INFO log
     *
     * @param tag      Used to identify the source of a log msg.  It usually identifies
     *                 the class or activity where the log call occurs.
     * @param msg      The msg you would like logged.
     * @param firebase Also send log to firebase crash
     */
    public static void i(String tag, String msg, boolean firebase) {
        log(Log.INFO, tag, msg, firebase);
    }

    /**
     * INFO log
     *
     * @param tag Used to identify the source of a log msg.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The msg you would like logged.
     */
    public static void i(String tag, String msg) {
        log(Log.INFO, tag, msg, false);
    }

    /**
     * WARN log
     *
     * @param tag      Used to identify the source of a log msg.  It usually identifies
     *                 the class or activity where the log call occurs.
     * @param msg      The msg you would like logged.
     * @param firebase Also send log to firebase crash
     */
    public static void w(String tag, String msg, boolean firebase) {
        log(Log.WARN, tag, msg, firebase);
    }

    /**
     * WARN log
     *
     * @param tag Used to identify the source of a log msg.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The msg you would like logged.
     */
    public static void w(String tag, String msg) {
        log(Log.WARN, tag, msg, false);
    }

    /**
     * ERROR log
     *
     * @param tag      Used to identify the source of a log msg.  It usually identifies
     *                 the class or activity where the log call occurs.
     * @param msg      The msg you would like logged.
     * @param firebase Also send log to firebase crash
     */
    public static void e(String tag, String msg, boolean firebase) {
        log(Log.ERROR, tag, msg, firebase);
    }

    /**
     * ERROR log
     *
     * @param tag Used to identify the source of a log msg.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The msg you would like logged.
     */
    public static void e(String tag, String msg) {
        log(Log.ERROR, tag, msg, false);
    }

    /**
     * Use to track app, activity, fragment life cycles
     *
     * @param o   Object for class name
     * @param msg Message
     */
    public static void lifecycle(Object o, String msg) {
        i("Lifecycle", o.getClass().getSimpleName() + " " + msg, true);
    }

    private static void log(int level, String tag, String msg, boolean firebase) {
        if (firebase && !BuildConfig.DEBUG)
            FirebaseCrash.logcat(level, tag, msg);
        else if (BuildConfig.DEBUG)
            Log.println(level, tag, msg);
    }

}
