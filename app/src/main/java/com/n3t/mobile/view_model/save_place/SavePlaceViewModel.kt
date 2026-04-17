package com.n3t.mobile.view_model.save_place

import androidx.lifecycle.ViewModel
import com.n3t.mobile.data.datasources.local.AppStore
import com.n3t.mobile.data.model.place_flow.PlaceDetailUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class PlaceCategory {
    HOME, OFFICE, FAVORITE
}

data class SavePlaceUiState(
    val savedPlaces: List<PlaceDetailUiModel> = emptyList(),
    val homePlace: PlaceDetailUiModel? = null,
    val officePlace: PlaceDetailUiModel? = null,
    val isLoading: Boolean = false
)

class SavePlaceViewModel(
    private val appStore: AppStore
) : ViewModel() {

    private val _uiState = MutableStateFlow(SavePlaceUiState())
    val uiState: StateFlow<SavePlaceUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        _uiState.value = _uiState.value.copy(
            savedPlaces = appStore.getFavoritesPlacesList(),
            homePlace = appStore.getHomePlaceDetail(),
            officePlace = appStore.getOfficePlaceDetail()
        )
    }

    fun savePlace(place: PlaceDetailUiModel, category: PlaceCategory) {
        when (category) {
            PlaceCategory.HOME -> appStore.setHomePlaceDetail(place)
            PlaceCategory.OFFICE -> appStore.setOfficePlaceDetail(place)
            PlaceCategory.FAVORITE -> appStore.addFavoritePlace(place)
        }
        loadData()
    }

    fun deleteFavoritePlace(placeId: String) {
        appStore.removeFavoritePlace(placeId)
        loadData()
    }

    fun deleteHome() {
        appStore.clearHomePlace()
        loadData()
    }

    fun deleteOffice() {
        appStore.clearOfficePlace()
        loadData()
    }

    fun deletePlace(place: PlaceDetailUiModel) {
        val home = uiState.value.homePlace
        val office = uiState.value.officePlace
        
        when {
            place.placeId == home?.placeId -> deleteHome()
            place.placeId == office?.placeId -> deleteOffice()
            else -> deleteFavoritePlace(place.placeId)
        }
    }
}
