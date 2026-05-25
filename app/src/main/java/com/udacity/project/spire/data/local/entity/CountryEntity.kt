package com.udacity.project.spire.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Room entity representing a country.
 * A country can have many cities.
 */
@Entity(
    tableName = "countries",
    indices = [Index(value = ["name"], unique = true)]
)
data class CountryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val code: String
)
