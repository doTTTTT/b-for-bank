package fr.dot.library.remote.ratp.datasource

import fr.dot.domain.entities.RatpWC
import fr.dot.library.data.repository.ratp.RatpRemoteDataSource
import fr.dot.library.remote.ratp.entities.RatpWcEntity
import fr.dot.library.remote.ratp.entities.toDomain

internal class RatpRemoteDataSourceImpl(
    private val api: RatpApi
) : RatpRemoteDataSource {

    override suspend fun getWc(): List<RatpWC> {
        return api.getWcs()
            .records
            .map(RatpWcEntity::toDomain)
    }

}