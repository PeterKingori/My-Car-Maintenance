package com.pkndegwa.mycarmaintenance

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.pkndegwa.mycarmaintenance.databinding.ActivityVehicleRegistrationBinding

class VehicleRegistrationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVehicleRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Setup view binding
        binding = ActivityVehicleRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}