package it.nekotak.ndkdemo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val jniWrapper = JNIWrapper()

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    /*
     * NOTE:
     *  exposing a suspend function in a view model is not recommended,
     *  but is technically possible if u really need a single shot call in the UI.
     */
    suspend fun helloFromCpp(): String {
        return viewModelScope.async {
            delay(1000)  // simulate a delay
            jniWrapper.helloFromCpp()
        }.await()
    }

    /**
     * Public API.
     */
    fun authenticate(ip: String) {
        // NOTE: naive solution for brevity;
        //  the coroutine is launched with an IO dispatcher
        //  to prevent UI thread from blocking
        viewModelScope.launch(Dispatchers.IO) {
            val success = jniWrapper.authenticate(ip)
            _state.update {
                it.copy(isAuthenticated = success)
            }
        }
    }

    /**
     * MVI-like API.
     */
    fun onUiEvent(event: UiEvent) {
        when (event) {
            UiEvent.StartLivePreview ->
                viewModelScope.launch(Dispatchers.IO) {
                    jniWrapper.startLivePreview()
                }
            UiEvent.StopLivePreview ->
                viewModelScope.launch(Dispatchers.IO) {
                    jniWrapper.stopLivePreview()
                }
        }
    }

    data class UiState(
        val isAuthenticated: Boolean = false,
    )

    sealed interface UiEvent {
        data object StartLivePreview : UiEvent
        data object StopLivePreview : UiEvent
    }
}
