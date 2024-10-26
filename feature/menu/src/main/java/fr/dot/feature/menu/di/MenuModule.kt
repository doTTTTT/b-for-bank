package fr.dot.feature.menu.di

import fr.dot.feature.menu.screen.MenuViewModel
import fr.dot.feature.menu.screen.menu.profile.ProfileViewModel
import fr.dot.feature.menu.screen.menu.ratp.RatpViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val menuModule = module {
    viewModelOf(::MenuViewModel)

    viewModelOf(::RatpViewModel)
    viewModelOf(::ProfileViewModel)
}