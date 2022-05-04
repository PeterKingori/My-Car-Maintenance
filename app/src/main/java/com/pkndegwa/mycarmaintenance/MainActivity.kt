package com.pkndegwa.mycarmaintenance

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

        // Launch the VehicleRegistrationActivity on addVehicleButton click
        binding.addVehicleButton.setOnClickListener { vehicleRegistration() }
    }

    private fun vehicleRegistration() {
        val intent = Intent(this, VehicleRegistrationActivity::class.java)
        startActivity(intent)
    }
}