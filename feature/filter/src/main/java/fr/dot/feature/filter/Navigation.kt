package fr.dot.feature.filter

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import fr.dot.feature.filter.screen.RatpFilterScreen
import fr.dot.library.navigation.route.RatpFilterRoute

fun NavGraphBuilder.filterGraph(navController: NavHostController) {
    composable<RatpFilterRoute> {
        RatpFilterScreen(
            route = it.toRoute(),
            navController = navController
        )
    }
}