package com.walterda.photohub.features

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.walterda.photohub.R

/**
 * Loads [SettingsFragment].
 */
class SettingsActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_browse_fragment, SettingsFragment())
                .commitNow()
        }
    }
}