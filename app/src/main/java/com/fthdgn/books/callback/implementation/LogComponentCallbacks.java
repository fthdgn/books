package com.fthdgn.books.callback.implementation;

import android.content.ComponentCallbacks2;
import android.content.res.Configuration;

import com.fthdgn.books.utils.LogUtils;

public class LogComponentCallbacks implements ComponentCallbacks2 {

    @Override
    public void onTrimMemory(int level) {
        LogUtils.lifecycle(LogComponentCallbacks.this, "onTrimMemory");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        LogUtils.lifecycle(LogComponentCallbacks.this, "onConfigurationChanged " + newConfig.toString());
    }

    @Override
    public void onLowMemory() {
        LogUtils.lifecycle(LogComponentCallbacks.this, "onLowMemory");
    }
}
