package com.smartlens.actividad3.data

import android.content.Context
import android.content.SharedPreferences

class PreferenceManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    fun saveUser(email: String, password: String, name: String) {
        sharedPreferences.edit().apply {
            putString("user_email_$email", email)
            putString("user_password_$email", password)
            putString("user_name_$email", name)
            apply()
        }
    }

    fun getUserPassword(email: String): String? {
        return sharedPreferences.getString("user_password_$email", null)
    }

    fun getUserName(email: String): String? {
        return sharedPreferences.getString("user_name_$email", null)
    }

    fun setLoggedIn(email: String) {
        sharedPreferences.edit().putString("logged_in_user", email).apply()
    }

    fun getLoggedInUser(): String? {
        return sharedPreferences.getString("logged_in_user", null)
    }

    fun clearSession() {
        sharedPreferences.edit().remove("logged_in_user").apply()
    }

    fun setThemeMode(isDarkMode: Boolean) {
        sharedPreferences.edit().putBoolean("is_dark_mode", isDarkMode).apply()
    }

    fun isDarkMode(): Boolean? {
        return if (sharedPreferences.contains("is_dark_mode")) {
            sharedPreferences.getBoolean("is_dark_mode", false)
        } else {
            null
        }
    }
}
