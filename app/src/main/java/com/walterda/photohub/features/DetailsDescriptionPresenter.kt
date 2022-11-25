package com.walterda.photohub.features

import androidx.leanback.widget.AbstractDetailsDescriptionPresenter

class DetailsDescriptionPresenter : AbstractDetailsDescriptionPresenter() {

    override fun onBindDescription(
        viewHolder: AbstractDetailsDescriptionPresenter.ViewHolder,
        item: Any
    ) {
//        val movie = item as Movie

        viewHolder.title.text = "PARRUZZO"
        viewHolder.subtitle.text = "GIACCHINO"
//        viewHolder.body.text = movie.description
    }
}