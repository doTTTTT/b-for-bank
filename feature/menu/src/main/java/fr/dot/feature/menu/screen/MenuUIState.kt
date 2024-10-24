package fr.dot.feature.menu.screen

import androidx.compose.runtime.Immutable
import fr.dot.library.ui.common.ViewModelState

@Immutable
internal data class MenuUIState(
    val menu: MenuItem = MenuItem.RATP
) : ViewModelState