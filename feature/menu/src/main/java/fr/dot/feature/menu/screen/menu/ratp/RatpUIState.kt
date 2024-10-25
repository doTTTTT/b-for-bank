package fr.dot.feature.menu.screen.menu.ratp

import androidx.compose.runtime.Immutable
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
    val recordId: String,
    val address: String,
    val borough: Int?,
    val geoPoint: String?,
    val distance: String?,
    val accessPmr: Boolean,
    val recorded: String,
    val administrator: String?,
    val type: RatpWC.Type
)

internal fun RatpWC.toItem(
    formatter: DateTimeFormatter
) = ToiletItem(
    recordId = recordId,
    address = address,
    borough = borough,
    geoPoint = geoPoint?.let { "${it.latitude},${it.longitude}" },
    distance = distance,
    accessPmr = accessPmr,
    recorded = formatter.dateTime.format(recorded.toLocalDateTime(TimeZone.currentSystemDefault())),
    administrator = administrator,
    type = type
)

internal object RatpTestTag {
    const val LIST = "ratp_test_tag"
}