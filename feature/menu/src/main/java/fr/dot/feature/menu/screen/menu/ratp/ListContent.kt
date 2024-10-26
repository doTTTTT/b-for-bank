@file:OptIn(ExperimentalMaterial3Api::class)

package fr.dot.feature.menu.screen.menu.ratp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.SearchOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.pullToRefreshIndicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import fr.dot.library.ui.R
import fr.dot.library.ui.theme.BForBankTheme
import kotlinx.coroutines.delay

@Composable
internal fun ListContent(
    items: LazyPagingItems<ToiletItem>,
    onAction: (RatpAction) -> Unit
) {
    val pullToRefreshState = rememberPullToRefreshState()
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.bank)
    )

    var isRefreshing by remember { mutableStateOf(false) }

    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            delay(1000)
            isRefreshing = false
        }
    }

    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier
            .fillMaxSize()
            .pullToRefresh(
                isRefreshing = isRefreshing,
                state = pullToRefreshState,
                onRefresh = { isRefreshing = true }
            )
            .semantics { isTraversalGroup = true }
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(1),
            modifier = Modifier
                .fillMaxSize()
                .testTag(RatpTestTag.LIST)
        ) {
            items(
                count = items.itemCount,
                key = items.itemKey(ToiletItem::recordId)
            ) { index ->
                val item = items[index]

                if (item != null) {
                    ItemUI(
                        item = item,
                        onClick = { onAction(RatpAction.SelectItem(item)) }
                    )
                }
            }
            if (items.loadState.append is LoadState.Loading) {
                item(
                    key = "APPEND"
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(56.dp)
                                .animateItem()
                        )
                    }
                }
            }
        }
        AnimatedVisibility(
            visible = items.itemCount == 0,
            modifier = Modifier.align(Alignment.Center)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(BForBankTheme.padding.medium),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Sharp.SearchOff,
                    contentDescription = null,
                    modifier = Modifier.size(EmptyIconSize)
                )
                Text(
                    text = stringResource(R.string.screen_menu_ratp_empty),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = BForBankTheme.padding.large)
                )
            }
        }
        LottieAnimation(
            composition = composition,
            progress = { minOf(pullToRefreshState.distanceFraction, 1f) / 2.6f },
            modifier = Modifier
                .pullToRefreshIndicator(
                    state = pullToRefreshState,
                    containerColor = BForBankTheme.colorScheme.surfaceContainer,
                    isRefreshing = isRefreshing
                )
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
            verticalArrangement = Arrangement.spacedBy(BForBankTheme.padding.small),
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(BForBankTheme.padding.medium)
                .animateItem()
                .testTag(item.recordId)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = item.recorded,
                    style = BForBankTheme.typography.labelSmall,
                    modifier = Modifier.align(Alignment.Top)
                )
            }
            Text(
                text = item.address,
                style = BForBankTheme.typography.titleSmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}