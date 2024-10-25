package fr.dot.feature.menu.screen.menu.ratp

internal sealed interface RatpAction {

    data class SelectItem(val item: ToiletItem) : RatpAction

    data object Filter : RatpAction

}