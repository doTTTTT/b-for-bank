package fr.dot.feature.menu.screen.menu.description

import fr.dot.library.ui.common.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class DescriptionViewModel : BaseViewModel<DescriptionUIState, DescriptionEvent>() {

    private val _uiState = MutableStateFlow(DescriptionUIState())
    override val uiState: StateFlow<DescriptionUIState> = _uiState.asStateFlow()

    fun onAction(action: DescriptionAction) {

    }

}