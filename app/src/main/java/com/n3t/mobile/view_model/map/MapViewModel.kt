package com.n3t.mobile.view_model.map

import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.n3t.mobile.data.model.api_util.whenLeftRight
import com.n3t.mobile.data.model.common.MapStyleURL
import com.n3t.mobile.data.model.common.getStyleUrl
import com.n3t.mobile.data.model.place.StationModel
import com.n3t.mobile.data.model.place.StationProvider
import com.n3t.mobile.data.repositories.StationRepository
import com.n3t.mobile.utils.geohash.GeoHash
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for MapFragment — manages map state and station data using StateFlow and GeoHash throttling.
 */
class MapViewModel(
    private val stationRepository: StationRepository,
) : ViewModel() {

    // --- Current user location ---
    private val _currentLocation = MutableStateFlow<Location?>(null)
    val currentLocation: StateFlow<Location?> = _currentLocation.asStateFlow()

    // --- Current speed in km/h ---
    private val _speed = MutableStateFlow(0)
    val speed: StateFlow<Int> = _speed.asStateFlow()

    // --- Camera follow state ---
    private val _isFollowingCamera = MutableStateFlow(true)
    val isFollowingCamera: StateFlow<Boolean> = _isFollowingCamera.asStateFlow()

    // --- Map style ---
    private val _mapStyle = MutableStateFlow(MapStyleURL.DEFAULT)
    val mapStyle: StateFlow<MapStyleURL> = _mapStyle.asStateFlow()

    // --- Station ---
    var stationProvider: StationProvider = StationProvider.NONE
    var chosenStation: StationModel? = null

    /** All stations currently loaded from the API */
    private val _stations = MutableStateFlow<List<StationModel>>(emptyList())
    val stations: StateFlow<List<StationModel>> = _stations.asStateFlow()

    /** Tracks the geohash of the last loaded area to throttle API calls */
    private var centerMobileGeohash: String? = null

    /**
     * Update current location and calculate speed.
     */
    fun updateLocation(location: Location) {
        _currentLocation.value = location
        // Convert m/s to km/h
        val speedKmh = if (location.hasSpeed()) {
            (location.speed * 3.6).toInt()
        } else {
            0
        }
        _speed.value = speedKmh
    }

    /**
     * Set camera follow mode.
     */
    fun setFollowingCamera(following: Boolean) {
        _isFollowingCamera.value = following
    }

    /**
     * Toggle camera follow mode.
     */
    fun toggleCameraFollow() {
        _isFollowingCamera.value = !_isFollowingCamera.value
    }

    /**
     * Change map style.
     */
    fun setMapStyle(style: MapStyleURL) {
        _mapStyle.value = style
    }

    /**
     * Get current map style URL string.
     */
    fun getCurrentStyleUrl(): String {
        return _mapStyle.value.getStyleUrl()
    }

    // ====== Station Logic ======

    /**
     * Fetch stations from API based on current map center and provider type.
     * Uses GeoHash to throttle calls if the area hasn't changed significantly.
     *
     * @param lat Map center latitude
     * @param lon Map center longitude
     * @param zoom Current zoom level
     * @param isCompulsory If true, ignore GeoHash throttling
     */
    fun fetchStations(lat: Double, lon: Double, zoom: Double, isCompulsory: Boolean = false) {
        if (stationProvider == StationProvider.NONE) {
            _stations.value = emptyList()
            centerMobileGeohash = null
            return
        }

        // 1. Calculate geohash for current center
        val geohashLevel = getGeohashLevel(zoom)
        val currentGeohash = GeoHash(lat, lon, geohashLevel).toString()

        // 2. Throttle check
        if (!isCompulsory && centerMobileGeohash == currentGeohash) {
            return
        }

        centerMobileGeohash = currentGeohash

        // 3. Perform API call
        viewModelScope.launch {
            val result = stationRepository.getStations(lat, lon, stationProvider.getProvider())
            result.whenLeftRight(
                left = { failure ->
                    Log.e("MapViewModel", "fetchStations failed: ${failure.message}")
                },
                right = { data ->
                    _stations.value = data
                }
            )
        }
    }

    /**
     * Determine GeoHash level based on Mapbox zoom level.
     * Logic ported from gofa-android PlaceViewModel.
     */
    private fun getGeohashLevel(zoom: Double): Int {
        return when {
            zoom <= 5.0 -> 1
            zoom < 10.0 -> 2
            zoom < 13.0 -> 3
            zoom < 15.0 -> 4
            zoom < 16.0 -> 5
            else -> 6
        }
    }

    /**
     * Clear all loaded stations.
     */
    fun clearStations() {
        _stations.value = emptyList()
        chosenStation = null
        centerMobileGeohash = null
    }
}
