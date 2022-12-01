package com.walterda.photohub.features.gallery.domain.models
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.net.URL


/**
created by Soumik on 6/16/2022
piyal.developer@gmail.com
copyright (c) 2022 Soumik Bhattacharjee. All rights reserved
 **/

class PhotoList : ArrayList<PhotoListItem>()

@Parcelize
data class PhotoListItem(
    @SerializedName("id_")
    var id: String,
    @SerializedName("productUrl_")
    var productUrl: URL,
    @SerializedName("baseUrl_")
    var baseUrl: URL,
    @SerializedName("mediaMetadata_")
    var mediaMetadata: MediaMetadata,
) : Parcelable

@Parcelize
data class MediaMetadata(
    @SerializedName("width_")
    var width: Int,
    @SerializedName("height_")
    var height: Int,
) : Parcelable
