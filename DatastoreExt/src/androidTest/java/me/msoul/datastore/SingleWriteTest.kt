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
class SingleWriteTest {
    lateinit var dataStore: DataStore<Preferences>

    @Before
    fun prepare() {
        dataStore = InstrumentationRegistry.getInstrumentation().context.dataStore
    }

    @Test
    fun test() = runBlocking {
        val expected = Language.SPANISH

        dataStore.setEnum(expected)

        val actual = dataStore.getEnum<Language>().first()

        assertTrue("Preference was not set properly", actual == expected)
    }
}
