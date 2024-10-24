package fr.dot.library.remote.ratp.di

import fr.dot.library.data.repository.ratp.RatpRemoteDataSource
import fr.dot.library.remote.ratp.datasource.RatpApi
import fr.dot.library.remote.ratp.datasource.RatpRemoteDataSourceImpl
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

private const val RATP_JSON = "ratp_json"
private const val RATP_CLIENT = "ratp_client"

val ratpModule = module {
    single(named(RATP_JSON)) { provideJson() }
    single(named(RATP_CLIENT)) { provideClient(get<Json>(named(RATP_JSON))) }

    single { RatpApi(get(named(RATP_CLIENT))) }
    singleOf(::RatpRemoteDataSourceImpl) bind RatpRemoteDataSource::class
}

private fun provideJson() = Json {
    ignoreUnknownKeys = true
}

private fun provideClient(json: Json): HttpClient = HttpClient(OkHttp) {
    install(ContentNegotiation) {
        json(json)
    }

    install(Logging) {
        level = LogLevel.ALL
        logger = object : Logger {
            override fun log(message: String) {
                println(message)
            }
        }
    }
}