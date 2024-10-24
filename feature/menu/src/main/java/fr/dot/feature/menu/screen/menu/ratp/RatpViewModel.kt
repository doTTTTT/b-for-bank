package fr.dot.feature.menu.screen.menu.ratp

import androidx.lifecycle.viewModelScope
import fr.dot.domain.usecase.ratp.GetRatpWcsUseCase
import fr.dot.library.ui.common.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

internal class RatpViewModel(
    private val ratpWcsUseCase: GetRatpWcsUseCase
) : BaseViewModel<RatpUIState, RatpEvent>() {

    private val _uiState = MutableStateFlow(RatpUIState())
    override val uiState: StateFlow<RatpUIState> = _uiState.onStart { init() }
        .stateIn(_uiState.value)

    fun onAction(action: RatpAction) {

    }

    private fun init() {
        viewModelScope.launch {
            ratpWcsUseCase(GetRatpWcsUseCase.Params)
                .onFailure { it.printStackTrace() }
                .onSuccess { }
        }
    }

}