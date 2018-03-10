package com.fthdgn.books.dependecy.module

import com.fthdgn.books.api.ImageManager
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PicassoModule {

    @Provides
    @Singleton
    internal fun imageManager(picasso: Picasso): ImageManager =
            ImageManager(picasso)
}
