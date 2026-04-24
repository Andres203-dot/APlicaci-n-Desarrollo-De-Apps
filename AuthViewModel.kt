package com.smartlens.actividad3.ui.auth

import android.content.Context
import androidx.lifecycle.ViewModel
import com.smartlens.actividad3.R
import com.smartlens.actividad3.data.PreferenceManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthViewModel(context: Context) : ViewModel() {
    private val preferenceManager = PreferenceManager(context)
    private val appContext = context.applicationContext

    private val _isLoggedIn = MutableStateFlow(preferenceManager.getLoggedInUser() != null)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _userName = MutableStateFlow<String?>(
        preferenceManager.getLoggedInUser()?.let { preferenceManager.getUserName(it) }
    )
    val userName: StateFlow<String?> = _userName.asStateFlow()

    private val _userEmail = MutableStateFlow<String?>(preferenceManager.getLoggedInUser())
    val userEmail: StateFlow<String?> = _userEmail.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun register(name: String, email: String, password: String): Boolean {
        if (name.isBlank() || email.isBlank() || password.isBlank()) {
            _error.value = appContext.getString(R.string.error_all_fields_required)
            return false
        }
        preferenceManager.saveUser(email, password, name)
        _error.value = null
        return true
    }

    fun login(email: String, password: String): Boolean {
        if (email.isBlank() || password.isBlank()) {
            _error.value = appContext.getString(R.string.error_all_fields_required)
            return false
        }
        val savedPassword = preferenceManager.getUserPassword(email)
        return if (savedPassword == password) {
            preferenceManager.setLoggedIn(email)
            _isLoggedIn.value = true
            _userEmail.value = email
            _userName.value = preferenceManager.getUserName(email)
            _error.value = null
            true
        } else {
            _error.value = appContext.getString(R.string.error_invalid_credentials)
            false
        }
    }

    fun logout() {
        preferenceManager.clearSession()
        _isLoggedIn.value = false
        _userName.value = null
        _userEmail.value = null
    }

    fun clearError() {
        _error.value = null
    }
}
