package fr.dot.feature.filter.screen

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import fr.dot.library.navigation.ResultConstant
import fr.dot.library.navigation.route.RatpFilterRoute
import fr.dot.library.ui.R
import fr.dot.library.ui.common.LaunchedEffectFlowWithLifecycle
import fr.dot.library.ui.theme.BForBankTheme
import fr.dot.library.ui.theme.BforBankTheme
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import kotlin.math.roundToInt

@Composable
internal fun RatpFilterScreen(
    route: RatpFilterRoute,
    navController: NavController,
    viewModel: RatpFilterViewModel = koinViewModel { parametersOf(route) }
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffectFlowWithLifecycle(viewModel.event) { event ->
        when (event) {
            is RatpFilterEvent.Validate -> {
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.apply {
                        set(ResultConstant.DISTANCE, event.distance)
                        set(ResultConstant.LATITUDE, event.latLng.latitude)
                        set(ResultConstant.LONGITUDE, event.latLng.longitude)
                    }
                navController.popBackStack()
            }
        }
    }

    Content(
        uiState = uiState,
        onAction = viewModel::onAction
    )
}

@SuppressLint("MissingPermission")
@Composable
private fun Content(
    uiState: RatpFilterUIState,
    onAction: (RatpFilterAction) -> Unit
) {
    val context = LocalContext.current
    val camera = rememberCameraPositionState()

    LaunchedEffect(uiState.latLng) {
        if (uiState.latLng != null) {
            camera.animate(
                CameraUpdateFactory.newLatLngZoom(
                    /* latLng = */ uiState.latLng,
                    /* zoom = */ 16f
                )
            )
        }
    }

    LaunchedEffect(Unit) {
        LocationServices.getFusedLocationProviderClient(context)
            .lastLocation
            .addOnSuccessListener {
                onAction(
                    RatpFilterAction.LatLngChanged(LatLng(it.latitude, it.longitude))
                )
            }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(BForBankTheme.padding.medium),
        modifier = Modifier
            .fillMaxSize()
            .padding(BForBankTheme.padding.medium)
    ) {
        ElevatedCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(BForBankTheme.padding.small),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(BForBankTheme.padding.medium)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.screen_ratp_filter_distance),
                        style = BForBankTheme.typography.labelLarge
                    )
                    AnimatedContent(
                        targetState = uiState.distance,
                        label = "distance",
                        transitionSpec = {
                            if (targetState.toDouble() > initialState.toDouble()) {
                                (slideInVertically { height -> height } + fadeIn())
                                    .togetherWith(slideOutVertically { height -> -height } + fadeOut())
                            } else {
                                (slideInVertically { height -> -height } + fadeIn())
                                    .togetherWith(slideOutVertically { height -> height } + fadeOut())
                            }
                                .using(SizeTransform(clip = false))
                        }
                    ) {
                        Text(
                            text = "${it}m",
                            style = BForBankTheme.typography.labelSmall
                        )
                    }
                }
                Slider(
                    value = uiState.distance.toFloat(),
                    valueRange = 100f..2000f,
                    onValueChange = { onAction(RatpFilterAction.DistanceChanged(it.roundToInt())) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Column {
                Text(
                    text = stringResource(R.string.screen_ratp_filter_maps_description),
                    style = BForBankTheme.typography.labelMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(BForBankTheme.padding.medium)
                )
                GoogleMap(
                    onMapClick = { onAction(RatpFilterAction.LatLngChanged(it)) },
                    cameraPositionState = camera,
                    onMyLocationClick = {
                        onAction(
                            RatpFilterAction.LatLngChanged(
                                LatLng(
                                    it.latitude,
                                    it.longitude
                                )
                            )
                        )
                    },
                    properties = MapProperties(
                        isMyLocationEnabled = true
                    ),
                    uiSettings = MapUiSettings(
                        myLocationButtonEnabled = true,
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (uiState.latLng != null) {
                        val state = rememberMarkerState(position = uiState.latLng)

                        state.position = uiState.latLng

                        Marker(
                            state = state
                        )
                        Circle(
                            center = state.position,
                            fillColor = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = .5f),
                            radius = uiState.distance.toDouble(),
                            strokeColor = MaterialTheme.colorScheme.onTertiaryContainer,
                            onClick = {}
                        )
                    }
                }
            }
        }
        Button(
            onClick = { onAction(RatpFilterAction.Validate) },
            enabled = uiState.latLng != null,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                stringResource(R.string.common_validate)
            )
        }
    }
}

@Composable
private fun Preview() {
    BforBankTheme {
        Content(
            uiState = RatpFilterUIState(),
            onAction = {}
        )
    }
}