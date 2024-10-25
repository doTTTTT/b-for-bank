package fr.dot.library.ui.common

import androidx.compose.runtime.Composable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@Composable
fun <T> LaunchedEffectFlowWithLifecycle(
    flow: Flow<T>,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    collect: suspend CoroutineScope.(T) -> Unit,
) {
    LifecycleStartEffect(flow) {
        val job = lifecycleScope.launch {
            flow.flowWithLifecycle(lifecycle, minActiveState)
                .collect { collect(it) }
        }

        onStopOrDispose { job.cancel() }
    }
}