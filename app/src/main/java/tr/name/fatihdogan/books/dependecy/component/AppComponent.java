package tr.name.fatihdogan.books.dependecy.component;

import android.view.inputmethod.InputMethodManager;

import javax.inject.Singleton;

import dagger.Component;
import tr.name.fatihdogan.books.api.AccountManager;
import tr.name.fatihdogan.books.api.GoogleBooksApi;
import tr.name.fatihdogan.books.api.ImageManager;
import tr.name.fatihdogan.books.dependecy.module.ApiModule;
import tr.name.fatihdogan.books.dependecy.module.AppModule;
import tr.name.fatihdogan.books.dependecy.module.NetworkModule;
import tr.name.fatihdogan.books.dependecy.module.PicassoModule;
import tr.name.fatihdogan.books.dependecy.module.PreferencesModule;
import tr.name.fatihdogan.books.dependecy.module.RepositoryModule;
import tr.name.fatihdogan.books.repository.BookDao;

@Singleton
@Component(modules = {
        ApiModule.class,
        AppModule.class,
        NetworkModule.class,
        PreferencesModule.class,
        RepositoryModule.class,
        PicassoModule.class})
public interface AppComponent {

    BookDao bookDao();

    GoogleBooksApi googleBooksApi();

    AccountManager accountManager();

    InputMethodManager inputMethodManager();

    ImageManager imageManager();

}
