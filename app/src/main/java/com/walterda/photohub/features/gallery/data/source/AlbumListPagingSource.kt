package com.walterda.photohub.features.gallery.data.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.walterda.photohub.features.gallery.domain.models.AlbumListItem
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
created by Soumik on 6/16/2022
piyal.developer@gmail.com
copyright (c) 2022 Soumik Bhattacharjee. All rights reserved
 **/

class AlbumListPagingSource @Inject constructor(private val albumWebService: AlbumWebService) :
    PagingSource<Int, AlbumListItem>() {

    companion object {
        private const val STARTING_PAGE_NUMBER = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AlbumListItem> {
        val page = params.key ?: STARTING_PAGE_NUMBER

        return try {
            val list = albumWebService.getAlbums(page = page)
            LoadResult.Page(
                data = list,
                nextKey = if (list.isNotEmpty()) page + 1 else null,
                prevKey = if (page == STARTING_PAGE_NUMBER) null else page - 1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, AlbumListItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}