package fr.dot.library.data.repository.ratp

import androidx.paging.PagingData
import fr.dot.domain.entities.LatitudeLongitude
import fr.dot.domain.entities.RatpWC
import kotlinx.coroutines.flow.Flow

interface RatpRemoteDataSource {

    fun paging(
        distance: Int,
        latitudeLongitude: LatitudeLongitude?
    ): Flow<PagingData<RatpWC>>

}