package fr.dot.feature.menu

import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.map
import androidx.test.ext.junit.runners.AndroidJUnit4
import fr.dot.domain.entities.RatpWC
import fr.dot.feature.menu.screen.menu.ratp.Content
import fr.dot.feature.menu.screen.menu.ratp.RatpAction
import fr.dot.feature.menu.screen.menu.ratp.RatpTestTag
import fr.dot.feature.menu.screen.menu.ratp.RatpUIState
import fr.dot.feature.menu.screen.menu.ratp.ToiletItem
import fr.dot.feature.menu.screen.menu.ratp.toItem
import fr.dot.library.ui.formatter.DateTimeFormatter
import fr.dot.library.ui.theme.BforBankTheme
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertIs

@RunWith(AndroidJUnit4::class)
class RatpScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testItemIsDisplayed() {
        val expectedAddress = "expected-address"
        val dateFormatter = mockk<DateTimeFormatter> {
            every {
                dateTime.format(Instant.DISTANT_PAST.toLocalDateTime(TimeZone.currentSystemDefault()))
            } returns ""
        }

        composeTestRule.setContent {
            BforBankTheme {
                Content(
                    uiState = RatpUIState(),
                    items = flowOf(
                        PagingData.from(
                            listOf(
                                RatpWC(
                                    recordId = "random-id",
                                    address = expectedAddress
                                )
                            )
                        )
                    )
                        .map { it.map { item -> item.toItem(dateFormatter) } }
                        .collectAsLazyPagingItems(),
                    snackbarHostState = SnackbarHostState(),
                    onAction = {}
                )
            }
        }

        composeTestRule.onNodeWithTag(RatpTestTag.LIST)
            .performScrollToNode(hasText(expectedAddress))
            .assertIsDisplayed()
    }

    @Test
    fun testItemClick() {
        val toiletItem = ToiletItem(
            recordId = "expected-id",
            address = "expected-address"
        )
        val onAction: (RatpAction) -> Unit = {
            assertIs<RatpAction.SelectItem>(it)
        }

        composeTestRule.setContent {
            BforBankTheme {
                Content(
                    uiState = RatpUIState(),
                    items = flowOf(PagingData.from(listOf(toiletItem)))
                        .collectAsLazyPagingItems(),
                    snackbarHostState = SnackbarHostState(),
                    onAction = onAction
                )
            }
        }

        val node = composeTestRule.onNodeWithTag(toiletItem.recordId)

        node.assertIsDisplayed()
        node.performClick()
    }

}