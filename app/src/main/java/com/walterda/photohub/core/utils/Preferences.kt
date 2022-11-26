package com.walterda.photohub.core.photos

enum class PreferenceId {
    NAME, ALBUM
}

data class Preferences(
    val id: PreferenceId,
    val title: String,
    val description: String
)
