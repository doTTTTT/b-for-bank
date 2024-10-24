package fr.dot.feature.menu.screen

import fr.dot.library.ui.common.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

internal class MenuViewModel : BaseViewModel<MenuUIState, MenuEvent>() {

    private val _uiState = MutableStateFlow(MenuUIState())
    override val uiState: StateFlow<MenuUIState> = _uiState.asStateFlow()

    fun onAction(action: MenuAction) {
        when (action) {
            is MenuAction.MenuSelect -> onMenuSelect(action)
        }
    }

    private fun onMenuSelect(action: MenuAction.MenuSelect) {
        _uiState.update { it.copy(menu = action.menu) }
    }

}