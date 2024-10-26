package fr.dot.domain

import androidx.paging.PagingData
import app.cash.turbine.turbineScope
import fr.dot.domain.entities.RatpWC
import fr.dot.domain.repository.RatpRepository
import fr.dot.domain.usecase.ratp.FlowOfRatpWcsUseCase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

internal class FlowOfRatpWcsUseCaseTest {

    @Test
    fun `test use case`() = runTest {
        turbineScope {
            val expectedId = "expected-id"
            val item = RatpWC(recordId = expectedId)
            val data = PagingData.from(listOf(item))
            val repository = mockk<RatpRepository> {
                every { pagingWc(0, null) } returns flowOf(data)
            }
            val turbine = FlowOfRatpWcsUseCase(repository)
                .invoke(FlowOfRatpWcsUseCase.Params(0, null, null))
                .testIn(this)

            val list = turbine.awaitItem()

            assertEquals(data, list)

            turbine.awaitComplete()
        }
    }

}