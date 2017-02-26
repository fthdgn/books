package tr.name.fatihdogan.books;

import android.app.Application;

import tr.name.fatihdogan.googlebooksapi.BooksApiManager;

public class BaseApplication extends Application {

    private static String FILES_DIR_PATH;
    private static BaseApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        BooksApiManager.initialize(this);
        BooksApiManager.getInstance().setDebugEnabled(BuildConfig.DEBUG);
        FILES_DIR_PATH = getFilesDir().getAbsolutePath();
    }

    public static BaseApplication getInstance() {
        return mInstance;
    }

    public static String getFilesDirPath() {
        return FILES_DIR_PATH;
    }
}
