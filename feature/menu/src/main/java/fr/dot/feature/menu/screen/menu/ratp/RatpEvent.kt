package fr.dot.feature.menu.screen.menu.ratp

import fr.dot.domain.entities.LatitudeLongitude
import fr.dot.library.ui.common.ViewModelEvent

internal sealed interface RatpEvent : ViewModelEvent {

    data object NavigateToFilter : RatpEvent

    data class NavigateTo(val latitudeLongitude: LatitudeLongitude) : RatpEvent

}