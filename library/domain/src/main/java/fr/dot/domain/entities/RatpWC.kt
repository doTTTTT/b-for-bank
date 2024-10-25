package fr.dot.domain.entities

import kotlinx.datetime.Instant

data class RatpWC(
    val recordId: String,
    val address: String = "",
    val borough: Int? = null,
    val geoPoint: LatitudeLongitude? = null,
    val distance: String? = null,
    val accessPmr: Boolean = false,
    val recorded: Instant = Instant.DISTANT_PAST,
    val administrator: String? = null,
    val type: Type = Type.UNKNOWN
) {

    enum class Type {
        WC,
        SANISETTE,
        LAVOTORY,
        URINOIR,

        UNKNOWN
    }

}