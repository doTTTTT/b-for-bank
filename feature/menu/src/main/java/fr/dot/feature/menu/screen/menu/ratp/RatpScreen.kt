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
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import fr.dot.library.ui.R
import fr.dot.library.ui.theme.BForBankTheme
import fr.dot.library.ui.theme.BforBankTheme
import kotlinx.coroutines.flow.emptyFlow
import org.koin.androidx.compose.koinViewModel

private val DetailEmptyIconSize = 64.dp

@Composable
internal fun RatpScreen(
    viewModel: RatpViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val items = viewModel.items.collectAsLazyPagingItems()

    Content(
        uiState = uiState,
        items = items,
        onAction = viewModel::onAction
    )
}

@Composable
private fun Content(
    uiState: RatpUIState,
    items: LazyPagingItems<ToiletItem>,
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
                    items = items,
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
    items: LazyPagingItems<ToiletItem>,
    onAction: (RatpAction) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(1)
    ) {
        items(
            count = items.itemCount,
            key = items.itemKey { it.recordId }
        ) { index ->
            val item = items[index]

            if (item != null) {
                ItemUI(
                    item = item,
                    onClick = { onAction(RatpAction.SelectItem(item)) }
                )
            }
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
                .clickable(onClick = onClick)
                .padding(16.dp)
                .animateItem()
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
            items = emptyFlow<PagingData<ToiletItem>>().collectAsLazyPagingItems(),
            onAction = {}
        )
    }
}