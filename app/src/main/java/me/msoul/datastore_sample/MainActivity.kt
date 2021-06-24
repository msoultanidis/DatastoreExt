package me.msoul.datastore_sample

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
import me.msoul.datastore.defaultOf
import me.msoul.datastore.getEnum

class MainActivity : AppCompatActivity() {
    private var currentLanguage = defaultOf<Language>()
    private var syncingStatus = defaultOf<Syncing>()

    lateinit var textView: TextView
    lateinit var buttonSetLanguage: Button
    lateinit var buttonSetSyncing: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.textView)
        buttonSetLanguage = findViewById(R.id.button)
        buttonSetSyncing = findViewById(R.id.button2)

        buttonSetLanguage.setOnClickListener { showPreferenceDialog("Language", currentLanguage) }
        buttonSetSyncing.setOnClickListener { showPreferenceDialog("Syncing", syncingStatus) }

        // When collecting flows from UI, it's better to use the new repeatOnLifecycle() API
        // This code is for demonstration purposes only and is not safe for production
        lifecycleScope.launchWhenStarted {
            dataStore.getEnum<Language>().collect {
                currentLanguage = it
                textView.text = "The country code of the preferred language is [${currentLanguage.countryCode}]"
            }
        }

        lifecycleScope.launchWhenStarted {
            dataStore.getEnum<Syncing>().collect {
                syncingStatus = it
                buttonSetSyncing.text = "Syncing is $syncingStatus"
            }
        }
    }
}
