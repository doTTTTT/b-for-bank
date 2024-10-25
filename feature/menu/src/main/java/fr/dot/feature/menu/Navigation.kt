package fr.dot.feature.menu

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import fr.dot.feature.menu.screen.MenuScreen
import fr.dot.library.navigation.route.MenuRoute

fun NavGraphBuilder.menuGraph(navController: NavHostController) {
    composable<MenuRoute> {
        MenuScreen(
            navController = navController
        )
    }
}