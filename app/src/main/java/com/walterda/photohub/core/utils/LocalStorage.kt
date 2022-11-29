package com.walterda.photohub.core.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.walterda.photohub.R
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class LocalStorage(context: Context) {
    private val mContext: Context = context

    data class SavedAlbum(
        val id: String,
        val name: String
    )

    companion object {
        private val FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    }

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

    fun setCurrentPreferenceAlbum(id: String, name: String) {
        val preferences = getSharedPreferences()
        val editor = preferences.edit()
        editor.putString(mContext.getString(R.string.preferences_album_id), id)
        editor.putString(mContext.getString(R.string.preferences_album_name), name)
        editor.commit()
    }

    fun deleteCurrentPreferenceAlbum() {
        val preferences = getSharedPreferences()
        val editor = preferences.edit()
        editor.remove(mContext.getString(R.string.preferences_album_id))
        editor.remove(mContext.getString(R.string.preferences_album_name))
        editor.commit()
    }

    fun getCurrentPreferenceToken(): String? {
        val preferences = getSharedPreferences()
        return preferences.getString(mContext.getString(R.string.preferences_token), null)
    }

    fun getCurrentPreferenceTokenExpiration(): LocalDateTime? {
        val preferences = getSharedPreferences()
        val expiration_string = preferences.getString(mContext.getString(R.string.preferences_token_expiration), null)
        if (expiration_string == null) {
            return expiration_string
        }
        return LocalDateTime.parse(expiration_string, FORMATTER)
    }

    fun setCurrentPreferenceToken(token: String, expiration: LocalDateTime) {
        val preferences = getSharedPreferences()
        val editor = preferences.edit()
        editor.putString(mContext.getString(R.string.preferences_token), token)
        editor.putString(mContext.getString(R.string.preferences_token_expiration), expiration.format(
            FORMATTER))
        editor.commit()
    }

    fun deleteCurrentPreferenceToken() {
        val preferences = getSharedPreferences()
        val editor = preferences.edit()
        editor.remove(mContext.getString(R.string.preferences_token))
        editor.remove(mContext.getString(R.string.preferences_token_expiration))
        editor.commit()
    }
}