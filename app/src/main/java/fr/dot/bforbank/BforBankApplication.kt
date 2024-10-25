package fr.dot.bforbank

import android.app.Application
import fr.dot.domain.di.domainModule
import fr.dot.feature.menu.di.menuModule
import fr.dot.library.data.di.dataModule
import fr.dot.library.remote.ratp.di.ratpModule
import fr.dot.library.ui.di.uiModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BforBankApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@BforBankApplication)
            modules(
                uiModule,
                ratpModule,
                dataModule,
                domainModule,

                menuModule
            )
        }
    }

}