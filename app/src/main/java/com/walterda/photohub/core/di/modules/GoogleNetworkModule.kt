package com.walterda.photohub.core.di.modules

import android.content.Context
import com.walterda.photohub.features.gallery.data.source.AlbumWebService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class GoogleNetworkModule {
    @Singleton
    @Provides
    fun provideGooglePhotosService(@ApplicationContext context: Context): AlbumWebService = AlbumWebService(context)

}