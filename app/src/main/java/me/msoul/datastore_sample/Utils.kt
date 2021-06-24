package me.msoul.datastore_sample

import android.app.Activity
import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.runBlocking
import me.msoul.datastore.EnumPreference
import me.msoul.datastore.key
import me.msoul.datastore.setEnum

enum class Language(val countryCode: String) : EnumPreference by key("language") {
    GREEK("gr"),
    ENGLISH("en") { override val isDefault = true },
    ITALIAN("it"),
}

enum class Syncing : EnumPreference by key("syncing") {
    ENABLED,
    DISABLED { override val isDefault = true },
}

val Context.dataStore by preferencesDataStore("prefs")

inline fun <reified T> Activity.showPreferenceDialog(
    title: String, // or a string resource ID
    selected: T,
) where T : Enum<T>, T : EnumPreference {
    val enumValues = enumValues<T>()
    val selectedIndex = enumValues.indexOf(selected)
    val items = enumValues
        .map { it.name } // You could also store the string resource ID of each enum entry inside via its constructor
        .toTypedArray()

    MaterialAlertDialogBuilder(this)
        .setTitle(title)
        .setSingleChoiceItems(items, selectedIndex) { dialogInterface, which ->
            dialogInterface.dismiss()

            // Ideally you would want to pass an onClick lambda to this function and handle this in the ViewModel
            // Don't do the following in production!
            runBlocking { dataStore.setEnum(enumValues[which]) }
        }
        .setPositiveButton("Done") { dialogInterface, i ->
            dialogInterface.dismiss()
        }
        .show()
}
