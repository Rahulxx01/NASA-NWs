package com.udacity.asteroidradar.model

data class AsteroidData(
    val links: Links,
    val id: String,
    val neo_reference_id: String,
    val name: String,
    val nasa_jpl_url: String,
    val absolute_magnitude_h: Double,
    val estimated_diameter: Kilometers,
    val is_potentially_hazardous_asteroid: Boolean,
    val close_approach_data: ArrayList<CloseApproachData>,
    val is_sentry_object: Boolean
)
