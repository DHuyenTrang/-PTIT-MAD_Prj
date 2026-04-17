package com.n3t.mobile.view_model.search

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.n3t.mobile.data.model.api_util.Either
import com.n3t.mobile.data.model.place_flow.CoordinateModel
import com.n3t.mobile.data.model.place_flow.PlaceDetailUiModel
import com.n3t.mobile.data.repositories.PlaceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DetailPlaceUiState(
    val isLoading: Boolean = false,
    val placeDetail: PlaceDetailUiModel? = null,
    val errorMessage: String? = null,
)

class DetailPlaceViewModel(
    private val placeRepository: PlaceRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailPlaceUiState())
    val uiState: StateFlow<DetailPlaceUiState> = _uiState.asStateFlow()

    fun loadPlaceDetail(
        placeId: String,
        sessionToken: String?,
        fallbackTitle: String,
        fallbackSubtitle: String,
        currentLocation: CoordinateModel?,
    ) {
        if (_uiState.value.placeDetail?.placeId == placeId) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = placeRepository.getPlaceDetail(placeId, sessionToken)) {
                is Either.Failure -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.value.message,
                            placeDetail = null,
                        )
                    }
                }

                is Either.Success -> {
                    val detail = result.value
                    val distanceMeters = currentLocation?.let {
                        val outputs = FloatArray(1)
                        Location.distanceBetween(
                            it.latitude,
                            it.longitude,
                            detail.latitude,
                            detail.longitude,
                            outputs,
                        )
                        outputs.firstOrNull()?.toInt()
                    }
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = null,
                            placeDetail = detail.copy(
                                name = if (detail.name.isBlank()) fallbackTitle else detail.name,
                                formattedAddress = if (detail.formattedAddress.isBlank()) fallbackSubtitle else detail.formattedAddress,
                                distanceMeters = distanceMeters,
                            ),
                        )
                    }
                }
            }
        }
    }
}
