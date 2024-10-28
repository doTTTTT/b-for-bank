package fr.dot.feature.menu.screen.menu.ratp

import fr.dot.domain.entities.LatitudeLongitude
import fr.dot.domain.entities.RatpWC

internal sealed interface RatpAction {

    data class SelectItem(val item: ToiletItem) : RatpAction

    data object Filter : RatpAction

    data class FilterChange(
        val distance: Int,
        val latitude: Double,
        val longitude: Double
    ) : RatpAction

    data class NavigateTo(val latitudeLongitude: LatitudeLongitude?) : RatpAction

    data class SelectFilter(
        val type: RatpWC.Type
    ) : RatpAction

}