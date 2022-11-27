package com.walterda.photohub.features.settings.presentation

import android.util.Log
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.leanback.widget.Presenter
import com.walterda.photohub.R
import com.walterda.photohub.core.photos.GoogleIdentity
import com.walterda.photohub.core.photos.GoogleInfoView
import kotlin.properties.Delegates


/**
 * A CardPresenter is used to generate Views and bind Objects to them on demand.
 * It contains an ImageCardView.
 */
class GoogleInfoPresenter : Presenter() {
    private var sSelectedBackgroundColor: Int by Delegates.notNull()
    private var sDefaultBackgroundColor: Int by Delegates.notNull()

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        Log.d(TAG, "onCreateViewHolder")

        sDefaultBackgroundColor = ContextCompat.getColor(parent.context, R.color.default_background)
        sSelectedBackgroundColor =
            ContextCompat.getColor(parent.context, R.color.selected_background)

        Log.w(TAG, "GOOGLE INFO TO FOLLOW:")
        Log.w(TAG, GoogleIdentity(parent.context).getLastSignIn().toString())


        val cardView = GoogleInfoView(parent.context, null)
        return ViewHolder(cardView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, item: Any) {
        Log.d(TAG, "onBindViewHolder")
    }

    override fun onUnbindViewHolder(viewHolder: Presenter.ViewHolder) {
        Log.d(TAG, "onUnbindViewHolder")
    }

    companion object {
        private val TAG = "GoogleInfoPresenter"
    }
}