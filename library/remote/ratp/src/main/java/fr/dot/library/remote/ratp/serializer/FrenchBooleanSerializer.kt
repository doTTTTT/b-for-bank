package fr.dot.library.remote.ratp.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.serialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

internal object FrenchBooleanSerializer : KSerializer<Boolean> {
    override val descriptor: SerialDescriptor = serialDescriptor<Boolean>()

    private const val FRENCH_TRUE = "Oui"
    private const val FRENCH_FALSE = "Non"

    override fun deserialize(decoder: Decoder): Boolean {
        val booleanString = decoder.decodeString()

        return when (booleanString.lowercase()) {
            FRENCH_FALSE.lowercase() -> false
            FRENCH_TRUE.lowercase() -> true

            else -> false
        }
    }

    override fun serialize(encoder: Encoder, value: Boolean) {
        encoder.encodeString(
            when (value) {
                true -> FRENCH_TRUE
                false -> FRENCH_FALSE
            }
        )
    }

}