package fr.dot.library.data.repository.ratp

import androidx.paging.PagingData
import fr.dot.domain.entities.RatpWC
import fr.dot.domain.repository.RatpRepository
import kotlinx.coroutines.flow.Flow

internal class RatpRepositoryImpl(
    private val remote: RatpRemoteDataSource
) : RatpRepository {

    override suspend fun getWC(): List<RatpWC> {
        return remote.getWc()
    }

    override fun pagingWc(): Flow<PagingData<RatpWC>> {
        return remote.paging()
    }

}