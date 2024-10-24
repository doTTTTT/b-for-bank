package fr.dot.library.remote.ratp.entities

import fr.dot.domain.entities.RatpWC
import fr.dot.library.remote.ratp.serializer.FrenchBooleanSerializer
import kotlinx.datetime.Instant
import kotlinx.datetime.serializers.InstantIso8601Serializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class RatpWcEntity(

    @SerialName("datasetid")
    val dataSetId: String,

    @SerialName("recordid")
    val recordId: String,

    @SerialName("fields")
    val fields: FieldsEntity,

    @SerialName("geometry")
    val geometry: GeometryEntity,

    @SerialName("record_timestamp")
    @Serializable(InstantIso8601Serializer::class)
    val recordTimestamp: Instant

) {

    @Serializable
    data class FieldsEntity(

        @SerialName("complement_addresse")
        val complementAddress: String = "",

        @SerialName("geo_shape")
        val geoShape: GeoShapeEntity? = null,

        @SerialName("horaire")
        val time: String,

        @SerialName("acces_pmr")
        @Serializable(FrenchBooleanSerializer::class)
        val accesPmr: String = "",

        @SerialName("arrondissement")
        val arrondissement: Int? = null,

        @SerialName("geo_point_2d")
        val geoPoint2d: LatitudeLongitudeEntity,

        @SerialName("source")
        val source: String,

        @SerialName("gestionnaire")
        val gestionnaire: String,

        @SerialName("adresse")
        val address: String,

        @SerialName("type")
        val type: String,

        @SerialName("dist")
        val dist: String

    )

    @Serializable
    data class GeometryEntity(

        @SerialName("type")
        val type: String,

        @SerialName("coordinates")
        val coordinates: LatitudeLongitudeEntity

    )

    @Serializable
    data class GeoShapeEntity(

        @SerialName("coordinates")
        val coordinates: List<LatitudeLongitudeEntity> = emptyList(),

        @SerialName("type")
        val type: String

    )

}

internal fun RatpWcEntity.toDomain() = RatpWC(
    recordId = recordId
)