package com.walterda.photohub.features.gallery.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.walterda.photohub.core.utils.Constants
import com.walterda.photohub.features.gallery.data.source.AlbumListPagingSource
import com.walterda.photohub.features.gallery.data.source.AlbumWebService
import com.walterda.photohub.features.gallery.domain.models.AlbumListItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
created by Soumik on 6/16/2022
piyal.developer@gmail.com
copyright (c) 2022 Soumik Bhattacharjee. All rights reserved
 **/

class AlbumRepositoryImpl @Inject constructor(private val albumWebService: AlbumWebService) :
    IAlbumRepository {
    override fun getAlbums(page: Int): Flow<PagingData<AlbumListItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = Constants.INITIALLY_LOADED_ITEM_COUNT,
                enablePlaceholders = false
            ), pagingSourceFactory = {
                AlbumListPagingSource(
                    albumWebService
                )
            }
        ).flow
    }
}