package com.fthdgn.books.dependecy.module;

import com.squareup.picasso.Picasso;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import com.fthdgn.books.api.ImageManager;

@Module
public class PicassoModule {

    @Provides
    @Singleton
    ImageManager imageManager(Picasso picasso) {
        return new ImageManager(picasso);
    }
}
