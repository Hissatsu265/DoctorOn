package com.example.doctoron.Objects

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context:Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    companion object {
        private const val KEY_USERNAME = "username"
        private const val KEY_PASSWORD = "password"
    }

    fun saveLoginDetails(username: String, password: String) {
        editor.putString(KEY_USERNAME, username)
        editor.putString(KEY_PASSWORD, password)
        editor.apply()
    }

    fun getSavedUsername(): String? {
        return sharedPreferences.getString(KEY_USERNAME, null)
    }

    fun getSavedPassword(): String? {
        return sharedPreferences.getString(KEY_PASSWORD, null)
    }

    fun clearLoginDetails() {
        editor.remove(KEY_USERNAME)
        editor.remove(KEY_PASSWORD)
        editor.apply()
    }

}