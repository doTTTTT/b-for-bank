package fr.dot.library.ui.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

abstract class BaseViewModel<State : ViewModelState, Event : ViewModelEvent> : ViewModel() {

    abstract val uiState: StateFlow<State>

    fun <T> Flow<T>.stateIn(
        initialValue: T
    ): StateFlow<T> = stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = initialValue
    )

    private val _event = MutableSharedFlow<Event>(extraBufferCapacity = 1)
    val event: SharedFlow<Event> = _event.asSharedFlow()

    fun ViewModel.sendEvents(vararg events: Event) {
        viewModelScope.launch { events.forEach { event -> _event.emit(event) } }
    }

}

interface ViewModelState

interface ViewModelEvent