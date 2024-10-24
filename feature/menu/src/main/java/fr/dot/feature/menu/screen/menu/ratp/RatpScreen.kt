@file:OptIn(ExperimentalMaterial3AdaptiveApi::class)

package fr.dot.feature.menu.screen.menu.ratp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Wc
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import fr.dot.library.ui.R
import fr.dot.library.ui.theme.BForBankTheme
import fr.dot.library.ui.theme.BforBankTheme
import org.koin.androidx.compose.koinViewModel

private val DetailEmptyIconSize = 64.dp

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

    LaunchedEffect(uiState.item) {
        if (uiState.item != null) {
            navigator.navigateTo(ListDetailPaneScaffoldRole.Detail)
        }
    }

    NavigableListDetailPaneScaffold(
        navigator = navigator,
        listPane = {
            AnimatedPane {
                ListContent(
                    uiState = uiState,
                    onAction = onAction
                )
            }
        },
        detailPane = {
            AnimatedPane {
                if (uiState.item != null) {
                    DetailContent(uiState.item)
                } else {
                    DetailEmptyContent()
                }
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
private fun ListContent(
    uiState: RatpUIState,
    onAction: (RatpAction) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(1)
    ) {
        items(
            items = uiState.items,
            key = ToiletItem::recordId
        ) {
            ItemUI(
                item = it,
                onClick = { onAction(RatpAction.SelectItem(it)) }
            )
        }
    }
}

@Composable
private fun DetailContent(
    item: ToiletItem
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = item.recordId,
            style = BForBankTheme.typography.titleLarge
        )
    }
}

@Composable
private fun DetailEmptyContent() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            BForBankTheme.padding.medium,
            Alignment.CenterVertically
        ),
        modifier = Modifier.fillMaxSize()
    ) {
        Icon(
            imageVector = Icons.Sharp.Wc,
            contentDescription = null,
            modifier = Modifier.size(DetailEmptyIconSize)
        )
        Text(
            text = stringResource(R.string.screen_menu_item_detail_empty),
            style = BForBankTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = BForBankTheme.padding.medium)
        )
    }
}

@Composable
private fun LazyGridItemScope.ItemUI(
    item: ToiletItem,
    onClick: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = BForBankTheme.padding.medium,
                vertical = BForBankTheme.padding.small
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .animateItem()
                .clickable(onClick = onClick)
        ) {
            Text(text = item.recordId)
        }
    }
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