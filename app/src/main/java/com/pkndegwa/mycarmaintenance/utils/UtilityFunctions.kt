package com.pkndegwa.mycarmaintenance.utils

/**
 * Public function that checks if a string value is blank or not.
 */
fun isEntryValid(propertyValue: String): Boolean {
    if (propertyValue.isBlank()) {
        return false
    }
    return true
}