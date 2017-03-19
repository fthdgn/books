package tr.name.fatihdogan.books.callback.implementation;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import tr.name.fatihdogan.books.utils.LogUtils;

public class LogActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        LogUtils.lifecycle(activity, "onActivityCreated");
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
