package fr.dot.domain.repository

import androidx.paging.PagingData
import fr.dot.domain.entities.LatitudeLongitude
import fr.dot.domain.entities.RatpWC
import kotlinx.coroutines.flow.Flow

interface RatpRepository {

    fun pagingWc(
        distance: Int,
        latitudeLongitude: LatitudeLongitude?
    ): Flow<PagingData<RatpWC>>

}