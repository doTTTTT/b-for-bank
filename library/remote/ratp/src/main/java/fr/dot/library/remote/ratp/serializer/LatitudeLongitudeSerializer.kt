package fr.dot.library.remote.ratp.serializer

import fr.dot.library.remote.ratp.entities.LatitudeLongitudeEntity
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

internal object LatitudeLongitudeSerializer : KSerializer<LatitudeLongitudeEntity> {
    private val serializer = ListSerializer(Double.serializer())
    override val descriptor: SerialDescriptor = serializer.descriptor

    override fun deserialize(decoder: Decoder): LatitudeLongitudeEntity {
        val (latitude, longitude) = decoder.decodeSerializableValue(serializer)

        return LatitudeLongitudeEntity(
            latitude = latitude,
            longitude = longitude
        )
    }

    override fun serialize(encoder: Encoder, value: LatitudeLongitudeEntity) {
        encoder.encodeSerializableValue(serializer, listOf(value.latitude, value.longitude))
    }

}