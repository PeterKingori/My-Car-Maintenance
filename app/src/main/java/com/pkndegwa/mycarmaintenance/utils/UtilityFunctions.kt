package com.pkndegwa.mycarmaintenance.utils

import androidx.core.widget.doOnTextChanged
import com.google.android.material.textfield.TextInputLayout

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