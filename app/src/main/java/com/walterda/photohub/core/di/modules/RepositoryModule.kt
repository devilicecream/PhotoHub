package com.walterda.photohub.core.di.modules

import com.walterda.photohub.features.gallery.data.repositories.*
import com.walterda.photohub.features.gallery.data.source.AlbumWebService
import com.walterda.photohub.features.gallery.data.source.PhotosWebService
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

//    @Provides
//    @Singleton
//    fun provideGalleryRepositoryImplementation(galleryWebService: GalleryWebService) : IGalleryRepository {
//        return GalleryRepositoryImpl(galleryWebService)
//    }

    @Provides
    @Singleton
    fun provideGalleryRepositoryImplementation(photosWebService: PhotosWebService) : IGalleryRepository {
        return PhotosRepositoryImpl(photosWebService)
    }

    @Provides
    @Singleton
    fun provideAlbumRepositoryImplementation(albumWebService: AlbumWebService) : IAlbumRepository {
        return AlbumRepositoryImpl(albumWebService)
    }
}