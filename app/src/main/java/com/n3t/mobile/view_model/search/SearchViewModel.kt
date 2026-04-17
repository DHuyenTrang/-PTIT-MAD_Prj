package com.n3t.mobile.view_model.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.n3t.mobile.data.model.api_util.Either
import com.n3t.mobile.data.model.api_util.ErrorType
import com.n3t.mobile.data.model.place_flow.CoordinateModel
import com.n3t.mobile.data.model.place_flow.PlaceSuggestionUiModel
import com.n3t.mobile.data.repositories.PlaceRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

data class SearchUiState(
    val query: String = "",
    val isLoading: Boolean = false,
    val suggestions: List<PlaceSuggestionUiModel> = emptyList(),
    val isOffline: Boolean = false,
    val errorMessage: String? = null,
)

data class OpenPlaceDetailEvent(
    val placeId: String,
    val sessionToken: String,
    val fallbackTitle: String,
    val fallbackSubtitle: String,
    val distanceMeters: Int,
)

class SearchViewModel(
    private val placeRepository: PlaceRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private val _openPlaceDetail = MutableSharedFlow<OpenPlaceDetailEvent>()
    val openPlaceDetail: SharedFlow<OpenPlaceDetailEvent> = _openPlaceDetail.asSharedFlow()

    private var searchJob: Job? = null
    private var sessionToken: String? = null

    fun onQueryChanged(query: String, currentLocation: CoordinateModel?) {
        _uiState.update {
            it.copy(
                query = query,
                isOffline = false,
                errorMessage = null,
            )
        }

        searchJob?.cancel()
        if (query.trim().length < 2) {
            if (query.isBlank()) {
                sessionToken = null
            }
            _uiState.update {
                it.copy(
                    isLoading = false,
                    suggestions = emptyList(),
                    isOffline = false,
                    errorMessage = null,
                )
            }
            return
        }

        if (sessionToken == null) {
            sessionToken = UUID.randomUUID().toString()
        }

        searchJob = viewModelScope.launch {
            delay(350)
            _uiState.update { it.copy(isLoading = true) }

            when (val result = placeRepository.searchSuggestions(query.trim(), currentLocation, sessionToken!!)) {
                is Either.Failure -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            suggestions = emptyList(),
                            isOffline = result.value.errorType == ErrorType.LOST_INTERNET,
                            errorMessage = result.value.message,
                        )
                    }
                }

                is Either.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            suggestions = result.value,
                            isOffline = false,
                            errorMessage = null,
                        )
                    }
                }
            }
        }
    }

    fun onSuggestionSelected(item: PlaceSuggestionUiModel) {
        val activeSessionToken = sessionToken ?: UUID.randomUUID().toString()
        viewModelScope.launch {
            _openPlaceDetail.emit(
                OpenPlaceDetailEvent(
                    placeId = item.placeId,
                    sessionToken = activeSessionToken,
                    fallbackTitle = item.title,
                    fallbackSubtitle = item.subtitle,
                    distanceMeters = item.distanceMeters ?: -1,
                )
            )
        }
    }
}
