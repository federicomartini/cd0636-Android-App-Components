package com.udacity.project.spire.data.remote.dto

/**
 * Root response from GET /api/buildings.
 */
data class BuildingsResponse(
    val buildings: List<BuildingDto>,
    val pagination: PaginationMetadata?
)
