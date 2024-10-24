package fr.dot.library.remote.ratp.datasource

import fr.dot.library.remote.ratp.entities.RatpRecordsEntity
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

internal class RatpApi(
    private val client: HttpClient
) {

    suspend fun getWcs(): RatpRecordsEntity {
        return client.get("https://data.ratp.fr/api/records/1.0/search/?dataset=sanisettesparis2011")
            .body()
    }

}