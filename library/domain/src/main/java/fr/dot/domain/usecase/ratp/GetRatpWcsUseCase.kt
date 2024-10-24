package fr.dot.domain.usecase.ratp

import fr.dot.domain.entities.RatpWC
import fr.dot.domain.repository.RatpRepository
import fr.dot.domain.usecase.UseCase
import fr.dot.domain.usecase.UseCaseParams

class GetRatpWcsUseCase internal constructor(
    private val ratpRepository: RatpRepository
) : UseCase<List<RatpWC>, GetRatpWcsUseCase.Params> {

    override suspend fun execute(params: Params): List<RatpWC> {
        return ratpRepository.getWC()
    }

    data object Params : UseCaseParams

}