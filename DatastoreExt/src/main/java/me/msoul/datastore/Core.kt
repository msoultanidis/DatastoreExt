package me.msoul.datastore

/**
 * Represents an [Enum] preference. Requires a [String] key.
 */
interface EnumPreference {
    val key: String
    val isDefault: Boolean
}

/**
 * Function used to delegate implementation of the [EnumPreference] interface.
 *
 * @param key A [String] key which will be used as the name of the preference. Must be unique.
 * @return Implementation of the [EnumPreference] interface.
 */
fun key(key: String): EnumPreference {
    return object : EnumPreference {
        override val key = key
        override val isDefault: Boolean = false
    }
}

/**
 * Returns an enum entry with specified name or null if no such entry exists.
 */
inline fun <reified T : Enum<T>> enumValueOfOrNull(value: String): T? {
    return runCatching { enumValueOf<T>(value) }.getOrNull()
}

/**
 * Returns the default entry of an [EnumPreference] or the first if a default has not been set.
 * @throws [EmptyEnumPreferenceException] if the enum has no entries.
 */
inline fun <reified T> defaultOf(): T where T : Enum<T>, T : EnumPreference {
    val values = enumValues<T>()
    return values.find { it.isDefault } ?: values.firstOrNull() ?: throw EmptyEnumPreferenceException(T::class.java.simpleName)
}

class EmptyEnumPreferenceException(enumName: String) : Exception("Enum $enumName has no entries and thus cannot be used as a preference.")
