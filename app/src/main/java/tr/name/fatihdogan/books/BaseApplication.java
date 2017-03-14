package tr.name.fatihdogan.books;

import android.app.Application;
import android.os.AsyncTask;

import tr.name.fatihdogan.books.data.Book;
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

    public static String getFilesDirPath() {
        return FILES_DIR_PATH;
    }
}
