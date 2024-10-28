package fr.dot.feature.menu.screen.menu.description

import androidx.compose.runtime.Immutable
import fr.dot.library.ui.common.ViewModelState

@Immutable
internal data class DescriptionUIState(
    val test: String = ""
) : ViewModelState