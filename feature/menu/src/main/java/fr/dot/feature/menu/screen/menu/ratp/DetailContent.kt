package fr.dot.feature.menu.screen.menu.ratp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Accessible
import androidx.compose.material.icons.rounded.AdminPanelSettings
import androidx.compose.material.icons.rounded.Navigation
import androidx.compose.material.icons.rounded.NotAccessible
import androidx.compose.material.icons.rounded.SocialDistance
import androidx.compose.material.icons.sharp.Wc
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowWidthSizeClass
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import fr.dot.domain.entities.asLatLng
import fr.dot.library.ui.R
import fr.dot.library.ui.theme.BForBankTheme

@Composable
internal fun DetailContent(
    item: ToiletItem,
    onAction: (RatpAction) -> Unit
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
        Button(
            onClick = { onAction(RatpAction.NavigateTo(item.geoPoint)) }
        ) {
            Icon(
                imageVector = Icons.Rounded.Navigation,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
            Spacer(Modifier.width(BForBankTheme.padding.small))
            Text(
                stringResource(R.string.common_navigate)
            )
        }
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
internal fun DetailEmptyContent() {
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
            modifier = Modifier.size(EmptyIconSize)
        )
        Text(
            text = stringResource(R.string.screen_menu_item_detail_empty),
            style = BForBankTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = BForBankTheme.padding.medium)
        )
    }
}