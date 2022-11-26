package com.walterda.photohub.features

import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.leanback.widget.Presenter
import com.walterda.photohub.R
import com.walterda.photohub.core.photos.Preferences

class SettingsPresenter : Presenter() {

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        val view = TextView(parent.context)
        view.layoutParams = ViewGroup.LayoutParams(
            GRID_ITEM_WIDTH,
            GRID_ITEM_HEIGHT
        )
        view.isFocusable = true
        view.isFocusableInTouchMode = true
        view.setBackgroundColor(ContextCompat.getColor(parent.context, R.color.default_background))
        view.setTextColor(Color.WHITE)
        view.gravity = Gravity.CENTER
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, item: Any) {
        val preference = item as Preferences
        (viewHolder.view as TextView).text = String.format("%s\n%s", preference.title, preference.description)
    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder) {

    }

    companion object {
        private val GRID_ITEM_WIDTH = 200
        private val GRID_ITEM_HEIGHT = 200
    }
}