package fr.dot.feature.menu

import app.cash.turbine.turbineScope
import fr.dot.feature.menu.screen.menu.ratp.RatpAction
import fr.dot.feature.menu.screen.menu.ratp.RatpEvent
import fr.dot.feature.menu.screen.menu.ratp.RatpViewModel
import fr.dot.feature.menu.screen.menu.ratp.ToiletItem
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

class RatpViewModelTest {

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @Test
    fun `test click on item`() {
        val toiletItem = ToiletItem(
            recordId = "expected-id",
            address = "expected-address"
        )
        val viewModel = RatpViewModel(
            flowOfRatpWcsUseCase = mockk(relaxed = true),
            formatter = mockk(relaxed = true)
        )

        runTest {
            turbineScope {
                val turbine = viewModel.uiState.testIn(this)

                turbine.awaitItem()

                viewModel.onAction(RatpAction.SelectItem(toiletItem))

                val item = turbine.awaitItem()

                assertNotNull(item.item)
                assertEquals(toiletItem.recordId, item.item.recordId)
                assertEquals(toiletItem.address, item.item.address)

                turbine.cancel()
            }
        }
    }

    @Test
    fun `test click on filter`() {
        val viewModel = RatpViewModel(
            flowOfRatpWcsUseCase = mockk(relaxed = true),
            formatter = mockk(relaxed = true)
        )

        runTest {
            turbineScope {
                val turbine = viewModel.event.testIn(this)

                viewModel.onAction(RatpAction.Filter)

                val item = turbine.awaitItem()

                assertIs<RatpEvent.NavigateToFilter>(item)

                turbine.cancel()
            }
        }
    }

}