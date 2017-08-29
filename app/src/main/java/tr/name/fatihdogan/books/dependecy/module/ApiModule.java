package tr.name.fatihdogan.books.dependecy.module;

import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import tr.name.fatihdogan.books.api.AccountManager;

@Module
public class ApiModule {

    @Provides
    @Singleton
    AccountManager providesAccountManager(SharedPreferences sharedPreferences) {
        return new AccountManager(sharedPreferences);
    }

}
