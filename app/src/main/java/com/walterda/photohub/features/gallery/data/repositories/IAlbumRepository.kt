package com.walterda.photohub.features.gallery.data.repositories

import androidx.paging.PagingData
import com.walterda.photohub.features.gallery.domain.models.AlbumListItem
import kotlinx.coroutines.flow.Flow

/**
created by Soumik on 6/16/2022
piyal.developer@gmail.com
copyright (c) 2022 Soumik Bhattacharjee. All rights reserved
 **/

interface IAlbumRepository {
    fun getAlbums(page: Int): Flow<PagingData<AlbumListItem>>
}