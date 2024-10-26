@file:OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalMaterial3Api::class)

package fr.dot.feature.menu.screen.menu.ratp

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Accessible
import androidx.compose.material.icons.rounded.AdminPanelSettings
import androidx.compose.material.icons.rounded.NotAccessible
import androidx.compose.material.icons.rounded.SocialDistance
import androidx.compose.material.icons.sharp.FilterList
import androidx.compose.material.icons.sharp.Wc
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.material3.pulltorefresh.pullToRefresh
import androidx.compose.material3.pulltorefresh.pullToRefreshIndicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import androidx.window.core.layout.WindowWidthSizeClass
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import fr.dot.domain.entities.asLatLng
import fr.dot.library.navigation.route.RatpFilterRoute
import fr.dot.library.ui.R
import fr.dot.library.ui.common.LaunchedEffectFlowWithLifecycle
import fr.dot.library.ui.theme.BForBankTheme
import fr.dot.library.ui.theme.BforBankTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

private val DetailEmptyIconSize = 64.dp

@Composable
internal fun RatpScreen(
    navController: NavController,
    distance: Int?,
    latitude: Double?,
    longitude: Double?,
    viewModel: RatpViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val items = viewModel.items.collectAsLazyPagingItems()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val permission = rememberLauncherForActivityResult(RequestMultiplePermissions()) {
        if (it.any(Map.Entry<String, @kotlin.jvm.JvmSuppressWildcards Boolean>::value)) {
            navController.navigate(RatpFilterRoute)
        } else {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = context.getString(R.string.screen_menu_ratp_permission)
                )
            }
        }
    }

    LaunchedEffectFlowWithLifecycle(viewModel.event) { event ->
        when (event) {
            RatpEvent.NavigateToFilter -> {
                if (
                    context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED
                    && context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED
                ) {
                    permission.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                    )
                } else {
                    navController.navigate(
                        RatpFilterRoute(
                            distance = uiState.distance,
                            latitude = uiState.latitude,
                            longitude = uiState.longitude
                        )
                    )
                }
            }
        }
    }

    LaunchedEffect(distance, latitude, longitude) {
        viewModel.onAction(
            RatpAction.FilterChange(
                distance = distance ?: return@LaunchedEffect,
                latitude = latitude ?: return@LaunchedEffect,
                longitude = longitude ?: return@LaunchedEffect
            )
        )
    }

    Content(
        uiState = uiState,
        items = items,
        snackbarHostState = snackbarHostState,
        onAction = viewModel::onAction
    )
}

@Composable
internal fun Content(
    uiState: RatpUIState,
    snackbarHostState: SnackbarHostState,
    items: LazyPagingItems<ToiletItem>,
    onAction: (RatpAction) -> Unit
) {
    val navigator = rememberListDetailPaneScaffoldNavigator<Any>()
    val resource = LocalContext.current.resources

    LaunchedEffect(uiState.item) {
        if (uiState.item != null) {
            navigator.navigateTo(ListDetailPaneScaffoldRole.Detail)
        }
    }

    LaunchedEffect(items.loadState.hasError) {
        if (items.loadState.hasError) {
            val result = snackbarHostState.showSnackbar(
                message = resource.getString(R.string.error_common),
                actionLabel = resource.getString(R.string.common_retry)
            )

            when (result) {
                SnackbarResult.Dismissed -> Unit
                SnackbarResult.ActionPerformed -> items.retry()
            }
        }
    }

    Scaffold(
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAction(RatpAction.Filter) }
            ) {
                Icon(
                    imageVector = Icons.Sharp.FilterList,
                    contentDescription = null
                )
            }
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState) {
                Snackbar(it)
            }
        },
        modifier = Modifier.fillMaxSize()
    ) {
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
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        )
    }
}

@Composable
private fun ListContent(
    uiState: RatpUIState,
    items: LazyPagingItems<ToiletItem>,
    onAction: (RatpAction) -> Unit
) {
    val currentWindow = currentWindowAdaptiveInfo()
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
            if (items.loadState.append is LoadState.Loading) {
                item(
                    key = "APPEND"
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(56.dp)
                            .animateItem()
                    )
                }
            }
        }
        AnimatedVisibility(
            visible = items.itemCount == 0,
            modifier = Modifier.align(Alignment.Center)
        ) {
            Text(
                text = stringResource(R.string.screen_menu_ratp_empty),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = BForBankTheme.padding.large)
            )
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
private fun DetailContent(
    item: ToiletItem
) {
    val windowInfo = currentWindowAdaptiveInfo()

    Column(
        verticalArrangement = Arrangement.spacedBy(BForBankTheme.padding.medium),
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(BForBankTheme.padding.medium)
    ) {
        Text(
            text = item.address,
            style = BForBankTheme.typography.titleLarge
        )
        if (windowInfo.windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(BForBankTheme.padding.medium),
                modifier = Modifier.fillMaxWidth()
            ) {
                DetailDescriptionItem(
                    item = item,
                    modifier = Modifier.weight(1f)
                )
                DetailItem(
                    item = item,
                    modifier = Modifier.weight(1f)
                )
            }
        } else {
            DetailDescriptionItem(
                item = item,
                modifier = Modifier.fillMaxWidth()
            )
            DetailItem(
                item = item,
                modifier = Modifier.fillMaxWidth()
            )
        }
        DetailMap(item)
    }
}

@Composable
private fun DetailDescriptionItem(
    item: ToiletItem,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(BForBankTheme.padding.small),
            modifier = Modifier
                .fillMaxWidth()
                .padding(BForBankTheme.padding.medium)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(BForBankTheme.padding.small)
            ) {
                Icon(
                    imageVector = if (item.accessPmr) {
                        Icons.AutoMirrored.Rounded.Accessible
                    } else {
                        Icons.Rounded.NotAccessible
                    },
                    contentDescription = null
                )
                Text(
                    text = stringResource(R.string.screen_menu_ratp_access_pmr),
                    style = BForBankTheme.typography.labelMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(BForBankTheme.padding.small)
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = null
                )
                Text(
                    text = item.type.toString(),
                    style = BForBankTheme.typography.labelMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun DetailItem(
    item: ToiletItem,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(BForBankTheme.padding.small),
            modifier = Modifier
                .fillMaxWidth()
                .padding(BForBankTheme.padding.medium)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(BForBankTheme.padding.small)
            ) {
                Icon(
                    imageVector = Icons.Rounded.AdminPanelSettings,
                    contentDescription = null
                )
                Text(
                    text = item.administrator ?: stringResource(R.string.screen_menu_ratp_no_admin),
                    style = BForBankTheme.typography.labelMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(BForBankTheme.padding.small)
            ) {
                Icon(
                    imageVector = Icons.Rounded.SocialDistance,
                    contentDescription = null
                )
                Text(
                    text = item.distance?.let { "${it}m" }
                        ?: stringResource(R.string.common_unknown),
                    style = BForBankTheme.typography.labelMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun DetailMap(
    item: ToiletItem
) {
    val camera = rememberCameraPositionState()

    if (item.geoPoint != null) {
        val state = rememberMarkerState(position = item.geoPoint.asLatLng())

        LaunchedEffect(item) {
            val latLng = item.geoPoint.asLatLng()

            state.position = latLng
            camera.animate(CameraUpdateFactory.newLatLngZoom(latLng, 16f))
        }

        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
        ) {
            GoogleMap(
                uiSettings = MapUiSettings(
                    compassEnabled = true,
                    zoomControlsEnabled = false,
                    zoomGesturesEnabled = false,
                    myLocationButtonEnabled = false,
                    scrollGesturesEnabled = false,
                    tiltGesturesEnabled = false,
                    indoorLevelPickerEnabled = false,
                    mapToolbarEnabled = false,
                    rotationGesturesEnabled = false,
                    scrollGesturesEnabledDuringRotateOrZoom = false
                ),
                cameraPositionState = camera,
                modifier = Modifier.fillMaxWidth()
            ) {
                Marker(
                    state = state
                )
            }
        }
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
            verticalArrangement = Arrangement.spacedBy(BForBankTheme.padding.small),
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(16.dp)
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

@Preview
@Composable
private fun Preview() {
    BforBankTheme {
        Content(
            uiState = RatpUIState(),
            items = emptyFlow<PagingData<ToiletItem>>().collectAsLazyPagingItems(),
            snackbarHostState = SnackbarHostState(),
            onAction = {}
        )
    }
}