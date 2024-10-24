package fr.dot.feature.menu.screen.menu.ratp

import androidx.compose.runtime.Immutable
import fr.dot.domain.entities.RatpWC
import fr.dot.library.ui.common.ViewModelState

@Immutable
internal data class RatpUIState(
    val item: ToiletItem? = null,
    val items: List<ToiletItem> = emptyList()
) : ViewModelState

@Immutable
internal data class ToiletItem(
    val recordId: String
)

internal fun RatpWC.toItem() = ToiletItem(
    recordId = recordId
)