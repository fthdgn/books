package tr.name.fatihdogan.books;

import android.app.Application;
import android.os.AsyncTask;

import tr.name.fatihdogan.books.callback.implementation.LogActivityLifecycleCallbacks;
import tr.name.fatihdogan.books.callback.implementation.LogComponentCallbacks;
import tr.name.fatihdogan.books.data.Book;
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

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                Book.loadBooks();
                return null;
            }
        }.execute();
    }

    public static BaseApplication getInstance() {
        return mInstance;
    }

}
