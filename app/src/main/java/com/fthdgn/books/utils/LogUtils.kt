package com.fthdgn.books.utils

import android.util.Log
import com.fthdgn.books.BuildConfig
import com.google.firebase.crash.FirebaseCrash

/**
 * Always use this class for logging purposes.
 * It disables logging on release builds.
 */
object LogUtils {

    /**
     * VERBOSE log
     *
     * @param tag      Used to identify the source of a log msg.  It usually identifies
     * the class or activity where the log call occurs.
     * @param msg      The msg you would like logged.
     * @param firebase Also send log to firebase crash
     */
    fun v(tag: String, msg: String, firebase: Boolean) {
        log(Log.VERBOSE, tag, msg, firebase)
    }

    /**
     * VERBOSE log
     *
     * @param tag Used to identify the source of a log msg.  It usually identifies
     * the class or activity where the log call occurs.
     * @param msg The msg you would like logged.
     */
    fun v(tag: String, msg: String) {
        log(Log.VERBOSE, tag, msg, false)
    }

    /**
     * DEBUG log
     *
     * @param tag      Used to identify the source of a log msg.  It usually identifies
     * the class or activity where the log call occurs.
     * @param msg      The msg you would like logged.
     * @param firebase Also send log to firebase crash
     */
    fun d(tag: String, msg: String, firebase: Boolean) {
        log(Log.DEBUG, tag, msg, firebase)
    }

    /**
     * DEBUG log
     *
     * @param tag Used to identify the source of a log msg.  It usually identifies
     * the class or activity where the log call occurs.
     * @param msg The msg you would like logged.
     */
    fun d(tag: String, msg: String) {
        log(Log.DEBUG, tag, msg, false)
    }

    /**
     * INFO log
     *
     * @param tag      Used to identify the source of a log msg.  It usually identifies
     * the class or activity where the log call occurs.
     * @param msg      The msg you would like logged.
     * @param firebase Also send log to firebase crash
     */
    fun i(tag: String, msg: String, firebase: Boolean) {
        log(Log.INFO, tag, msg, firebase)
    }

    /**
     * INFO log
     *
     * @param tag Used to identify the source of a log msg.  It usually identifies
     * the class or activity where the log call occurs.
     * @param msg The msg you would like logged.
     */
    fun i(tag: String, msg: String) {
        log(Log.INFO, tag, msg, false)
    }

    /**
     * WARN log
     *
     * @param tag      Used to identify the source of a log msg.  It usually identifies
     * the class or activity where the log call occurs.
     * @param msg      The msg you would like logged.
     * @param firebase Also send log to firebase crash
     */
    fun w(tag: String, msg: String, firebase: Boolean) {
        log(Log.WARN, tag, msg, firebase)
    }

    /**
     * WARN log
     *
     * @param tag Used to identify the source of a log msg.  It usually identifies
     * the class or activity where the log call occurs.
     * @param msg The msg you would like logged.
     */
    fun w(tag: String, msg: String) {
        log(Log.WARN, tag, msg, false)
    }

    /**
     * ERROR log
     *
     * @param tag      Used to identify the source of a log msg.  It usually identifies
     * the class or activity where the log call occurs.
     * @param msg      The msg you would like logged.
     * @param firebase Also send log to firebase crash
     */
    fun e(tag: String, msg: String, firebase: Boolean) {
        log(Log.ERROR, tag, msg, firebase)
    }

    /**
     * ERROR log
     *
     * @param tag Used to identify the source of a log msg.  It usually identifies
     * the class or activity where the log call occurs.
     * @param msg The msg you would like logged.
     */
    fun e(tag: String, msg: String) {
        log(Log.ERROR, tag, msg, false)
    }

    /**
     * Use to track getApplication, activity, fragment life cycles
     *
     * @param o   Object for class name
     * @param msg Message
     */
    fun lifecycle(o: Any, msg: String) {
        i("Lifecycle", o.javaClass.simpleName + " " + msg, true)
    }

    private fun log(level: Int, tag: String, msg: String, firebase: Boolean) {
        if (firebase && !BuildConfig.DEBUG)
            FirebaseCrash.logcat(level, tag, msg)
        else if (BuildConfig.DEBUG)
            Log.println(level, tag, msg)
    }

}
