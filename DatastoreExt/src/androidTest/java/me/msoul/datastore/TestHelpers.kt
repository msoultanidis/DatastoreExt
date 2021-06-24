package me.msoul.datastore

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore by preferencesDataStore("test_preferences")

enum class DarkMode : EnumPreference by key("dark_mode") {
    ENABLED,
    DISABLED { override val isDefault = true },
}

enum class Language : EnumPreference by key("language") {
    GREEK,
    ENGLISH { override val isDefault = true },
    SPANISH,
    TURKISH,
}

enum class LanguageWithoutDefault : EnumPreference by key("language_no_default") {
    GREEK,
    ENGLISH,
    SPANISH,
    TURKISH,
}

enum class EmptyEnum : EnumPreference by key("empty_enum")
