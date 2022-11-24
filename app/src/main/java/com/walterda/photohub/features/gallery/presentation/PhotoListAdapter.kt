package com.walterda.photohub.features.gallery.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.walterda.photohub.core.base.BasePagingAdapter
import com.walterda.photohub.core.utils.loadImage
import com.walterda.photohub.databinding.ItemGalleryPhotosBinding
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

class PhotoListAdapter @Inject constructor (@ActivityContext private val context: Context) : BasePagingAdapter<PhotoListItem, ItemGalleryPhotosBinding>(
    diffCallback = object : DiffUtil.ItemCallback<PhotoListItem>() {
        override fun areItemsTheSame(oldItem: PhotoListItem, newItem: PhotoListItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PhotoListItem, newItem: PhotoListItem): Boolean {
            return oldItem==newItem
        }

    }
) {

    private var onItemClicked: ((PhotoListItem) -> Unit) ? = null

    override fun createBinding(parent: ViewGroup): ItemGalleryPhotosBinding {
        return ItemGalleryPhotosBinding.inflate(LayoutInflater.from(parent.context),parent,false)
    }

    override fun bind(binding: ItemGalleryPhotosBinding, item: PhotoListItem, position: Int) {
        binding.apply {
            root.setOnClickListener { onItemClicked?.let { it(item) } }
            context.loadImage(ivGalleryPhoto,item.urls?.small)
        }
    }

    /*
    * when any photo is clicked
    * */
    fun onItemClicked(listener: ((PhotoListItem) -> Unit)) {
        onItemClicked = listener
    }

    /*
     * Select next/previous items in the list
     */
    fun getNextItem(item: PhotoListItem): PhotoListItem {
        return this.snapshot().items.get(min(this.snapshot().items.indexOf(item) + 1, this.snapshot().size))
    }

    fun getPreviousItem(item: PhotoListItem): PhotoListItem {
        return this.snapshot().items.get(max(this.snapshot().items.indexOf(item) - 1, 0))
    }
}