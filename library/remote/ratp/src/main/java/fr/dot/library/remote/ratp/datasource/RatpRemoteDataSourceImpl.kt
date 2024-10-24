package fr.dot.library.remote.ratp.datasource

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import fr.dot.domain.entities.RatpWC
import fr.dot.library.data.repository.ratp.RatpRemoteDataSource
import fr.dot.library.remote.ratp.RatpConstant
import fr.dot.library.remote.ratp.entities.RatpWcEntity
import fr.dot.library.remote.ratp.entities.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class RatpRemoteDataSourceImpl(
    private val api: RatpApi
) : RatpRemoteDataSource {

    override suspend fun getWc(): List<RatpWC> {
        return api.getWcs(0)
            .records
            .map(RatpWcEntity::toDomain)
    }

    override fun paging(): Flow<PagingData<RatpWC>> {
        return Pager(
            config = PagingConfig(
                pageSize = RatpConstant.PER_PAGE
            )
        ) {
            ToiletPagingSource(api)
        }
            .flow
            .map { it.map(RatpWcEntity::toDomain) }
    }

}