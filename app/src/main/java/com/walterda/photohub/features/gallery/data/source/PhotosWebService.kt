package com.walterda.photohub.features.gallery.data.source

import android.content.Context
import com.google.gson.Gson
import com.walterda.photohub.core.photos.GoogleIdentity
import com.walterda.photohub.core.utils.LocalStorage
import com.walterda.photohub.features.gallery.domain.models.PhotoList
import com.walterda.photohub.features.gallery.domain.models.PhotoListItem

/**
created by Soumik on 6/15/2022
piyal.developer@gmail.com
copyright (c) 2022 Soumik Bhattacharjee. All rights reserved
 **/

class PhotosWebService(context: Context) {
    private val mContext = context

    suspend fun getPhotos(page: Int) : PhotoList {
        val result = PhotoList()
        if (page > 1) {
            return result
        }
        val album = LocalStorage(mContext).getCurrentPreferenceAlbum()
        album ?: return result
        val response = GoogleIdentity(mContext).loadPhotos(album.id) ?: return result
        for (media in response.iterateAll()) {
            val gson = Gson()
            val mediaString = gson.toJson(media)
            result.add(gson.fromJson(mediaString, PhotoListItem::class.java))
        }
        return result
    }
}