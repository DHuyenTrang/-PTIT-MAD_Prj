package com.n3t.mobile.data.datasources.local

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.n3t.mobile.data.model.place_flow.PlaceDetailUiModel

class AppStore(context: Context) {
    private val prefFileName = "n3t_prefs"
    private var _storage: SharedPreferences =
        context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE)
    private val gson = Gson()

    // ====== ACCESS TOKEN ======
    fun getAccessToken(): String? {
        return try {
            _storage.getString(PrefKeys.accessToken, null)
        } catch (e: Exception) {
            null
        }
    }

    fun setAccessToken(accessToken: String?) {
        _storage.edit().putString(PrefKeys.accessToken, accessToken).apply()
    }

    fun clearAccessToken() {
        _storage.edit().remove(PrefKeys.accessToken).apply()
    }

    // ====== REFRESH TOKEN ======
    fun getRefreshToken(): String? {
        return _storage.getString(PrefKeys.refreshToken, null)
    }

    fun setRefreshToken(refreshToken: String?) {
        _storage.edit().putString(PrefKeys.refreshToken, refreshToken).apply()
    }

    fun clearRefreshToken() {
        _storage.edit().remove(PrefKeys.refreshToken).apply()
    }

    // ====== EXPIRE TIME ======
    fun getExpireTime(): Long {
        return _storage.getLong(PrefKeys.expireTime, 0)
    }

    fun setExpireTime(expireTime: Long) {
        _storage.edit().putLong(PrefKeys.expireTime, expireTime).apply()
    }

    fun clearExpireTime() {
        _storage.edit().remove(PrefKeys.expireTime).apply()
    }

    // ====== USER INFO ======
    fun getDisplayName(): String? {
        return _storage.getString(PrefKeys.displayName, null)
    }

    fun setDisplayName(displayName: String?) {
        _storage.edit().putString(PrefKeys.displayName, displayName).apply()
    }

    fun clearDisplayName() {
        _storage.edit().remove(PrefKeys.displayName).apply()
    }

    fun getPhoneNumber(): String? {
        return _storage.getString(PrefKeys.phoneNumber, null)
    }

    fun setPhoneNumber(phoneNumber: String?) {
        _storage.edit().putString(PrefKeys.phoneNumber, phoneNumber).apply()
    }

    fun clearPhoneNumber() {
        _storage.edit().remove(PrefKeys.phoneNumber).apply()
    }

    fun getCustomerId(): Int {
        return try {
            _storage.getInt(PrefKeys.customerId, -1)
        } catch (e: Exception) {
            -1
        }
    }

    fun setCustomerId(customerId: Int) {
        _storage.edit().putInt(PrefKeys.customerId, customerId).apply()
    }

    fun clearCustomerId() {
        _storage.edit().remove(PrefKeys.customerId).apply()
    }

    // ====== LICENSE PLATE ======
    fun getLicensePlate(): String? {
        return _storage.getString(PrefKeys.licensePlate, null)
    }

    fun setLicensePlate(licensePlate: String?) {
        _storage.edit().putString(PrefKeys.licensePlate, licensePlate).apply()
    }

    fun clearLicensePlate() {
        _storage.edit().remove(PrefKeys.licensePlate).apply()
    }

    // ====== PLACES ======
    fun getRecentPlaces(): String? {
        return _storage.getString(PrefKeys.recentPlaces, null)
    }

    fun setRecentPlaces(places: String?) {
        _storage.edit().putString(PrefKeys.recentPlaces, places).apply()
    }

    fun clearRecentPlaces() {
        _storage.edit().remove(PrefKeys.recentPlaces).apply()
    }

    fun getFavoritesPlaces(): String? {
        return _storage.getString(PrefKeys.favoritesPlaces, null)
    }

    fun setFavoritesPlaces(places: String?) {
        _storage.edit().putString(PrefKeys.favoritesPlaces, places).apply()
    }

    fun clearFavoritesPlaces() {
        _storage.edit().remove(PrefKeys.favoritesPlaces).apply()
    }

    fun getHomePlace(): String? {
        return _storage.getString(PrefKeys.homePlace, null)
    }

    fun setHomePlace(place: String?) {
        _storage.edit().putString(PrefKeys.homePlace, place).apply()
    }

    fun clearHomePlace() {
        _storage.edit().remove(PrefKeys.homePlace).apply()
    }

    fun getOfficePlace(): String? {
        return _storage.getString(PrefKeys.officePlace, null)
    }

    fun setOfficePlace(place: String?) {
        _storage.edit().putString(PrefKeys.officePlace, place).apply()
    }

    fun clearOfficePlace() {
        _storage.edit().remove(PrefKeys.officePlace).apply()
    }

    // JSON Helpers for list and objects
    fun getFavoritesPlacesList(): List<PlaceDetailUiModel> {
        val json = getFavoritesPlaces() ?: return emptyList()
        val type = object : TypeToken<List<PlaceDetailUiModel>>() {}.type
        return try {
            gson.fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun setFavoritesPlacesList(list: List<PlaceDetailUiModel>) {
        val json = gson.toJson(list)
        setFavoritesPlaces(json)
    }

    fun addFavoritePlace(place: PlaceDetailUiModel) {
        val list = getFavoritesPlacesList().toMutableList()
        list.removeAll { it.placeId == place.placeId }
        list.add(0, place)
        setFavoritesPlacesList(list)
    }

    fun removeFavoritePlace(placeId: String) {
        val list = getFavoritesPlacesList().toMutableList()
        list.removeAll { it.placeId == placeId }
        setFavoritesPlacesList(list)
    }

    fun getHomePlaceDetail(): PlaceDetailUiModel? {
        val json = getHomePlace() ?: return null
        return try {
            gson.fromJson(json, PlaceDetailUiModel::class.java)
        } catch (e: Exception) {
            null
        }
    }

    fun setHomePlaceDetail(place: PlaceDetailUiModel?) {
        val json = if (place != null) gson.toJson(place) else null
        setHomePlace(json)
    }

    fun getOfficePlaceDetail(): PlaceDetailUiModel? {
        val json = getOfficePlace() ?: return null
        return try {
            gson.fromJson(json, PlaceDetailUiModel::class.java)
        } catch (e: Exception) {
            null
        }
    }

    fun setOfficePlaceDetail(place: PlaceDetailUiModel?) {
        val json = if (place != null) gson.toJson(place) else null
        setOfficePlace(json)
    }

    // ====== TRACKING ======
    fun getTrackingHistoryState(): Boolean {
        return _storage.getBoolean(PrefKeys.trackingHistory, false)
    }

    fun setTrackingHistoryState(isTracking: Boolean) {
        _storage.edit().putBoolean(PrefKeys.trackingHistory, isTracking).apply()
    }

    // ====== THEME ======
    fun getTheme(): String {
        return _storage.getString(PrefKeys.theme, "system") ?: "system"
    }

    fun setTheme(theme: String) {
        _storage.edit().putString(PrefKeys.theme, theme).apply()
    }

    // ====== ONBOARDING ======
    fun getOnboarding(): Boolean = _storage.getBoolean(PrefKeys.onboarding, false)

    fun setOnboarding(value: Boolean) =
        _storage.edit().putBoolean(PrefKeys.onboarding, value).apply()

    // ====== CLEAR DATA ======
    fun clearDataAuthen() {
        clearAccessToken()
        clearRefreshToken()
        clearExpireTime()
        clearDisplayName()
        clearPhoneNumber()
        clearLicensePlate()
        clearCustomerId()
    }

    fun clearPersonalData() {
        clearHomePlace()
        clearOfficePlace()
        clearFavoritesPlaces()
        clearRecentPlaces()
    }

    fun clearAll() {
        clearDataAuthen()
        clearPersonalData()
    }
}
