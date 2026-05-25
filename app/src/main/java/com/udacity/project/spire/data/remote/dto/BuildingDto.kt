package com.udacity.project.spire.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.udacity.project.spire.domain.model.Building
import com.udacity.project.spire.domain.model.VisitStatus

/**
 * Building data from the buildings API.
 */
data class BuildingDto(
    val id: Int,
    val name: String,
    val city: CityDto,
    val country: CountryDto,
    @SerializedName("height_m")
    val heightMeters: Int,
    val floors: Int,
    @SerializedName("year_completed")
    val yearCompleted: Int,
    @SerializedName("architectural_style")
    val architecturalStyle: String,
    @SerializedName("image_url")
    val imageUrl: String,
    val description: String
)

/**
 * Convert API DTO to domain [Building] (default visit status for new buildings).
 */
fun BuildingDto.toDomainModel(): Building {
    return Building(
        id = id,
        name = name,
        city = city.name,
        country = country.name,
        heightMeters = heightMeters,
        floors = floors,
        yearCompleted = yearCompleted,
        architecturalStyle = architecturalStyle,
        imageUrl = imageUrl,
        description = description,
        visitStatus = VisitStatus.NOT_VISITED
    )
}
