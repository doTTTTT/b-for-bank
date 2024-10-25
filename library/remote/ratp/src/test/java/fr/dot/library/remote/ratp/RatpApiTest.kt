package fr.dot.library.remote.ratp

import fr.dot.library.remote.ratp.datasource.RatpApi
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.HttpRequestData
import io.ktor.client.request.HttpResponseData
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.append
import io.ktor.http.headers
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

class RatpApiTest {

    @Test
    fun `should return 200`() = runTest {
        val api = createApi {
            respond(
                content = ByteReadChannel("""{"nhits":609,"parameters":{"dataset":"sanisettesparis2011","rows":10,"start":10,"format":"json","timezone":"UTC"}}"""),
                status = HttpStatusCode.OK,
                headers = headers {
                    append(HttpHeaders.ContentType, ContentType.Application.Json)
                }
            )
        }
        val response = api.getWcs(
            start = 10,
            distance = 1000,
            latLng = null
        )

        assertEquals(10, response.parameters.start)
    }

    private fun createApi(block: MockRequestHandleScope.(HttpRequestData) -> HttpResponseData): RatpApi {
        val engine = MockEngine(block)
        val client = HttpClient(engine) {
            useDefaultTransformers = true
            install(ContentNegotiation) {
                json()
            }
        }

        return RatpApi(client = client)
    }

}