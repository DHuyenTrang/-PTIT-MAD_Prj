package com.n3t.mobile.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.n3t.mobile.data.model.api_util.whenLeftRight
import com.n3t.mobile.data.model.search_map.PlaceDetailResponseModel
import com.n3t.mobile.data.model.search_map.PlacePrediction
import com.n3t.mobile.data.repositories.PlaceRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val placeRepository: PlaceRepository,
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _searchResults = MutableStateFlow<List<PlacePrediction>>(emptyList())
    val searchResults: StateFlow<List<PlacePrediction>> = _searchResults.asStateFlow()

    private val _placeDetail = MutableStateFlow<PlaceDetailResponseModel?>(null)
    val placeDetail: StateFlow<PlaceDetailResponseModel?> = _placeDetail.asStateFlow()

    private var searchJob: Job? = null

    fun searchPlace(query: String, lat: Float? = null, lng: Float? = null) {
        searchJob?.cancel()
        if (query.isBlank()) {
            _searchResults.value = emptyList()
            return
        }
        searchJob = viewModelScope.launch {
            delay(300) // Debounce 300ms
            _isLoading.value = true
            val result = placeRepository.searchPlace(query, lat, lng)
            result.whenLeftRight(
                { _searchResults.value = emptyList() },
                { response -> _searchResults.value = response?.predictions ?: emptyList() }
            )
            _isLoading.value = false
        }
    }

    fun getPlaceDetail(placeId: String) = viewModelScope.launch {
        _isLoading.value = true
        val result = placeRepository.getPlaceDetail(placeId)
        result.whenLeftRight(
            { _placeDetail.value = null },
            { _placeDetail.value = it }
        )
        _isLoading.value = false
    }
}
