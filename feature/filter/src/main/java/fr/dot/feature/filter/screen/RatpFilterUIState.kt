package fr.dot.feature.filter.screen

import androidx.compose.runtime.Immutable
import com.google.android.gms.maps.model.LatLng
import fr.dot.library.ui.common.ViewModelState

@Immutable
internal data class RatpFilterUIState(
    val distance: Int = 500,
    val latLng: LatLng? = null
) : ViewModelState