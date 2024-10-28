package fr.dot.feature.menu.screen

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Description
import androidx.compose.material.icons.sharp.Wc
import androidx.compose.ui.graphics.vector.ImageVector
import fr.dot.library.ui.R
import kotlinx.serialization.Serializable

internal enum class MenuItem(
    @StringRes val label: Int,
    val icon: ImageVector
) {
    RATP(
        R.string.screen_menu_item_ratp,
        Icons.Sharp.Wc
    ),
    DESCRIPTION(
        R.string.screen_menu_item_description,
        Icons.Sharp.Description
    )
}

@Serializable
internal data object MenuRatpRoute

@Serializable
internal data object MenuDescriptionRoute

internal val MenuItem.route: Any
    get() = when (this) {
        MenuItem.RATP -> MenuRatpRoute
        MenuItem.DESCRIPTION -> MenuDescriptionRoute
    }