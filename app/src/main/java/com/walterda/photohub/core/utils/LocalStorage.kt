package com.walterda.photohub.core.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.walterda.photohub.R


class LocalStorage(context: Context) {
    private val mContext: Context = context

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
}