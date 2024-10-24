package fr.dot.feature.menu.screen

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.AccountCircle
import androidx.compose.material.icons.sharp.Gamepad
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
    POKEMON(
        R.string.screen_menu_item_pokemon,
        Icons.Sharp.Gamepad
    ),
    PROFILE(
        R.string.screen_menu_item_profile,
        Icons.Sharp.AccountCircle
    )
}

@Serializable
internal data object MenuRatpRoute

@Serializable
internal data object MenuPokemonRoute

@Serializable
internal data object MenuProfileRoute