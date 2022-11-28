package com.walterda.photohub.features.settings.presentation

import android.os.Bundle
import androidx.leanback.app.DetailsSupportFragment
import androidx.leanback.app.DetailsSupportFragmentBackgroundController

/**
 * A wrapper fragment for leanback details screens.
 * It shows a detailed view of video and its metadata plus related videos.
 */
class SettingsDetailsFragment : DetailsSupportFragment() {

    private lateinit var mDetailsBackground: DetailsSupportFragmentBackgroundController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mDetailsBackground = DetailsSupportFragmentBackgroundController(this)

    }

    companion object {
        private val TAG = "SettingsDetailsFragment"
    }
}