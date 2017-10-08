package com.fthdgn.books.callback.implementation;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.fthdgn.books.utils.LogUtils;

public class LogActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        if (savedInstanceState == null)
            LogUtils.lifecycle(activity, "onActivityCreated without state");
        else
            LogUtils.lifecycle(activity, "onActivityCreated with state");
    }

    @Override
    public void onActivityStarted(Activity activity) {
        LogUtils.lifecycle(activity, "onActivityStarted");
    }

    @Override
    public void onActivityResumed(Activity activity) {
        LogUtils.lifecycle(activity, "onActivityResumed");
    }

    @Override
    public void onActivityPaused(Activity activity) {
        LogUtils.lifecycle(activity, "onActivityPaused");
    }

    @Override
    public void onActivityStopped(Activity activity) {
        LogUtils.lifecycle(activity, "onActivityStopped");
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        LogUtils.lifecycle(activity, "onActivitySaveInstanceState");
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        LogUtils.lifecycle(activity, "onActivityDestroyed");
    }
}
