package com.fthdgn.books.activity

import android.app.Activity
import android.content.Intent
import android.util.SparseArray
import android.view.View
import com.fthdgn.books.callback.ActivityResultListener


interface ActivitityStarter {
    var listenersLastIndex: Int
        get() = getIndex()
        set(value) {
            setIndex(value)
        }


    fun setIndex(i: Int)
    fun getIndex(): Int

    fun getListeners(): SparseArray<ActivityResultListener>


    fun startActivityForResult(intent: Intent, listener: (Int, Intent?) -> Unit) {
        if (this is Activity)
            startActivityForResult(intent, addActivityResultListener(listener))
        else
            throw IllegalStateException("This interface shoul be used with activities")
    }

    /**
     * Add a activity result listener and returns request code.
     * Use [BaseActivity.startActivityForResult] if possible.
     * If it is not possible use returned request code to start intent.
     *
     * @param listener Listener which will be called
     * @return Request code
     */
    fun addActivityResultListener(listener: (Int, Intent?) -> Unit): Int {
        /*
         * Small possibility.
         * Probably there are a few listeners never removed.
         * They will be overwritten.
         */
        if (listenersLastIndex == 0xffff)
            listenersLastIndex = -1

        getListeners().put(++listenersLastIndex, ActivityResultListener(listener))
        return listenersLastIndex
    }
}

fun <T : View> Activity.startActivityForResult(intent: Intent, listener: (Int, Intent?) -> Unit) {
    if (this is ActivitityStarter)
        this.startActivityForResult(intent, listener)
}

fun <T : View> Activity.addActivityResultListener(listener: (Int, Intent?) -> Unit) {
    if (this is ActivitityStarter)
        this.addActivityResultListener(listener)
}

