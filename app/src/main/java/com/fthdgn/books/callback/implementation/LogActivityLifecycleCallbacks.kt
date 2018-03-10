package com.fthdgn.books.callback.implementation

import android.app.Activity
import android.app.Application
import android.os.Bundle

import com.fthdgn.books.utils.LogUtils

class LogActivityLifecycleCallbacks : Application.ActivityLifecycleCallbacks {

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        if (savedInstanceState == null)
            LogUtils.lifecycle(activity, "onActivityCreated without state")
        else
            LogUtils.lifecycle(activity, "onActivityCreated with state")
    }

    override fun onActivityStarted(activity: Activity) {
        LogUtils.lifecycle(activity, "onActivityStarted")
    }

    override fun onActivityResumed(activity: Activity) {
        LogUtils.lifecycle(activity, "onActivityResumed")
    }

    override fun onActivityPaused(activity: Activity) {
        LogUtils.lifecycle(activity, "onActivityPaused")
    }

    override fun onActivityStopped(activity: Activity) {
        LogUtils.lifecycle(activity, "onActivityStopped")
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        LogUtils.lifecycle(activity, "onActivitySaveInstanceState")
    }

    override fun onActivityDestroyed(activity: Activity) {
        LogUtils.lifecycle(activity, "onActivityDestroyed")
    }
}
