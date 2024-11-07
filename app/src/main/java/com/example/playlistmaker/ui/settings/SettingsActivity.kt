package com.example.playlistmaker.ui.settings

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.example.playlistmaker.R
import com.example.playlistmaker.presentation.SettingsActionImpl
import com.example.playlistmaker.ui.tracks.App


class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)

        val backBtn = findViewById<TextView>(R.id.home)
        val themeSwitcher = findViewById<SwitchCompat>(R.id.theme_switcher)
        val shareBtn = findViewById<Button>(R.id.share_btn)
        val supportBtn = findViewById<Button>(R.id.support_btn)
        val agreementBtn = findViewById<Button>(R.id.agreement_btn)

        backBtn.setOnClickListener {
            this.finish()
        }
        themeSwitcher.isChecked = (applicationContext as App).darkTheme

        themeSwitcher.setOnCheckedChangeListener { switcher, isChecked ->
            (applicationContext as App).switchTheme(isChecked)
        }

        shareBtn.setOnClickListener {
            startActivity(
                Intent.createChooser(
                    SettingsActionImpl().share(getString(R.string.share_message),
                        getString(R.string.share_message_subj)),
                    null)
            )
        }
        supportBtn.setOnClickListener {
            startActivity(SettingsActionImpl().sendEmail(
                arrayOf(getString(R.string.support_message_email)),
                getString(R.string.support_message_subj),
                getString(R.string.support_message_text),
                "mailto:"
            )
            )
        }
        agreementBtn.setOnClickListener {
            startActivity(SettingsActionImpl().sendUrl(getString(R.string.agreement_url)))
        }
    }
}