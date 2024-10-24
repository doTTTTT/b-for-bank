package fr.dot.feature.menu.screen.menu.ratp

import androidx.lifecycle.viewModelScope
import fr.dot.domain.entities.RatpWC
import fr.dot.domain.usecase.ratp.GetRatpWcsUseCase
import fr.dot.library.ui.common.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class RatpViewModel(
    private val ratpWcsUseCase: GetRatpWcsUseCase
) : BaseViewModel<RatpUIState, RatpEvent>() {

    private val _uiState = MutableStateFlow(RatpUIState())
    override val uiState: StateFlow<RatpUIState> = _uiState.onStart { init() }
        .stateIn(_uiState.value)

    fun onAction(action: RatpAction) {
        when (action) {
            is RatpAction.SelectItem -> onSelectItem(action)
        }
    }

    private fun init() {
        viewModelScope.launch {
            ratpWcsUseCase(GetRatpWcsUseCase.Params)
                .onFailure { it.printStackTrace() }
                .onSuccess { list ->
                    _uiState.update { it.copy(items = list.map(RatpWC::toItem)) }
                }
        }
    }

    private fun onSelectItem(action: RatpAction.SelectItem) {
        _uiState.update { it.copy(item = action.item) }
    }

}