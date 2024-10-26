package fr.dot.feature.menu.screen.menu.profile

import fr.dot.library.ui.common.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class ProfileViewModel : BaseViewModel<ProfileUIState, ProfileEvent>() {

    private val _uiState = MutableStateFlow(ProfileUIState())
    override val uiState: StateFlow<ProfileUIState> = _uiState.asStateFlow()

    fun onAction(action: ProfileAction) {

    }

}