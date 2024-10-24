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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import fr.dot.feature.menu.screen.menu.ratp.RatpScreen
import fr.dot.library.ui.theme.BforBankTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun MenuScreen(
    viewModel: MenuViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Content(
        uiState = uiState,
        onAction = viewModel::onAction
    )
}

@Composable
private fun Content(
    uiState: MenuUIState,
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
            uiState = uiState
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
    uiState: MenuUIState
) {
    val navController = rememberNavController()

    LaunchedEffect(uiState.menu) {
        navController.navigate(
            when (uiState.menu) {
                MenuItem.RATP -> MenuRatpRoute
                MenuItem.POKEMON -> MenuPokemonRoute
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
            RatpScreen()
        }
        composable<MenuPokemonRoute> { }
        composable<MenuProfileRoute> { }
    }
}

@Preview
@Composable
private fun Preview() {
    BforBankTheme {
        Content(
            uiState = MenuUIState(),
            onAction = {}
        )
    }
}