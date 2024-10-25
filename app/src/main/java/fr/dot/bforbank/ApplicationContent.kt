package fr.dot.bforbank

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import fr.dot.feature.filter.filterGraph
import fr.dot.feature.menu.menuGraph
import fr.dot.library.navigation.route.MenuRoute
import fr.dot.library.ui.theme.BforBankTheme

@Composable
internal fun ApplicationContent() {
    val navController = rememberNavController()

    BforBankTheme {
        NavHost(
            navController = navController,
            startDestination = MenuRoute,
            modifier = Modifier
                .fillMaxSize()
                .safeContentPadding()
        ) {
            menuGraph(navController)
            filterGraph(navController)
        }
    }
}

@Composable
private fun Preview() {
    BforBankTheme {
        ApplicationContent()
    }
}