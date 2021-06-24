package me.msoul.datastore

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DefaultOfTest {
    @Test
    fun test() {
        assertTrue("Default pref has not been set properly", defaultOf<Language>() == Language.ENGLISH)
        assertTrue(
            "defaultOf does not fallback to the first entry",
            defaultOf<LanguageWithoutDefault>() == LanguageWithoutDefault.values().first()
        )
        assertTrue(
            "defaultOf does not throw exception for empty enums.",
            runCatching { defaultOf<EmptyEnum>() }.getOrNull() == null
        )
    }
}
