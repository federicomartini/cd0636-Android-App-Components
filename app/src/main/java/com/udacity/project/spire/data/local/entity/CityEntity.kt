package com.udacity.project.spire.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Room entity representing a city.
 * A city belongs to a country and can have many buildings.
 */
@Entity(
    tableName = "cities",
    foreignKeys = [
        ForeignKey(
            entity = CountryEntity::class,
            parentColumns = ["id"],
            childColumns = ["countryId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["countryId"]),
        Index(value = ["name", "countryId"], unique = true)
    ]
)
data class CityEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val countryId: Int
)
