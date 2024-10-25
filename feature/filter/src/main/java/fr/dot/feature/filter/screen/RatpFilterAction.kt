package fr.dot.feature.filter.screen

import com.google.android.gms.maps.model.LatLng

internal sealed interface RatpFilterAction {

    data class DistanceChanged(val distance: Int) : RatpFilterAction

    data class LatLngChanged(val latLng: LatLng) : RatpFilterAction

    data object Validate : RatpFilterAction

}