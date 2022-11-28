package com.walterda.photohub.features.gallery.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.walterda.photohub.R
import com.walterda.photohub.core.base.BasePagingAdapter
import com.walterda.photohub.core.utils.LocalStorage
import com.walterda.photohub.core.utils.loadImage
import com.walterda.photohub.databinding.ItemAlbumBinding
import com.walterda.photohub.features.gallery.domain.models.AlbumListItem
import dagger.hilt.android.qualifiers.ActivityContext
import kotlinx.android.synthetic.main.item_album.view.*
import javax.inject.Inject

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
            return false //oldItem==newItem
        }

    }
) {
    private var mSelectedId: String? = null
    private var onItemClicked: ((AlbumListItem) -> Unit) ? = null

    override fun createBinding(parent: ViewGroup): ItemAlbumBinding {
        mSelectedId = LocalStorage(context).getCurrentPreferenceAlbum()?.id
        return ItemAlbumBinding.inflate(LayoutInflater.from(parent.context),parent,false)
    }

    override fun bind(binding: ItemAlbumBinding, item: AlbumListItem, position: Int) {
        binding.apply {
            root.setOnClickListener { onItemClicked?.let { it(item) } }
            root.ivAlbumTitle.setText(item.title)
            root.ivAlbumSubtitle.setText(String.format(context.getString(R.string.media_items), item.mediaItemsCount))
            root.ivAlbumSelected.isChecked = item.id.equals(mSelectedId)
            context.loadImage(ivAlbum, item.coverPhotoBaseUrl.toString())
        }
    }

    /*
    * when any photo is clicked
    * */
    fun onItemClicked(listener: ((AlbumListItem) -> Unit)) {
        onItemClicked = listener
    }

    fun setSelectedAlbum(albumId: String) {
        mSelectedId = albumId
    }
}