package fr.dot.feature.filter.screen

import com.google.android.gms.maps.model.LatLng
import fr.dot.library.navigation.route.RatpFilterRoute
import fr.dot.library.ui.common.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

internal class RatpFilterViewModel(
    route: RatpFilterRoute
) : BaseViewModel<RatpFilterUIState, RatpFilterEvent>() {

    private val _uiState = MutableStateFlow(
        with(route) {
            val latitude = route.latitude
            val longitude = route.longitude

            RatpFilterUIState(
                distance = route.distance,
                latLng = if (latitude != null && longitude != null) {
                    LatLng(latitude, longitude)
                } else {
                    null
                }
            )
        }
    )
    override val uiState: StateFlow<RatpFilterUIState> = _uiState.asStateFlow()

    fun onAction(action: RatpFilterAction) {
        when (action) {
            is RatpFilterAction.DistanceChanged -> onDistanceChanged(action)
            is RatpFilterAction.LatLngChanged -> onLatLngChanged(action)
            RatpFilterAction.Validate -> onValidate()
        }
    }

    private fun onDistanceChanged(action: RatpFilterAction.DistanceChanged) {
        _uiState.update { it.copy(distance = action.distance) }
    }

    private fun onLatLngChanged(action: RatpFilterAction.LatLngChanged) {
        _uiState.update { it.copy(latLng = action.latLng) }
    }

    private fun onValidate() {
        val uiState = uiState.value

        sendEvents(
            RatpFilterEvent.Validate(
                distance = uiState.distance,
                latLng = uiState.latLng ?: return
            )
        )
    }

}