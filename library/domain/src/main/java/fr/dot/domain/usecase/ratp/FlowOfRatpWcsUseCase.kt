package fr.dot.domain.usecase.ratp

import androidx.paging.PagingData
import fr.dot.domain.entities.RatpWC
import fr.dot.domain.repository.RatpRepository
import fr.dot.domain.usecase.FlowUseCase
import fr.dot.domain.usecase.UseCaseParams
import kotlinx.coroutines.flow.Flow

class FlowOfRatpWcsUseCase internal constructor(
    private val ratpRepository: RatpRepository
) : FlowUseCase<PagingData<RatpWC>, FlowOfRatpWcsUseCase.Params> {

    override fun invoke(params: Params): Flow<PagingData<RatpWC>> {
        return ratpRepository.pagingWc()
    }

    data object Params : UseCaseParams

}