package fr.dot.library.remote.ratp.entities

import fr.dot.domain.entities.LatitudeLongitude
import fr.dot.library.remote.ratp.serializer.LatitudeLongitudeSerializer
import kotlinx.serialization.Serializable

@Serializable(with = LatitudeLongitudeSerializer::class)
internal data class LatitudeLongitudeEntity(
    val latitude: Double,
    val longitude: Double
)

internal fun LatitudeLongitudeEntity.toDomain() = LatitudeLongitude(
    latitude = latitude,
    longitude = longitude
)