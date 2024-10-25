package fr.dot.library.remote.ratp.datasource

import fr.dot.domain.entities.LatitudeLongitude
import fr.dot.library.remote.ratp.RatpConstant
import fr.dot.library.remote.ratp.entities.RatpRecordsEntity
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

internal class RatpApi(
    private val client: HttpClient
) {

    suspend fun getWcs(
        start: Int,
        distance: Int,
        latLng: LatitudeLongitude?
    ): RatpRecordsEntity {
        return client.get("https://data.ratp.fr/api/records/1.0/search/?dataset=sanisettesparis2011") {
            parameter("rows", RatpConstant.PER_PAGE)
            parameter("start", start)
            if (latLng != null) {
                parameter("geofilter.distance", "${latLng.latitude},${latLng.longitude},$distance")
            }
        }
            .body()
    }

}