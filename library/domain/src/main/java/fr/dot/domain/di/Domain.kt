package fr.dot.domain.di

import fr.dot.domain.usecase.ratp.FlowOfRatpWcsUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val domainModule = module {
    factoryOf(::FlowOfRatpWcsUseCase)
}