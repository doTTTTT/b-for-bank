@file:OptIn(ExperimentalCoroutinesApi::class)

package fr.dot.feature.menu.screen.menu.ratp

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import fr.dot.domain.usecase.ratp.FlowOfRatpWcsUseCase
import fr.dot.library.ui.common.BaseViewModel
import fr.dot.library.ui.formatter.DateTimeFormatter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.update

internal class RatpViewModel(
    flowOfRatpWcsUseCase: FlowOfRatpWcsUseCase,
    private val formatter: DateTimeFormatter
) : BaseViewModel<RatpUIState, RatpEvent>() {

    private val _uiState = MutableStateFlow(RatpUIState())
    override val uiState: StateFlow<RatpUIState> = _uiState.asStateFlow()

    val items = uiState.flatMapLatest { state ->
        flowOfRatpWcsUseCase(
            FlowOfRatpWcsUseCase.Params(
                distance = state.distance,
                latitude = state.latitude,
                longitude = state.longitude
            )
        )
    }
        .mapLatest { list -> list.map { it.toItem(formatter) } }
        .cachedIn(viewModelScope)

    fun onAction(action: RatpAction) {
        when (action) {
            is RatpAction.SelectItem -> onSelectItem(action)
            RatpAction.Filter -> onFilter()
            is RatpAction.FilterChange -> onFilterChange(action)
            is RatpAction.NavigateTo -> onNavigateTo(action)
            is RatpAction.SelectFilter -> onSelectFilter(action)
        }
    }

    private fun onSelectItem(action: RatpAction.SelectItem) {
        _uiState.update { it.copy(item = action.item) }
    }

    private fun onFilter() {
        sendEvents(RatpEvent.NavigateToFilter)
    }

    private fun onFilterChange(action: RatpAction.FilterChange) {
        _uiState.update { state ->
            state.copy(
                latitude = action.latitude,
                longitude = action.longitude,
                distance = action.distance
            )
        }
    }

    private fun onNavigateTo(action: RatpAction.NavigateTo) {
        sendEvents(RatpEvent.NavigateTo(action.latitudeLongitude ?: return))
    }

    private fun onSelectFilter(action: RatpAction.SelectFilter) {

    }

}