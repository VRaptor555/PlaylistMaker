package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.google.android.material.switchmaterial.SwitchMaterial


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
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.setType("text/plain")
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_message))
            shareIntent.putExtra(Intent.EXTRA_TITLE, getString(R.string.share_message_subj))
            startActivity(Intent.createChooser(shareIntent, null))
        }
        supportBtn.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SENDTO)
            shareIntent.data = Uri.parse("mailto:")
            shareIntent.putExtra(
                Intent.EXTRA_EMAIL,
                arrayOf(getString(R.string.support_message_email))
            )
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_message_subj))
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.support_message_text))
            startActivity(shareIntent)
        }
        agreementBtn.setOnClickListener {
            val browserIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.agreement_url)))
            startActivity(browserIntent)
        }
    }
}