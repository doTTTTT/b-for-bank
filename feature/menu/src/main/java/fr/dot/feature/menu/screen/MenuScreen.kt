package fr.dot.feature.menu.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import fr.dot.feature.menu.screen.menu.description.DescriptionScreen
import fr.dot.feature.menu.screen.menu.ratp.RatpScreen
import fr.dot.library.navigation.ResultConstant
import fr.dot.library.ui.theme.BforBankTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun MenuScreen(
    navController: NavController,
    viewModel: MenuViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Content(
        uiState = uiState,
        mainNavController = navController,
        onAction = viewModel::onAction
    )
}

@Composable
private fun Content(
    uiState: MenuUIState,
    mainNavController: NavController,
    onAction: (MenuAction) -> Unit
) {
    NavigationSuiteScaffold(
        navigationSuiteItems = {
            menuContent(
                uiState = uiState,
                onAction = onAction
            )
        },
        modifier = Modifier.fillMaxSize()
    ) {
        MainContent(
            uiState = uiState,
            mainNavController = mainNavController
        )
    }
}

private fun NavigationSuiteScope.menuContent(
    uiState: MenuUIState,
    onAction: (MenuAction) -> Unit
) {
    MenuItem.entries
        .forEach {
            item(
                selected = uiState.menu == it,
                alwaysShowLabel = false,
                icon = {
                    Icon(
                        imageVector = it.icon,
                        contentDescription = null
                    )
                },
                label = {
                    Text(
                        text = stringResource(it.label)
                    )
                },
                onClick = { onAction(MenuAction.MenuSelect(it)) }
            )
        }
}

@Composable
private fun MainContent(
    uiState: MenuUIState,
    mainNavController: NavController
) {
    val navController = rememberNavController()

    LaunchedEffect(uiState.menu) {
        navController.navigate(
            when (uiState.menu) {
                MenuItem.RATP -> MenuRatpRoute
                MenuItem.PROFILE -> MenuProfileRoute
            }
        )
    }

    NavHost(
        navController = navController,
        startDestination = MenuRatpRoute,
        modifier = Modifier.fillMaxSize()
    ) {
        composable<MenuRatpRoute> {
            RatpScreen(
                navController = mainNavController,
                distance = mainNavController.currentBackStackEntry
                    ?.savedStateHandle
                    ?.get<Int>(ResultConstant.DISTANCE),
                latitude = mainNavController.currentBackStackEntry
                    ?.savedStateHandle
                    ?.get<Double>(ResultConstant.LATITUDE),
                longitude = mainNavController.currentBackStackEntry
                    ?.savedStateHandle
                    ?.get<Double>(ResultConstant.LONGITUDE)
            )
        }
        composable<MenuProfileRoute> {
            DescriptionScreen()
        }
    }
}

@Preview
@Composable
private fun Preview() {
    BforBankTheme {
        Content(
            uiState = MenuUIState(),
            mainNavController = rememberNavController(),
            onAction = {}
        )
    }
}