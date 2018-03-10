package com.fthdgn.books.activity

import android.app.Activity
import android.util.SparseArray
import com.fthdgn.books.callback.ActivityResultListener

/**
 * Created by fatih on 16-Dec-17.
 */
class BActivity : Activity(), ActivitityStarter {
    var i: Int = 0
    var l: SparseArray<ActivityResultListener> = SparseArray<ActivityResultListener>()

    override fun setIndex(i: Int) {
        this.i = i;
    }

    override fun getIndex(): Int {
        return i;
    }

    override fun getListeners(): SparseArray<ActivityResultListener> {
        return l;
    }
}