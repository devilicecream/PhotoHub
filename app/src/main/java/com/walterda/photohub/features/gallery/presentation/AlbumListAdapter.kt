package com.walterda.photohub.features.gallery.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.walterda.photohub.core.base.BasePagingAdapter
import com.walterda.photohub.core.utils.loadImage
import com.walterda.photohub.databinding.ItemAlbumBinding
import com.walterda.photohub.databinding.ItemGalleryPhotosBinding
import com.walterda.photohub.features.gallery.domain.models.AlbumListItem
import com.walterda.photohub.features.gallery.domain.models.PhotoListItem
import dagger.hilt.android.qualifiers.ActivityContext
import java.lang.Integer.max
import javax.inject.Inject
import kotlin.math.min

/**
created by Soumik on 6/16/2022
piyal.developer@gmail.com
copyright (c) 2022 Soumik Bhattacharjee. All rights reserved
 **/

class AlbumListAdapter @Inject constructor (@ActivityContext private val context: Context) : BasePagingAdapter<AlbumListItem, ItemAlbumBinding>(
    diffCallback = object : DiffUtil.ItemCallback<AlbumListItem>() {
        override fun areItemsTheSame(oldItem: AlbumListItem, newItem: AlbumListItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: AlbumListItem, newItem: AlbumListItem): Boolean {
            return oldItem==newItem
        }

    }
) {

    private var onItemClicked: ((AlbumListItem) -> Unit) ? = null

    override fun createBinding(parent: ViewGroup): ItemAlbumBinding {
        return ItemAlbumBinding.inflate(LayoutInflater.from(parent.context),parent,false)
    }

    override fun bind(binding: ItemAlbumBinding, item: AlbumListItem, position: Int) {
        binding.apply {
            root.setOnClickListener { onItemClicked?.let { it(item) } }
        }
    }

    /*
    * when any photo is clicked
    * */
    fun onItemClicked(listener: ((AlbumListItem) -> Unit)) {
        onItemClicked = listener
    }
}