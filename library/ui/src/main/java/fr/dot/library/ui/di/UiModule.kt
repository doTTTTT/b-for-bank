package fr.dot.library.ui.di

import fr.dot.library.ui.formatter.DateTimeFormatter
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val uiModule = module {
    single { DateTimeFormatter.withContext(androidContext()) }
}