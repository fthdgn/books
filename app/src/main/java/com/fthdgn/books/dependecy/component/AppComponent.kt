package com.fthdgn.books.dependecy.component

import com.fthdgn.books.api.AccountManager
import com.fthdgn.books.api.GoogleBooksApi
import com.fthdgn.books.api.ImageManager
import com.fthdgn.books.dependecy.module.*
import com.fthdgn.books.repository.BookDao
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(ApiModule::class, AppModule::class, NetworkModule::class, PreferencesModule::class, RepositoryModule::class, PicassoModule::class))
interface AppComponent {

    fun bookDao(): BookDao

    fun googleBooksApi(): GoogleBooksApi

    fun accountManager(): AccountManager

    fun imageManager(): ImageManager

}
