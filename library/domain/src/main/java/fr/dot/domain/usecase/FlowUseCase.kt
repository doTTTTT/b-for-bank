package fr.dot.domain.usecase

import kotlinx.coroutines.flow.Flow

interface FlowUseCase<R, P : UseCaseParams> {

    operator fun invoke(params: P): Flow<R>

}