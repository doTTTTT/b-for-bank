package fr.dot.library.data

import androidx.paging.PagingData
import app.cash.turbine.turbineScope
import fr.dot.domain.entities.RatpWC
import fr.dot.library.data.repository.ratp.RatpRemoteDataSource
import fr.dot.library.data.repository.ratp.RatpRepositoryImpl
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

internal class RatpRepositoryImplTest {

    @Test
    fun `test paging data`() = runTest {
        turbineScope {
            val expectedId = "expected-id"
            val item = RatpWC(recordId = expectedId)
            val data = PagingData.from(listOf(item))
            val remote = mockk<RatpRemoteDataSource> {
                every { paging(distance = 0, null) } returns flowOf(data)
            }
            val repository = RatpRepositoryImpl(remote)
            val turbine = repository.pagingWc(
                distance = 0,
                latitudeLongitude = null
            ).testIn(this)

            val list = turbine.awaitItem()

            assertEquals(data, list)

            turbine.awaitComplete()
        }
    }

}