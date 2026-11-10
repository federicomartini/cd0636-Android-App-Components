package com.udacity.project.spire.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.udacity.project.spire.data.local.entity.CityEntity

/**
 * Data Access Object for City entity.
 * Provides get-or-insert operations used during API data import.
 */
@Dao
interface CityDao {

    @Query("SELECT * FROM cities WHERE name = :name AND countryId = :countryId")
    suspend fun getCityByNameAndCountry(name: String, countryId: Int): CityEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCity(city: CityEntity): Long

    /**
     * Transactional get-or-insert for a city by name and country.
     * Used when importing buildings from the API.
     */
    @Transaction
    suspend fun getOrInsertCity(name: String, countryId: Int): Int {
        getCityByNameAndCountry(name, countryId)?.let { return it.id }
        return insertCity(CityEntity(name = name, countryId = countryId)).toInt()
    }
}