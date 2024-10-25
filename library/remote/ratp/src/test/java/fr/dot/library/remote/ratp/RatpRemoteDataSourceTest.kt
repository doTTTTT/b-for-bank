package fr.dot.library.remote.ratp

import fr.dot.library.remote.ratp.datasource.RatpApi
import fr.dot.library.remote.ratp.datasource.RatpRemoteDataSourceImpl
import fr.dot.library.remote.ratp.entities.RatpRecordsEntity
import fr.dot.library.remote.ratp.entities.RatpWcEntity
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

internal class RatpRemoteDataSourceTest {

    @Test
    fun `test remote return list of one`() = runTest {
        val api = mockk<RatpApi>()
        val remote = RatpRemoteDataSourceImpl(api)
        val expectedId = "expected-id"

        coEvery { api.getWcs(start = 0, distance = 0, latLng = null) } returns RatpRecordsEntity(
            nhits = 0,
            parameters = RatpRecordsEntity.ParametersEntity(),
            records = listOf(
                RatpWcEntity(recordId = expectedId)
            )
        )

        val list = remote.getWc()
        val item = list.firstOrNull()

        assertEquals(1, list.size)
        assertNotNull(item)
        assertEquals(expectedId, item.recordId)
    }

    @Test
    fun `test remote return list of two`() = runTest {
        val api = mockk<RatpApi>()
        val remote = RatpRemoteDataSourceImpl(api)
        val expectedIdOne = "expected-id-one"
        val expectedIdTwo = "expected-id-two"

        coEvery { api.getWcs(start = 0, distance = 0, latLng = null) } returns RatpRecordsEntity(
            nhits = 0,
            parameters = RatpRecordsEntity.ParametersEntity(),
            records = listOf(
                RatpWcEntity(recordId = expectedIdOne),
                RatpWcEntity(recordId = expectedIdTwo)
            )
        )

        val list = remote.getWc()
        val (itemOne, itemTwo) = list

        assertEquals(2, list.size)
        assertEquals(expectedIdOne, itemOne.recordId)
        assertEquals(expectedIdTwo, itemTwo.recordId)
    }

}