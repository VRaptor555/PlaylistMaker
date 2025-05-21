package com.example.playlistmaker.main.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostController = supportFragmentManager.findFragmentById(R.id.mainContainerView)
        val navController = navHostController?.findNavController()
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        navController?.let {
            bottomNavigationView.setupWithNavController(it)
            it.addOnDestinationChangedListener{ _, destination, _ ->
                when(destination.id) {
                    R.id.playerActivity,
                    R.id.playlistAddFragment,
                    R.id.playlistDetailFragment,
                    R.id.playlistEditFragment -> {
                        bottomNavigationView.visibility = View.GONE
                        binding.divLine.visibility = View.GONE
                    }
                    else -> {
                        bottomNavigationView.visibility = View.VISIBLE
                        binding.divLine.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

}