package com.fthdgn.books;

import android.app.Application;

import com.fthdgn.books.callback.implementation.LogActivityLifecycleCallbacks;
import com.fthdgn.books.callback.implementation.LogComponentCallbacks;
import com.fthdgn.books.dependecy.component.AppComponent;
import com.fthdgn.books.dependecy.component.DaggerAppComponent;
import com.fthdgn.books.dependecy.module.ApiModule;
import com.fthdgn.books.dependecy.module.AppModule;
import com.fthdgn.books.dependecy.module.NetworkModule;
import com.fthdgn.books.dependecy.module.PicassoModule;
import com.fthdgn.books.dependecy.module.PreferencesModule;
import com.fthdgn.books.dependecy.module.RepositoryModule;
import com.fthdgn.books.utils.LogUtils;

public class BaseApplication extends Application {

    private static BaseApplication baseApplication;
    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.lifecycle(this, "onCreate");
        registerComponentCallbacks(new LogComponentCallbacks());
        registerActivityLifecycleCallbacks(new LogActivityLifecycleCallbacks());
        baseApplication = this;
        appComponent = DaggerAppComponent.builder().
                apiModule(new ApiModule()).
                appModule(new AppModule(this)).
                repositoryModule(new RepositoryModule()).
                preferencesModule(new PreferencesModule()).
                networkModule(new NetworkModule()).
                picassoModule(new PicassoModule()).
                build();
    }

    public static AppComponent getAppComponent() {
        return baseApplication.appComponent;
    }
}
