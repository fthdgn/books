package com.fthdgn.books.callback.implementation

import android.content.ComponentCallbacks2
import android.content.res.Configuration

import com.fthdgn.books.utils.LogUtils

class LogComponentCallbacks : ComponentCallbacks2 {

    override fun onTrimMemory(level: Int) {
        LogUtils.lifecycle(this@LogComponentCallbacks, "onTrimMemory")
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        LogUtils.lifecycle(this@LogComponentCallbacks, "onConfigurationChanged " + newConfig.toString())
    }

    override fun onLowMemory() {
        LogUtils.lifecycle(this@LogComponentCallbacks, "onLowMemory")
    }
}
