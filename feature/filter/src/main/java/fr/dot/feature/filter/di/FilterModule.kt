package fr.dot.feature.filter.di

import fr.dot.feature.filter.screen.RatpFilterViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val filterModule = module {
    viewModelOf(::RatpFilterViewModel)
}