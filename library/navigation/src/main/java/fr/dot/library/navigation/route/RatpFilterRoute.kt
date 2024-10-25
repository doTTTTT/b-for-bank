package fr.dot.library.navigation.route

import kotlinx.serialization.Serializable

@Serializable
data class RatpFilterRoute(
    val distance: Int,
    val latitude: Double?,
    val longitude: Double?
)