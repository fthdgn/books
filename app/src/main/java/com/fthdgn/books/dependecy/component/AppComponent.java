package com.fthdgn.books.dependecy.component;

import android.view.inputmethod.InputMethodManager;

import javax.inject.Singleton;

import dagger.Component;
import com.fthdgn.books.api.AccountManager;
import com.fthdgn.books.api.GoogleBooksApi;
import com.fthdgn.books.api.ImageManager;
import com.fthdgn.books.dependecy.module.ApiModule;
import com.fthdgn.books.dependecy.module.AppModule;
import com.fthdgn.books.dependecy.module.NetworkModule;
import com.fthdgn.books.dependecy.module.PicassoModule;
import com.fthdgn.books.dependecy.module.PreferencesModule;
import com.fthdgn.books.dependecy.module.RepositoryModule;
import com.fthdgn.books.repository.BookDao;

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
