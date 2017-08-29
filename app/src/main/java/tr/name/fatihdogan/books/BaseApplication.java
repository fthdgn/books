package tr.name.fatihdogan.books;

import android.app.Application;

import tr.name.fatihdogan.books.callback.implementation.LogActivityLifecycleCallbacks;
import tr.name.fatihdogan.books.callback.implementation.LogComponentCallbacks;
import tr.name.fatihdogan.books.dependecy.component.AppComponent;
import tr.name.fatihdogan.books.dependecy.component.DaggerAppComponent;
import tr.name.fatihdogan.books.dependecy.module.ApiModule;
import tr.name.fatihdogan.books.dependecy.module.AppModule;
import tr.name.fatihdogan.books.dependecy.module.NetworkModule;
import tr.name.fatihdogan.books.dependecy.module.PicassoModule;
import tr.name.fatihdogan.books.dependecy.module.PreferencesModule;
import tr.name.fatihdogan.books.dependecy.module.RepositoryModule;
import tr.name.fatihdogan.books.utils.LogUtils;

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
