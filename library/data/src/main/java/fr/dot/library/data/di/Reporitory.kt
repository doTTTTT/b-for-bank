package fr.dot.library.data.di

import fr.dot.domain.repository.RatpRepository
import fr.dot.library.data.repository.ratp.RatpRepositoryImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule = module {
    singleOf(::RatpRepositoryImpl) bind RatpRepository::class
}