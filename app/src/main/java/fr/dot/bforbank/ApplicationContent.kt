package fr.dot.bforbank

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import fr.dot.feature.menu.menuGraph
import fr.dot.library.navigation.route.MenuRoute
import fr.dot.library.ui.theme.BforBankTheme

@Composable
internal fun ApplicationContent() {
    val navController = rememberNavController()

    BforBankTheme {
        NavHost(
            navController = navController,
            startDestination = MenuRoute
        ) {
            menuGraph(navController)
        }
    }
}

@Composable
private fun Preview() {
    BforBankTheme {
        ApplicationContent()
    }
}