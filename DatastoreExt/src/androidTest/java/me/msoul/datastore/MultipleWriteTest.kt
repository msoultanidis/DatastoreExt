package me.msoul.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MultipleWriteTest {
    lateinit var dataStore: DataStore<Preferences>

    @Before
    fun prepare() {
        dataStore = InstrumentationRegistry.getInstrumentation().context.dataStore
    }

    @Test
    fun test() = runBlocking {
        val expectedLanguage = Language.TURKISH
        val expectedDarkMode = DarkMode.ENABLED

        dataStore.setAllEnums(expectedDarkMode, expectedLanguage, LanguageWithoutDefault.GREEK)

        val actualLanguage = dataStore.getEnum<Language>().first()
        val actualLanguage2 = dataStore.getEnum<LanguageWithoutDefault>().first()
        val actualDarkMode = dataStore.getEnum<DarkMode>().first()

        assertTrue("Language without default was not set properly", actualLanguage2 == LanguageWithoutDefault.GREEK)
        assertTrue("Language was not set properly", actualLanguage == expectedLanguage)
        assertTrue("DarkMode was not set properly", actualDarkMode == expectedDarkMode)

        assertTrue(true)
    }
}
