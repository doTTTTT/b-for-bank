package fr.dot.library.remote.ratp.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class RatpRecordsEntity(

    @SerialName("nhits")
    val nhits: Int,

    @SerialName("parameters")
    val parameters: ParametersEntity,

    @SerialName("records")
    val records: List<RatpWcEntity> = emptyList()

) {

    @Serializable
    data class ParametersEntity(

        @SerialName("dataset")
        val dataset: String,

        @SerialName("rows")
        val rows: Int,

        @SerialName("start")
        val start: Int,

        @SerialName("format")
        val format: String,

        @SerialName("geofilter.distance")
        val geoFilterDistance: List<String> = emptyList(),

        @SerialName("timezone")
        val timeZone: String

    )

}