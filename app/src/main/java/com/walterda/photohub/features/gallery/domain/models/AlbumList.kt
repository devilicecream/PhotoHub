package com.walterda.photohub.features.gallery.domain.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

class AlbumList : ArrayList<AlbumListItem>()

@Parcelize
data class AlbumListItem(
    @SerializedName("alt_description")
    var altDescription: String?,
    @SerializedName("blur_hash")
    var blurHash: String?,
    @SerializedName("color")
    var color: String?,
    @SerializedName("created_at")
    var createdAt: String?,
    @SerializedName("description")
    var description: String?,
    @SerializedName("height")
    var height: Int?,
    @SerializedName("id")
    var id: String?,
    @SerializedName("links")
    var links: Links?,
    @SerializedName("promoted_at")
    var promotedAt: String?,
    @SerializedName("updated_at")
    var updatedAt: String?,
    @SerializedName("urls")
    var urls: Urls?,
    @SerializedName("width")
    var width: Int?
) : Parcelable