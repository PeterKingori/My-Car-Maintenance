package com.pkndegwa.mycarmaintenance.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

/**
 * Data model for vehicle manufacturers.
 */
data class Manufacturer(
    @StringRes val nameResourceId: Int,
    @DrawableRes val logoResourceId: Int
)
