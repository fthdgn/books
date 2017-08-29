package tr.name.fatihdogan.books.dependecy.module;

import android.app.Application;
import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class PreferencesModule {

    private static final String PREFERENCES_NAME = "MyPref";

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences(Application application) {
        return application.getSharedPreferences(PREFERENCES_NAME, 0);
    }

}
