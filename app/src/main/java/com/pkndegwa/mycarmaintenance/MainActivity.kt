package com.pkndegwa.mycarmaintenance

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pkndegwa.mycarmaintenance.databinding.ActivityMainBinding

/**
 * This activity is for the welcome page.
 */
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set up view binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}