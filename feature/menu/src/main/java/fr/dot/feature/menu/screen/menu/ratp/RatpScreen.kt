@file:OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalMaterial3Api::class)

package fr.dot.feature.menu.screen.menu.ratp

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.FilterList
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import fr.dot.domain.entities.stringify
import fr.dot.library.navigation.route.RatpFilterRoute
import fr.dot.library.ui.R
import fr.dot.library.ui.common.LaunchedEffectFlowWithLifecycle
import fr.dot.library.ui.theme.BforBankTheme
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

internal val EmptyIconSize = 64.dp

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
            navController.navigate(
                RatpFilterRoute(
                    distance = uiState.distance,
                    latitude = uiState.latitude,
                    longitude = uiState.longitude
                )
            )
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

            is RatpEvent.NavigateTo -> {
                try {
                    val uri = Uri.parse(
                        "google.navigation:q=${event.latitudeLongitude.stringify()}"
                    )
                    val intent = Intent(Intent.ACTION_VIEW, uri)

                    intent.setPackage("com.google.android.apps.maps")

                    context.startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    snackbarHostState.showSnackbar(context.getString(R.string.screen_menu_ratp_cant_navigate))
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
                        items = items,
                        onAction = onAction
                    )
                }
            },
            detailPane = {
                AnimatedPane {
                    if (uiState.item != null) {
                        DetailContent(
                            item = uiState.item,
                            onAction = onAction
                        )
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