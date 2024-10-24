package fr.dot.domain.repository

import fr.dot.domain.entities.RatpWC

interface RatpRepository {

    suspend fun getWC(): List<RatpWC>

}