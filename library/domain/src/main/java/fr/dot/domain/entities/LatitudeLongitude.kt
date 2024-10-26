package fr.dot.domain.entities

import com.google.android.gms.maps.model.LatLng

data class LatitudeLongitude(
    val latitude: Double,
    val longitude: Double
)

fun LatitudeLongitude.stringify(): String {
    return "$latitude,$longitude"
}

fun LatitudeLongitude.asLatLng(): LatLng {
    return LatLng(latitude, longitude)
}