package fr.dot.feature.filter.screen

import com.google.android.gms.maps.model.LatLng
import fr.dot.library.ui.common.ViewModelEvent

internal sealed interface RatpFilterEvent : ViewModelEvent {

    data class Validate(
        val distance: Int,
        val latLng: LatLng
    ) : RatpFilterEvent

}