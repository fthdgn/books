package tr.name.fatihdogan.books;

import android.app.Application;

import tr.name.fatihdogan.books.callback.implementation.LogActivityLifecycleCallbacks;
import tr.name.fatihdogan.books.callback.implementation.LogComponentCallbacks;
import tr.name.fatihdogan.books.repository.AppDatabase;
import tr.name.fatihdogan.books.utils.LogUtils;
import tr.name.fatihdogan.googlebooksapi.BooksApiManager;

public class BaseApplication extends Application {

    private static BaseApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.lifecycle(this, "onCreate");
        registerComponentCallbacks(new LogComponentCallbacks());
        registerActivityLifecycleCallbacks(new LogActivityLifecycleCallbacks());

        mInstance = this;
        BooksApiManager.initialize(this);
        BooksApiManager.getInstance().setDebugEnabled(BuildConfig.DEBUG);

        AppDatabase.initialize(this);
    }

    public static BaseApplication getInstance() {
        return mInstance;
    }

}
