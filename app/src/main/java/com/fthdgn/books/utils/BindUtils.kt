package com.fthdgn.books.utils

import android.app.Activity
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.support.annotation.IdRes
import android.support.v4.app.FragmentActivity
import android.view.View

fun <T : View> Activity.bind(@IdRes idRes: Int): Lazy<T> = unsafeLazy { findViewById<T>(idRes) }

fun <T : View> View.bind(@IdRes idRes: Int): Lazy<T> = unsafeLazy { findViewById<T>(idRes) }

private fun <T> unsafeLazy(initializer: () -> T) = lazy(LazyThreadSafetyMode.NONE, initializer)

fun <T : ViewModel> FragmentActivity.getViewModel(viewModel: Class<T>): Lazy<T> {
    return unsafeLazy {
        ViewModelProviders.of(this).get(viewModel)
    }
}

