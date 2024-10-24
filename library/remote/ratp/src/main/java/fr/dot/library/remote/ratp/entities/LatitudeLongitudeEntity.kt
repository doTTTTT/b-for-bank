package fr.dot.library.remote.ratp.entities

import fr.dot.library.remote.ratp.serializer.LatitudeLongitudeSerializer
import kotlinx.serialization.Serializable

@Serializable(with = LatitudeLongitudeSerializer::class)
internal data class LatitudeLongitudeEntity(
    val latitude: Double,
    val longitude: Double
)