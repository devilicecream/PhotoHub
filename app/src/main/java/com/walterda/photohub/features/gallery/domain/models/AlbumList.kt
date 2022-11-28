package com.walterda.photohub.features.gallery.domain.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.net.URL

class AlbumList : ArrayList<AlbumListItem>()

@Parcelize
data class AlbumListItem(
    @SerializedName("title_")
    var title: String?,
    @SerializedName("productUrl_")
    var productUrl: URL?,
    @SerializedName("mediaItemsCount_")
    var mediaItemsCount: Int,
    @SerializedName("id_")
    var id: String,
    @SerializedName("coverPhotoMediaItemId_")
    var coverPhotoMediaItemId: String?,
    @SerializedName("coverPhotoBaseUrl_")
    var coverPhotoBaseUrl: URL?,
) : Parcelable