package com.walterda.photohub.features.gallery.data.source

import android.content.Context
import com.google.gson.Gson
import com.walterda.photohub.core.photos.GoogleIdentity
import com.walterda.photohub.features.gallery.domain.models.AlbumList
import com.walterda.photohub.features.gallery.domain.models.AlbumListItem

/**
created by Soumik on 6/15/2022
piyal.developer@gmail.com
copyright (c) 2022 Soumik Bhattacharjee. All rights reserved
 **/

class AlbumWebService(context: Context) {
    private val mContext = context

    fun getAlbums(page: Int) : AlbumList {
        val result = AlbumList()
        if (page > 0) {
            return result
        }
        val response = GoogleIdentity(mContext).loadAlbums() ?: return result
        for (album in response.iterateAll()) {
            val gson = Gson()
            val albumString = gson.toJson(album)
            result.add(gson.fromJson(albumString, AlbumListItem::class.java))
        }
        return result
    }
}