package fr.dot.library.remote.ratp.serializer

import fr.dot.library.remote.ratp.entities.LatitudeLongitudeEntity
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.serialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeCollection

internal object LatitudeLongitudeSerializer : KSerializer<LatitudeLongitudeEntity> {
    override val descriptor: SerialDescriptor = serialDescriptor<LatitudeLongitudeEntity>()

    override fun deserialize(decoder: Decoder): LatitudeLongitudeEntity {
        return decoder.decodeStructure(
            serialDescriptor<List<Double>>()
        ) {
            val latitude = decodeDoubleElement(Double.serializer().descriptor, 0)
            val longitude = decodeDoubleElement(Double.serializer().descriptor, 1)

            LatitudeLongitudeEntity(
                latitude = latitude,
                longitude = longitude
            )
        }
    }

    override fun serialize(encoder: Encoder, value: LatitudeLongitudeEntity) {
        encoder.encodeCollection(
            descriptor = serialDescriptor<List<Double>>(),
            collectionSize = 2
        ) {
            encodeDoubleElement(Double.serializer().descriptor, 0, value.latitude)
            encodeDoubleElement(Double.serializer().descriptor, 1, value.longitude)
        }
    }

}