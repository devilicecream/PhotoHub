package com.walterda.photohub.features.gallery.data.source

import android.content.Context
import android.util.Log
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

    suspend fun getAlbums(page: Int) : AlbumList {
        Log.e("ALBUMS", "started!!")
        val result = AlbumList()
        if (page > 1) {
            return result
        }
        val response = GoogleIdentity(mContext).loadAlbums() ?: return result
        for (album in response.iterateAll()) {
            val gson = Gson()
            val albumString = gson.toJson(album)
            Log.e("ALBUM", albumString)
            result.add(gson.fromJson(albumString, AlbumListItem::class.java))
        }
        Log.e("ALBUMS", result.toString())
        return result
    }
}