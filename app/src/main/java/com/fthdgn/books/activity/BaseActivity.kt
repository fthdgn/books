package com.fthdgn.books.activity

import android.content.Intent
import android.support.annotation.CallSuper
import android.support.annotation.IntRange
import android.support.v7.app.AppCompatActivity
import android.util.SparseArray
import com.fthdgn.books.callback.ActivityResultListener


abstract class BaseActivity : AppCompatActivity() {

    //region ActivityResultListener
    private val activityResultListeners = SparseArray<ActivityResultListener>()
    private var listenersLastIndex = -1

    @CallSuper
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val listener = getActivityResultListener(requestCode)
        listener?.onActivityResult(resultCode, data)
        removeActivityResultListener(requestCode)
    }

    /**
     * Starts an activity intent for result.
     * When result received the listener will be called.
     *
     * @param intent   Intent which will be started
     * @param listener The listener which will be called when result is received
     */
    fun startActivityForResult(intent: Intent, listener: (Int, Intent?) -> Unit) {
        startActivityForResult(intent, addActivityResultListener(listener))
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

        activityResultListeners.put(++listenersLastIndex, ActivityResultListener(listener))
        return listenersLastIndex
    }

    /**
     * Result listener for given request code.
     *
     * @param requestCode Request code (form 0 to 65535 (0xffff))
     * @return Result listener
     */
    private fun getActivityResultListener(@IntRange(from = 0, to = 0xffff) requestCode: Int): ActivityResultListener? = activityResultListeners.get(requestCode)

    /**
     * Removes result listener for given request code
     *
     * @param requestCode Request code (form 0 to 65535 (0xffff))
     */
    private fun removeActivityResultListener(@IntRange(from = 0, to = 0xffff) requestCode: Int) {
        activityResultListeners.remove(requestCode)
        if (activityResultListeners.size() == 0)
            listenersLastIndex = -1
    }
    //endregion

}