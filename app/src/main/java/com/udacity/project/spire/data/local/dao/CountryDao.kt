package com.udacity.project.spire.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.udacity.project.spire.data.local.entity.CountryEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Country entity.
 * Provides essential database operations for countries.
 */
@Dao
interface CountryDao {

    @Query("SELECT * FROM countries ORDER BY name ASC")
    fun getAllCountries(): Flow<List<CountryEntity>>

    @Query("SELECT * FROM countries WHERE name = :name")
    suspend fun getCountryByName(name: String): CountryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCountry(country: CountryEntity): Long

    /**
     * Transactional get-or-insert for a country by name.
     * Used when importing buildings from the API.
     */
    @Transaction
    suspend fun getOrInsertCountry(name: String, code: String): Int {
        getCountryByName(name)?.let { return it.id }
        return insertCountry(CountryEntity(name = name, code = code)).toInt()
    }
}
