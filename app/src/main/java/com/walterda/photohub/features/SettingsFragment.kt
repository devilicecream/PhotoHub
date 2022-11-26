package com.walterda.photohub.features

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.HeaderItem
import androidx.leanback.widget.ListRow
import androidx.leanback.widget.ListRowPresenter
import androidx.leanback.widget.OnItemViewClickedListener
import androidx.leanback.widget.OnItemViewSelectedListener
import androidx.leanback.widget.Presenter
import androidx.leanback.widget.Row
import androidx.leanback.widget.RowPresenter
import androidx.core.content.ContextCompat
import android.util.Log
import android.widget.Toast

import com.walterda.photohub.R
import com.walterda.photohub.core.photos.Preferences
import com.walterda.photohub.core.utils.LocalStorage

/**
 * Loads a grid of cards with movies to browse.
 */
class SettingsFragment : BrowseSupportFragment() {

    private var mDefaultBackground: Drawable? = null

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.i(TAG, "onCreate")
        super.onActivityCreated(savedInstanceState)

        setupUIElements()

        loadRows()

        setupEventListeners()
    }


    private fun setupUIElements() {
        title = getString(R.string.browse_title)
        // background
        mDefaultBackground = ContextCompat.getDrawable(context!!, R.drawable.default_background)
        // over title
        headersState = HEADERS_ENABLED
        isHeadersTransitionOnBackEnabled = true

        // set fastLane (or headers) background color
        brandColor = ContextCompat.getColor(context!!, R.color.pinknonna)
        // set search icon color
        searchAffordanceColor = ContextCompat.getColor(context!!, R.color.search_opaque)
    }

    private fun loadRows() {
        val rowsAdapter = ArrayObjectAdapter(ListRowPresenter())
        val googlePresenter = GoogleInfoPresenter()

        val listRowAdapter = ArrayObjectAdapter(googlePresenter)
        listRowAdapter.add("test")
        val header = HeaderItem(0, getString(R.string.google_settings))
        rowsAdapter.add(ListRow(header, listRowAdapter))

        val gridHeader = HeaderItem(1, getString(R.string.preferences))

        val mGridPresenter = SettingsPresenter()
        val gridRowAdapter = ArrayObjectAdapter(mGridPresenter)
        val name = LocalStorage(context!!).getCurrentPreferenceName()
        gridRowAdapter.add(Preferences(getString(R.string.name_title), name))
        gridRowAdapter.add(Preferences(getString(R.string.album), getString(R.string.default_album)))
        rowsAdapter.add(ListRow(gridHeader, gridRowAdapter))

        adapter = rowsAdapter
    }

    private fun setupEventListeners() {
        setOnSearchClickedListener {
            Toast.makeText(context!!, "Implement your own in-app search", Toast.LENGTH_LONG)
                .show()
        }

        onItemViewClickedListener = ItemViewClickedListener()
        onItemViewSelectedListener = ItemViewSelectedListener()
    }

    private inner class ItemViewClickedListener : OnItemViewClickedListener {
        override fun onItemClicked(
            itemViewHolder: Presenter.ViewHolder,
            item: Any,
            rowViewHolder: RowPresenter.ViewHolder,
            row: Row
        ) {
            Log.w(TAG, "Clicked " + item.toString())
        }
    }

    private inner class ItemViewSelectedListener : OnItemViewSelectedListener {
        override fun onItemSelected(
            itemViewHolder: Presenter.ViewHolder?, item: Any?,
            rowViewHolder: RowPresenter.ViewHolder, row: Row
        ) {
            Log.w(TAG, "SELECTED " + item.toString())
        }
    }

    companion object {
        private val TAG = "SettingsFragment"
    }
}