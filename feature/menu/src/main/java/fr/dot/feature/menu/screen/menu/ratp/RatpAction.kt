package fr.dot.feature.menu.screen.menu.ratp

internal sealed interface RatpAction {

    data class SelectItem(val item: ToiletItem) : RatpAction

    data object Filter : RatpAction

    data class FilterChange(
        val distance: Int,
        val latitude: Double,
        val longitude: Double
    ) : RatpAction

}