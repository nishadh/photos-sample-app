package com.nishadh.photosapp.util

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppPreferences @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private var preferences: SharedPreferences = context.getSharedPreferences(Companion.NAME, Companion.MODE)

    // list of app specific preferences
    private val SHOW_GRID_VIEW = Pair("showGridView", true)

    /**
     * SharedPreferences extension function, so we won't need to call edit()
    and apply()
     * ourselves on every SharedPreferences operation.
     */
    private inline fun SharedPreferences.edit(operation:
                                                  (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var showGridView: Boolean
        // custom getter to get a preference of a desired type, with a predefined default value
        get() = preferences.getBoolean(SHOW_GRID_VIEW.first, SHOW_GRID_VIEW.second)

        // custom setter to save a preference back to preferences file
        set(value) = preferences.edit {
            it.putBoolean(SHOW_GRID_VIEW.first, value)
        }

    companion object {
        private const val NAME = "SpinKotlin"
        private const val MODE = Context.MODE_PRIVATE
    }
}