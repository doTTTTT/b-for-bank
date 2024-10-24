package fr.dot.library.data.repository.ratp

import fr.dot.domain.entities.RatpWC

interface RatpRemoteDataSource {

    suspend fun getWc(): List<RatpWC>

}