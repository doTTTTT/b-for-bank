@file:OptIn(ExperimentalMaterial3AdaptiveApi::class)

package fr.dot.feature.menu.screen.menu.ratp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import fr.dot.library.ui.theme.BforBankTheme
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun RatpScreen(
    viewModel: RatpViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Content(
        uiState = uiState,
        onAction = viewModel::onAction
    )
}

@Composable
private fun Content(
    uiState: RatpUIState,
    onAction: (RatpAction) -> Unit
) {
    val navigator = rememberListDetailPaneScaffoldNavigator<Any>()

    NavigableListDetailPaneScaffold(
        navigator = navigator,
        listPane = {
            ListContent()
        },
        detailPane = {
            DetailContent()
        },
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
private fun ListContent() {

}

@Composable
private fun DetailContent() {

}

@Preview
@Composable
private fun Preview() {
    BforBankTheme {
        Content(
            uiState = RatpUIState(),
            onAction = {}
        )
    }
}