package com.n3t.mobile.view_model.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.n3t.mobile.data.model.api_util.Either
import com.n3t.mobile.data.model.place_flow.CoordinateModel
import com.n3t.mobile.data.model.place_flow.RouteOptionUiModel
import com.n3t.mobile.data.repositories.RoutingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RoutingUiState(
    val isLoading: Boolean = false,
    val routes: List<RouteOptionUiModel> = emptyList(),
    val errorMessage: String? = null,
) {
    val selectedRoute: RouteOptionUiModel?
        get() = routes.firstOrNull { it.isSelected } ?: routes.firstOrNull()
}

class RoutingViewModel(
    private val routingRepository: RoutingRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(RoutingUiState())
    val uiState: StateFlow<RoutingUiState> = _uiState.asStateFlow()

    fun loadRoutes(
        origin: CoordinateModel,
        destination: CoordinateModel,
        avoidTolls: Boolean = false,
        avoidHighways: Boolean = false,
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (
                val result = routingRepository.computeRoutes(
                    origin = origin,
                    destination = destination,
                    avoidTolls = avoidTolls,
                    avoidHighways = avoidHighways,
                )
            ) {
                is Either.Failure -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            routes = emptyList(),
                            errorMessage = result.value.message,
                        )
                    }
                }

                is Either.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            routes = result.value,
                            errorMessage = null,
                        )
                    }
                }
            }
        }
    }

    fun selectRoute(routeId: String) {
        _uiState.update { state ->
            state.copy(
                routes = state.routes.map { route ->
                    route.copy(isSelected = route.id == routeId)
                }
            )
        }
    }
}
