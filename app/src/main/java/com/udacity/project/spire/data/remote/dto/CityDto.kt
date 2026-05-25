package com.udacity.project.spire.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * City data from the buildings API.
 */
data class CityDto(
    val id: Int,
    val name: String,
    @SerializedName("country_id")
    val countryId: Int
)
