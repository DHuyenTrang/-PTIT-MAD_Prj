package com.n3t.mobile.presentation.license_plate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.n3t.mobile.data.model.api_util.Either
import com.n3t.mobile.data.model.api_util.Failure
import com.n3t.mobile.data.model.api_util.whenLeftRight
import com.n3t.mobile.data.model.car.AddLicensePlateRequest
import com.n3t.mobile.data.model.car.LookupLicensePlateRequest
import com.n3t.mobile.data.model.car.RequestDeleteLicensePlate
import com.n3t.mobile.data.model.car.TrafficInfoModel
import com.n3t.mobile.data.repositories.LicensePlateRepository
import com.google.gson.JsonElement
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LicensePlateViewModel(
    private val licensePlateRepository: LicensePlateRepository,
) : ViewModel() {

    private val _licensePlates = MutableStateFlow<ArrayList<String>>(arrayListOf())
    val licensePlates: StateFlow<ArrayList<String>> = _licensePlates.asStateFlow()

    private val _licensePlatesQuery = MutableStateFlow<TrafficInfoModel?>(null)
    val licensePlatesQuery: StateFlow<TrafficInfoModel?> = _licensePlatesQuery.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _haveFine = MutableStateFlow<Boolean?>(null)
    val haveFine: StateFlow<Boolean?> = _haveFine.asStateFlow()

    private val _responseAddLicensePlateResult = MutableStateFlow<Either<Failure, JsonElement?>?>(null)
    val responseAddLicensePlateResult: StateFlow<Either<Failure, JsonElement?>?> = _responseAddLicensePlateResult.asStateFlow()

    init {
        getLicensePlates()
    }

    fun getLicensePlates() = viewModelScope.launch {
        val response = licensePlateRepository.getLicensePlates()
        response.whenLeftRight({}, { listResults ->
            _licensePlates.value = ArrayList(listResults)
        })
    }

    fun licensePlatesQuery(licensePlate: String) = viewModelScope.launch {
        _isLoading.value = true
        val response = licensePlateRepository.licensePlateQuery(
            LookupLicensePlateRequest(listOf(licensePlate))
        )
        response.whenLeftRight(
            { _haveFine.value = false },
            {
                if (it.isNotEmpty()) {
                    _haveFine.value = true
                    _licensePlatesQuery.value = it.first()
                }
            }
        )
        _isLoading.value = false
    }

    fun addLicensePlate(licensePlate: String) = viewModelScope.launch {
        _responseAddLicensePlateResult.value =
            licensePlateRepository.addLicensePlate(AddLicensePlateRequest(licensePlate))
    }

    fun deleteLicensePlate(arrayLicensePlate: List<String>) = viewModelScope.launch {
        val response = licensePlateRepository.deleteLicensePlate(
            RequestDeleteLicensePlate(arrayLicensePlate)
        )
        response.whenLeftRight(
            {},
            {
                val currentList = _licensePlates.value
                currentList.removeAll(arrayLicensePlate)
                _licensePlates.value = ArrayList(currentList)
            }
        )
    }
}
