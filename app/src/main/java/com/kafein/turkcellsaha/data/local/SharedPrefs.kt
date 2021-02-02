package com.kafein.turkcellsaha.data.local

import android.content.Context
import android.content.SharedPreferences

object SharedPrefs {
    private const val NAME = "GoArena"
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences
    private const val KEY_TOKEN = "token"
    private const val KEY_USER_ID = "user_id"

    fun init(context: Context) {
        preferences = context.getSharedPreferences(NAME, MODE)
    }


    fun saveToken(value: String) {
        preferences.edit().putString(KEY_TOKEN, value).apply()
    }

    fun readToken(): String? {
        return preferences.getString(KEY_TOKEN, "")
    }

    fun saveUserId(value : String){
        preferences.edit().putString(KEY_USER_ID, value).apply()
    }

    fun readUserId(): String? {
        return preferences.getString(KEY_USER_ID, "")
    }
}