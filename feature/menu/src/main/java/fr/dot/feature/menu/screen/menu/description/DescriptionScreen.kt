@file:OptIn(ExperimentalLayoutApi::class)

package fr.dot.feature.menu.screen.menu.description

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import fr.dot.domain.entities.RatpWC
import fr.dot.library.ui.R.string
import fr.dot.library.ui.theme.BForBankTheme
import fr.dot.library.ui.theme.BforBankTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun DescriptionScreen(
    viewModel: DescriptionViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Content(
        uiState = uiState,
        onAction = viewModel::onAction
    )
}

@Composable
private fun Content(
    uiState: DescriptionUIState,
    onAction: (DescriptionAction) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(BForBankTheme.padding.medium),
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(BForBankTheme.padding.medium)
    ) {
        Text(
            text = stringResource(string.screen_description_title),
            style = BForBankTheme.typography.headlineLarge
        )
        Text(
            text = stringResource(string.screen_description_description),
            style = BForBankTheme.typography.bodyLarge
        )
        CardTypes()
    }
}

@Composable
private fun CardTypes() {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(BForBankTheme.padding.small),
            modifier = Modifier.padding(BForBankTheme.padding.medium)
        ) {
            Text(
                text = stringResource(string.screen_description_list_of_types),
                style = BForBankTheme.typography.labelMedium
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(BForBankTheme.padding.small),
                verticalArrangement = Arrangement.spacedBy(BForBankTheme.padding.small),
                modifier = Modifier.fillMaxWidth()
            ) {
                RatpWC.Type.entries
                    .forEach {
                        Text(
                            text = it.name,
                            color = BForBankTheme.colorScheme.contentColorFor(
                                BForBankTheme.colorScheme.onBackground
                            ),
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(BForBankTheme.colorScheme.background)
                                .padding(
                                    horizontal = BForBankTheme.padding.small,
                                    vertical = BForBankTheme.padding.extraSmall
                                )
                        )
                    }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    BforBankTheme {
        Content(
            uiState = DescriptionUIState(),
            onAction = {}
        )
    }
}