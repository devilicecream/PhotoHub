package com.walterda.photohub.features.gallery.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.walterda.photohub.core.utils.Constants
import com.walterda.photohub.features.gallery.data.source.PhotoListPagingSource
import com.walterda.photohub.features.gallery.data.source.PhotosWebService
import com.walterda.photohub.features.gallery.domain.models.PhotoListItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
created by Soumik on 6/16/2022
piyal.developer@gmail.com
copyright (c) 2022 Soumik Bhattacharjee. All rights reserved
 **/

class PhotosRepositoryImpl @Inject constructor(private val photosWebService: PhotosWebService) :
    IGalleryRepository {
    override fun getPhotos(page: Int): Flow<PagingData<PhotoListItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = Constants.INITIALLY_LOADED_ITEM_COUNT,
                enablePlaceholders = false
            ), pagingSourceFactory = {
                PhotoListPagingSource(
                    photosWebService
                )
            }
        ).flow
    }
}