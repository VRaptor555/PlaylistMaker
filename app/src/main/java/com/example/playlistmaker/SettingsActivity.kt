package com.example.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity


class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)

        val backBtn = findViewById<Button>(R.id.home)
        val shareBtn = findViewById<Button>(R.id.share_btn)
        val supportBtn = findViewById<Button>(R.id.support_btn)
        val agreementBtn = findViewById<Button>(R.id.agreement_btn)

        backBtn.setOnClickListener {
            this.finish()
        }

        shareBtn.setOnClickListener {
            Toast.makeText(this@SettingsActivity, "Поделимся, скоро!", Toast.LENGTH_SHORT).show()
        }
        supportBtn.setOnClickListener {
            Toast.makeText(this@SettingsActivity, "Помогите!!!", Toast.LENGTH_SHORT).show()
        }
        agreementBtn.setOnClickListener {
            Toast.makeText(this@SettingsActivity, "Вы должны нам денег!", Toast.LENGTH_SHORT).show()
        }
    }
}