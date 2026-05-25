package com.udacity.project.spire.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.project.spire.data.local.entity.BuildingRemoteKeys

/**
 * Data Access Object for BuildingRemoteKeys.
 * Manages pagination state for Paging3 RemoteMediator.
 */
@Dao
interface BuildingRemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKeys: List<BuildingRemoteKeys>)

    @Query("SELECT * FROM building_remote_keys WHERE buildingId = :buildingId")
    suspend fun remoteKeysByBuildingId(buildingId: Int): BuildingRemoteKeys?

    @Query("DELETE FROM building_remote_keys")
    suspend fun clearRemoteKeys()
}
