package com.walterda.photohub.features.settings.presentation

import android.app.Activity
import android.content.Intent
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
import androidx.core.app.ActivityCompat
import com.google.android.gms.auth.api.signin.GoogleSignInAccount

import com.walterda.photohub.R
import com.walterda.photohub.core.photos.GoogleIdentity
import com.walterda.photohub.core.photos.PreferenceId
import com.walterda.photohub.core.photos.Preferences
import com.walterda.photohub.core.utils.Constants
import com.walterda.photohub.core.utils.LocalStorage
import kotlinx.android.synthetic.main.fragment_gallery.*

/**
 * Loads a grid of cards with movies to browse.
 */
class SettingsFragment : BrowseSupportFragment() {

    private var mDefaultBackground: Drawable? = null
    private var mLoggedAccount: GoogleSignInAccount? = null

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.i(TAG, "onCreate")
        super.onActivityCreated(savedInstanceState)

        setUpGoogle()

        setupUIElements()

        loadRows()

        setupEventListeners()
    }

    private fun setUpGoogle() {
        mLoggedAccount = GoogleIdentity(context!!).getLastSignIn()
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
        val listRowAdapter = ArrayObjectAdapter(GoogleInfoPresenter())

        listRowAdapter.add(Preferences(PreferenceId.GOOGLE, "google", "google"))
        val header = HeaderItem(0, getString(R.string.google_settings))
        rowsAdapter.add(ListRow(header, listRowAdapter))

        val gridHeader = HeaderItem(1, getString(R.string.preferences))

        val mGridPresenter = SettingsPresenter()
        val gridRowAdapter = ArrayObjectAdapter(mGridPresenter)
        gridRowAdapter.add(Preferences(PreferenceId.NAME, getString(R.string.name_title), LocalStorage(context!!).getCurrentPreferenceName()))
        gridRowAdapter.add(Preferences(PreferenceId.ALBUM, getString(R.string.album), getString(R.string.default_album)))
        rowsAdapter.add(ListRow(gridHeader, gridRowAdapter))

        adapter = rowsAdapter
    }

    private fun setupEventListeners() {
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
            Log.w(TAG, "CLICKED " + item.toString())
            val preference = item as Preferences
            when (preference.id) {
                PreferenceId.NAME -> {
                    val intent = Intent(context!!, NamePopup::class.java)
                    startActivity(intent)
                }
                PreferenceId.ALBUM -> {
                    val intent = Intent(context!!, AlbumChoicePopup::class.java)
                    startActivity(intent)
                }
                PreferenceId.GOOGLE -> {
                    if (mLoggedAccount == null) {
                        val signInIntent: Intent =
                            GoogleIdentity(context!!).getSignInClient().getSignInIntent()
                        ActivityCompat.startActivityForResult(
                            context as Activity,
                            signInIntent,
                            Constants.GOOG_RC_SIGN_IN,
                            null
                        )
                    } else {
                        GoogleIdentity(context!!).getSignInClient().signOut().addOnCompleteListener {
                            setUpGoogle()
                            loadRows()
                        }
                    }
                }
            }
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

    override fun onResume() {
        super.onResume()
        loadRows()
    }

    companion object {
        private val TAG = "SettingsFragment"
    }
}