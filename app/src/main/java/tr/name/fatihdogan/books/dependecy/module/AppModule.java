package tr.name.fatihdogan.books.dependecy.module;

import android.app.Application;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private final Application application;

    public AppModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Application providesApplication() {
        return application;
    }

    @Provides
    @Singleton
    InputMethodManager inputMethodManager(Application application) {
        return (InputMethodManager) application.getSystemService(Context.INPUT_METHOD_SERVICE);
    }
}
