package com.smartlens.actividad3.ui.theme

import android.content.Context
import androidx.lifecycle.ViewModel
import com.smartlens.actividad3.data.PreferenceManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ThemeViewModel(context: Context) : ViewModel() {
    private val preferenceManager = PreferenceManager(context)

    private val _isDarkMode = MutableStateFlow(preferenceManager.isDarkMode())
    val isDarkMode: StateFlow<Boolean?> = _isDarkMode.asStateFlow()

    fun toggleTheme(isDark: Boolean) {
        preferenceManager.setThemeMode(isDark)
        _isDarkMode.value = isDark
    }
}
