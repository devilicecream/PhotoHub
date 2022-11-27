package com.walterda.photohub.core.di.modules

import com.walterda.photohub.features.gallery.data.repositories.AlbumRepositoryImpl
import com.walterda.photohub.features.gallery.data.repositories.GalleryRepositoryImpl
import com.walterda.photohub.features.gallery.data.repositories.IAlbumRepository
import com.walterda.photohub.features.gallery.data.repositories.IGalleryRepository
import com.walterda.photohub.features.gallery.data.source.AlbumWebService
import com.walterda.photohub.features.gallery.data.source.GalleryWebService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
created by Soumik on 6/16/2022
piyal.developer@gmail.com
copyright (c) 2022 Soumik Bhattacharjee. All rights reserved
 **/

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideGalleryRepositoryImplementation(galleryWebService: GalleryWebService) : IGalleryRepository {
        return GalleryRepositoryImpl(galleryWebService)
    }

    @Provides
    @Singleton
    fun provideAlbumRepositoryImplementation(albumWebService: AlbumWebService) : IAlbumRepository {
        return AlbumRepositoryImpl(albumWebService)
    }
}