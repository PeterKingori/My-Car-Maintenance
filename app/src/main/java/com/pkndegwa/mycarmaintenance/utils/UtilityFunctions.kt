package com.pkndegwa.mycarmaintenance.utils

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.widget.doOnTextChanged
import com.google.android.material.textfield.TextInputLayout
import com.pkndegwa.mycarmaintenance.R

/**
 * Public function that checks if a string value is blank or not.
 */
private fun isInputValid(propertyValue: String): Boolean {
    if (propertyValue.isBlank()) {
        return false
    }
    return true
}

/**
 * Checks if the text input fields have been filled.
 */
fun isEntryValid(view: TextInputLayout): Boolean {
    return if (!isInputValid(view.editText?.text.toString())) {
        setError(view)
        removeError(view)
        false
    } else {
        true
    }
}

/**
 * Sets the text field error status.
 */
private fun setError(view: TextInputLayout) {
    view.isErrorEnabled = true
    view.error = "Fill in this field."
}

/**
 * Removes the text field error stats.
 */
private fun removeError(view: TextInputLayout) {
    view.editText?.doOnTextChanged { _, _, _, _ ->
        view.isErrorEnabled = false
        view.error = null
    }
}

/**
 * Change default image shown depending on type of vehicle when user doesn't select own image.
 */
fun changeVehicleTypeImage(context: Context, vehicleType: String, resize: Boolean): Drawable? {
    return when (vehicleType) {
        "Car", "car" -> AppCompatResources.getDrawable(context, R.drawable.generic_car)
        "Motorcycle", "motorcycle" -> AppCompatResources.getDrawable(context, R.drawable.generic_motorbike)
        "Bus", "bus" -> {
            if (resize)
                AppCompatResources.getDrawable(context, R.drawable.generic_bus_resized)
            else
                AppCompatResources.getDrawable(context, R.drawable.generic_bus)
        }
        "Truck", "truck" -> AppCompatResources.getDrawable(context, R.drawable.generic_lorry)
        else -> AppCompatResources.getDrawable(context, R.drawable.generic_car)
    }
}