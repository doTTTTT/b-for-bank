package fr.dot.feature.menu

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollToNode
import androidx.navigation.compose.rememberNavController
import androidx.paging.PagingData
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.turbineScope
import fr.dot.domain.entities.RatpWC
import fr.dot.domain.usecase.ratp.FlowOfRatpWcsUseCase
import fr.dot.feature.menu.screen.menu.ratp.RatpScreen
import fr.dot.feature.menu.screen.menu.ratp.RatpTestTag
import fr.dot.feature.menu.screen.menu.ratp.RatpViewModel
import fr.dot.library.ui.theme.BforBankTheme
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun useAppContext() {
        val distance = 1000
        val expectedAddress = "expected-address"
        val flowOfRatpUseCase = mockk<FlowOfRatpWcsUseCase> {
            every { this@mockk(FlowOfRatpWcsUseCase.Params(distance, null, null)) } returns flowOf(
                PagingData.from(
                    listOf(
                        RatpWC(
                            recordId = "random-id",
                            address = expectedAddress
                        )
                    )
                )
            )
        }
        val viewModel = RatpViewModel(
            flowOfRatpWcsUseCase = flowOfRatpUseCase,
            formatter = mockk(relaxed = true)
        )

        composeTestRule.setContent {
            BforBankTheme {
                RatpScreen(
                    navController = rememberNavController(),
                    distance = distance,
                    latitude = null,
                    longitude = null,
                    viewModel = viewModel
                )
            }
        }

        composeTestRule.onNodeWithTag(RatpTestTag.LIST)
            .performScrollToNode(hasText(expectedAddress))
            .assertIsDisplayed()
    }
}