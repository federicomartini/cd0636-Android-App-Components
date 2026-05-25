package com.udacity.project.spire.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Remote keys entity for building pagination.
 * Stores page metadata per building for Paging3 RemoteMediator.
 *
 * All buildings on the same API page share the same prevKey/nextKey values.
 */
@Entity(tableName = "building_remote_keys")
data class BuildingRemoteKeys(
    @PrimaryKey
    val buildingId: Int,
    val prevKey: Int?,
    val nextKey: Int?
)
