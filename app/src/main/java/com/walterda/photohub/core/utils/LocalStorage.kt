package com.walterda.photohub.core.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.walterda.photohub.R


class LocalStorage(context: Context) {
    private val mContext: Context = context

    data class SavedAlbum(
        val id: String,
        val name: String
    )

    private fun getSharedPreferences(): SharedPreferences {
        return mContext.getSharedPreferences(mContext.getString(R.string.app_name), MODE_PRIVATE)
    }

    fun getCurrentPreferenceName(): String {
        val preferences = getSharedPreferences()
        return preferences.getString(mContext.getString(R.string.preferences_name), mContext.getString(R.string.default_name))!!
    }

    fun setCurrentPreferenceName(name: String) {
        val preferences = getSharedPreferences()
        val editor = preferences.edit()
        editor.putString(mContext.getString(R.string.preferences_name), name)
        editor.commit()
    }

    fun getCurrentPreferenceAlbum(): SavedAlbum? {
        val preferences = getSharedPreferences()
        val albumId = preferences.getString(mContext.getString(R.string.preferences_album_id), null)
        if (albumId == null)
            return null
        return SavedAlbum(albumId,
            preferences.getString(mContext.getString(R.string.preferences_album_name), "Album")!!
        )
    }

    fun setCurrentPreferenceName(id: String, name: String) {
        val preferences = getSharedPreferences()
        val editor = preferences.edit()
        editor.putString(mContext.getString(R.string.preferences_album_id), id)
        editor.putString(mContext.getString(R.string.preferences_album_name), name)
        editor.commit()
    }
}