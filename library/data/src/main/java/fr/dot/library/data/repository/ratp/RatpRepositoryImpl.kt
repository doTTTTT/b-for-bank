package fr.dot.library.data.repository.ratp

import fr.dot.domain.entities.RatpWC
import fr.dot.domain.repository.RatpRepository

internal class RatpRepositoryImpl(
    private val remote: RatpRemoteDataSource
) : RatpRepository {

    override suspend fun getWC(): List<RatpWC> {
        return remote.getWc()
    }

}