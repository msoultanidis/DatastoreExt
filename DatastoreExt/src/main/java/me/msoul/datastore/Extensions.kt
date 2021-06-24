package me.msoul.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

val EnumPreference.preferenceKey get() = stringPreferencesKey(key)

/**
 * Returns the stored value of an [EnumPreference] or its default if the preference has not been set yet.
 * For use while transforming [DataStore.data].
 *
 * @throws [EmptyEnumPreferenceException] if the enum has no entries.
 */
inline fun <reified T> Preferences.getEnum(default: T = defaultOf()): T where T : Enum<T>, T : EnumPreference {
    val name = this[default.preferenceKey] ?: return default
    return enumValueOfOrNull<T>(name) ?: default
}

/**
 * Writes the new value of an [EnumPreference] to [MutablePreferences].
 * For use inside [DataStore.edit] or [DataStore.updateData]
 */
fun <T> MutablePreferences.setEnum(preference: T) where T : Enum<*>, T : EnumPreference {
    this[preference.preferenceKey] = preference.name
}

/**
 * Writes the new values of many [EnumPreference] to [MutablePreferences].
 * For use inside [DataStore.edit] or [DataStore.updateData]
 */
fun MutablePreferences.setAllEnums(vararg preferences: EnumPreference) {
    for (pref in preferences) {
        if (pref is Enum<*>) setEnum(pref)
    }
}

/**
 * Returns a [Flow] containing the stored value of an [EnumPreference] or its default if the preference has not been set yet.
 *
 * @throws [EmptyEnumPreferenceException] if the enum has no entries.
 */
inline fun <reified T> DataStore<Preferences>.getEnum(): Flow<T> where T : Enum<T>, T : EnumPreference {
    val default = defaultOf<T>()

    return data
        .catch {
            if (it is IOException) {
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences.getEnum(default)
        }
}

/**
 * Writes the new value of an [EnumPreference] to disk.
 *
 * @param preference The value to write.
 * @throws [IOException] when an exception is encountered when writing data to disk.
 */
suspend fun <T> DataStore<Preferences>.setEnum(preference: T) where T : Enum<T>, T : EnumPreference {
    edit { it.setEnum(preference) }
}

/**
 * Writes the values of many [EnumPreference] to disk.
 *
 * @param preferences The values to write.
 * @throws [IOException] when an exception is encountered when writing data to disk.
 */
suspend fun DataStore<Preferences>.setAllEnums(vararg preferences: EnumPreference) {
    edit { it.setAllEnums(*preferences) }
}
