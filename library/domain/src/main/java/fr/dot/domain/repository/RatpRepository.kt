package fr.dot.domain.repository

import androidx.paging.PagingData
import fr.dot.domain.entities.RatpWC
import kotlinx.coroutines.flow.Flow

interface RatpRepository {

    suspend fun getWC(): List<RatpWC>

    fun pagingWc(): Flow<PagingData<RatpWC>>

}