package fr.dot.domain.entities

import kotlinx.datetime.Instant

data class RatpWC(
    val recordId: String,
    val address: String,
    val borough: Int?,
    val geoPoint: LatitudeLongitude?,
    val distance: String?,
    val accessPmr: Boolean,
    val recorded: Instant,
    val administrator: String?,
    val type: Type
) {

    enum class Type {
        WC,
        SANISETTE,
        UNKNOWN
    }

}