package fr.dot.feature.menu.screen.menu.ratp

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Bathroom
import androidx.compose.material.icons.sharp.CleanHands
import androidx.compose.material.icons.sharp.CurrencyBitcoin
import androidx.compose.material.icons.sharp.QuestionMark
import androidx.compose.material.icons.sharp.Wc
import androidx.compose.runtime.Immutable
import fr.dot.domain.entities.LatitudeLongitude
import fr.dot.domain.entities.RatpWC
import fr.dot.library.ui.common.ViewModelState
import fr.dot.library.ui.formatter.DateTimeFormatter
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Immutable
internal data class RatpUIState(
    val item: ToiletItem? = null,
    val distance: Int = 1000,
    val latitude: Double? = null,
    val longitude: Double? = null
) : ViewModelState

@Immutable
internal data class ToiletItem(
    val recordId: String = "",
    val address: String = "",
    val borough: Int? = null,
    val geoPoint: LatitudeLongitude? = null,
    val distance: String? = null,
    val accessPmr: Boolean = false,
    val recorded: String = "",
    val administrator: String? = null,
    val type: RatpWC.Type = RatpWC.Type.UNKNOWN
)

internal fun RatpWC.toItem(
    formatter: DateTimeFormatter
) = ToiletItem(
    recordId = recordId,
    address = address,
    borough = borough,
    geoPoint = geoPoint,
    distance = distance,
    accessPmr = accessPmr,
    recorded = formatter.dateTime.format(recorded.toLocalDateTime(TimeZone.currentSystemDefault())),
    administrator = administrator,
    type = type
)

internal val ToiletItem.icon
    get() = when (type) {
        RatpWC.Type.WC -> Icons.Sharp.Wc
        RatpWC.Type.SANISETTE -> Icons.Sharp.Bathroom
        RatpWC.Type.LAVOTORY -> Icons.Sharp.CleanHands
        RatpWC.Type.URINOIR -> Icons.Sharp.CurrencyBitcoin

        RatpWC.Type.UNKNOWN -> Icons.Sharp.QuestionMark
    }

internal object RatpTestTag {
    const val LIST = "ratp_test_tag"
}