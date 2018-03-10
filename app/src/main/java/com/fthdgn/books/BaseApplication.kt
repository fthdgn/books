package com.fthdgn.books

import android.app.Application
import com.fthdgn.books.callback.implementation.LogActivityLifecycleCallbacks
import com.fthdgn.books.callback.implementation.LogComponentCallbacks
import com.fthdgn.books.dependecy.component.AppComponent
import com.fthdgn.books.dependecy.component.DaggerAppComponent
import com.fthdgn.books.dependecy.module.*
import com.fthdgn.books.utils.LogUtils

class BaseApplication : Application() {
    val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
                .apiModule(ApiModule())
                .appModule(AppModule(this))
                .repositoryModule(RepositoryModule())
                .preferencesModule(PreferencesModule())
                .networkModule(NetworkModule())
                .picassoModule(PicassoModule())
                .build()
    }

    override fun onCreate() {
        super.onCreate()
        baseApplication = this
        LogUtils.lifecycle(this, "onCreate")
        registerComponentCallbacks(LogComponentCallbacks())
        registerActivityLifecycleCallbacks(LogActivityLifecycleCallbacks())
    }

    companion object {

        private lateinit var baseApplication: BaseApplication

        fun getAppComponent(): AppComponent = baseApplication.appComponent
    }
}
