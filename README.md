# DatastoreExt

DatastoreExt is a small library on top of AndroidX DataStore which makes persisting Enum preferences a bit easier by reducing boilerplate code.


## Get the dependency
### Step 1. Add the JitPack repository to your build file
```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
### Step 2. Add the dependency
```
dependencies {
    implementation 'me.msoul:datastoreext:1.0.0'
}
```


## Define your enums

```kotlin
enum class Language(val countryCode: String) : EnumPreference by key("language") {
    GREEK ("gr"),
    ENGLISH ("en") { override val isDefault = true },
    ITALIAN ("it"),
}

enum class Syncing : EnumPreference by key("syncing") {
    ENABLED,
    DISABLED { override val isDefault = true }, // Setting a default is optional
}
```

## Store enum values

### One at a time

```kotlin
val language = Language.GREEK
dataStore.setEnum(language) 

// Or you can do the following if you want to set other preferences too at the same time
dataStore.edit { preferences ->
    preferences[SOME_KEY] = "SOME_VALUE"
    preferences[OTHER_KEY] = "OTHER_VALUE"
    preferences.setEnum(language)
}
```

### Multiple at the same time

```kotlin
val language = Language.GREEK
// You can use the defaultOf() function to retrieve the enum constant you have set as the default.
// It's useful when you want to reset preferences back to their default
val syncingStatus = defaultOf<Syncing>() 
dataStore.setEnums(language, syncingStatus) 

// Or you can do the following if you want to set other preferences too at the same time
dataStore.edit { preferences ->
    preferences[SOME_KEY] = "SOME_VALUE"
    preferences[OTHER_KEY] = "OTHER_VALUE"
    preferences.setEnums(language, syncingStatus)
}
```

## Retrieve a `Flow` which emits the latest value
```kotlin
dataStore.getEnum<Language>().collect {
    textView.text = "The country code of the preferred language is [${it.countryCode}]"
}

// Or you can do the following if you want to retrieve multiple preferences from the same flow
dataStore.data.map { preferences ->
    AppPreferences( // Some data class
        preferences[SOME_KEY],
        preferences[OTHER_KEY],
        preferences.getEnum<Language>(),
    )
}
```

## Example: Create a dialog that can be used to set multiple preferences
```kotlin
inline fun <reified T> Activity.showPreferenceDialog(
    title: String, // or a string resource ID
    selected: T, // the enum entry that should be selected by default when showing the dialog
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
```


# License
```
Copyright (C) 2021 Michael Soultanidis

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```