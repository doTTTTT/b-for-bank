package fr.dot.domain.usecase

interface UseCase<R, P : UseCaseParams> {

    suspend fun execute(params: P): R

    suspend operator fun invoke(params: P): Result<R> = runCatching {
        execute(params)
    }

}