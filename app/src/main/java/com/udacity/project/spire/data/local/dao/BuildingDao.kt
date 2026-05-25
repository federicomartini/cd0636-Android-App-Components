package com.udacity.project.spire.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.udacity.project.spire.data.local.entity.BuildingEntity
import com.udacity.project.spire.data.local.entity.BuildingWithDetails
import com.udacity.project.spire.data.local.entity.VisitStatusEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Building entity.
 * Defines database operations for the buildings table.
 *
 * TODO #10-21: Implement BuildingDao with Room queries (12 methods)
 *
 * This DAO has 12 methods demonstrating various Room patterns:
 * - Basic queries with Flow
 * - Paging3 integration with PagingSource
 * - JOIN queries across multiple tables
 * - Filtering queries
 * - Insert/Update/Delete operations
 * - Aggregation queries (COUNT, SUM)
 *
 * CRITICAL CONCEPTS:
 *
 * 1. @Transaction annotation:
 *    - Required for queries that load relations (@Embedded, @Relation)
 *    - Ensures all related data is loaded atomically
 *    - Use with getAllBuildings(), getBuildingsPagingSource(), getBuildingById(), etc.
 *
 * 2. Flow vs suspend:
 *    - Flow: Use for queries that need reactive updates (UI observes changes)
 *    - suspend: Use for one-time operations (insert, update, delete, counts)
 *
 * 3. PagingSource:
 *    - Room automatically creates PagingSource implementation
 *    - Just add @Query annotation, Room handles pagination
 *    - Used with Paging3 library for efficient list pagination
 *
 * 4. JOIN queries:
 *    - Use SQL JOIN to query across related tables
 *    - Example: Get buildings in a specific country requires joining 3 tables
 *    - Always alias tables (AS b, AS c, AS co) for clarity
 *
 * 5. Aggregation queries:
 *    - COUNT(*): Count rows
 *    - SUM(column): Sum numeric values
 *    - COUNT(DISTINCT column): Count unique values
 *    - Aggregate functions return nullable types if no rows exist
 *
 * Required imports (add these as you implement):
 * - androidx.room.Insert
 * - androidx.room.OnConflictStrategy
 * - androidx.room.Query
 * - androidx.room.Transaction
 * - androidx.room.Update
 */
@Dao
interface BuildingDao {

    /**
     * Get all buildings with their city and country details.
     *
     * @return Flow of all buildings with full details
     */
    @Transaction
    @Query("SELECT * FROM buildings ORDER BY id ASC")
    fun getAllBuildings(): Flow<List<BuildingWithDetails>>

    /**
     * Get all buildings as a PagingSource for Paging 3.
     * Core query for the main buildings list screen.
     *
     * @return PagingSource of buildings with full details
     */
    @Transaction
    @Query("SELECT * FROM buildings ORDER BY id ASC")
    fun getBuildingsPagingSource(): PagingSource<Int, BuildingWithDetails>

    /**
     * Get a specific building by ID with city and country details.
     * Used in BuildingDetailScreen.
     *
     * @param id The building ID to query
     * @return Flow of the building with details or null if not found
     */
    @Transaction
    @Query("SELECT * FROM buildings WHERE id = :id")
    fun getBuildingById(id: Int): Flow<BuildingWithDetails?>

    /**
     * Get all buildings in a specific country by country name.
     *
     * @param countryName The country name to filter by
     * @return Flow of buildings in that country
     */
    @Transaction
    @Query(
        """
        SELECT b.* FROM buildings AS b
        INNER JOIN cities AS c ON b.cityId = c.id
        INNER JOIN countries AS co ON c.countryId = co.id
        WHERE co.name = :countryName
        ORDER BY b.id ASC
        """
    )
    fun getBuildingsByCountry(countryName: String): Flow<List<BuildingWithDetails>>

    /**
     * Get all buildings with a specific visit status.
     * Used in MyVisitsScreen.
     *
     * @param status The visit status to filter by
     * @return Flow of buildings with that status
     */
    @Transaction
    @Query("SELECT * FROM buildings WHERE visitStatus = :status ORDER BY id ASC")
    fun getBuildingsByVisitStatus(status: VisitStatusEntity): Flow<List<BuildingWithDetails>>

    /**
     * Insert multiple buildings, replacing on conflict.
     * Called by RemoteMediator after fetching data from API.
     *
     * @param buildings List of buildings to insert
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBuildings(buildings: List<BuildingEntity>)

    /**
     * Update an existing building (e.g. visit status from detail screen).
     *
     * @param building The building to update
     */
    @Update
    suspend fun updateBuilding(building: BuildingEntity)

    /**
     * Clear all buildings from the database.
     * Used during REFRESH; call in same transaction as clearRemoteKeys().
     */
    @Query("DELETE FROM buildings")
    suspend fun clearBuildings()

    /**
     * Get the total count of buildings.
     * Used in StatisticsScreen.
     *
     * @return Total number of buildings
     */
    @Query("SELECT COUNT(*) FROM buildings")
    suspend fun getBuildingCount(): Int

    /**
     * Get the count of buildings with a specific visit status.
     * Used in StatisticsScreen for visited and bucket list counts.
     *
     * @param status The visit status to count
     * @return Number of buildings with that status
     */
    @Query("SELECT COUNT(*) FROM buildings WHERE visitStatus = :status")
    suspend fun getCountByStatus(status: VisitStatusEntity): Int

    /**
     * Get the count of unique countries with buildings matching visit status.
     * Used in StatisticsScreen for "Countries Explored".
     *
     * @param status The visit status to filter by (default: VISITED)
     * @return Number of countries with matching buildings
     */
    @Query(
        """
        SELECT COUNT(DISTINCT co.id) FROM countries AS co
        INNER JOIN cities AS c ON co.id = c.countryId
        INNER JOIN buildings AS b ON c.id = b.cityId
        WHERE b.visitStatus = :status
        """
    )
    suspend fun getVisitedCountriesCount(status: VisitStatusEntity = VisitStatusEntity.VISITED): Int

    /**
     * Get the total height of all buildings with a specific visit status.
     * Used in StatisticsScreen for "Total Meters Climbed".
     *
     * @param status The visit status to filter by (default: VISITED)
     * @return Sum of heights in meters, or null if none
     */
    @Query("SELECT SUM(heightMeters) FROM buildings WHERE visitStatus = :status")
    suspend fun getTotalMetersClimbed(status: VisitStatusEntity = VisitStatusEntity.VISITED): Int?
}
