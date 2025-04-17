package com.hestabit.sparkmatch.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.hestabit.sparkmatch.utils.Utils.printDebug
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.IOException
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    @ApplicationContext val context: Context
) : ViewModel() {

    companion object {
        private val _userCurrentLocation = MutableStateFlow<Location?>(null)
        val userCurrentLocation: StateFlow<Location?> = _userCurrentLocation

        private val _userCurrentAddress = MutableStateFlow("")
        val userCurrentAddress: StateFlow<String> = _userCurrentAddress
    }

    private val _locationPairData = MutableLiveData<Pair<Double, Double>>()
    val locationPairData: LiveData<Pair<Double, Double>> = _locationPairData

    private lateinit var locationProvider: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    init {
        startLocationUpdates()
    }

    @SuppressLint("MissingPermission")
    fun startLocationUpdates(
        intervalMillis: Long = 900000L,
        fastestIntervalMillis: Long = 300000L
    ) {
        locationProvider = LocationServices.getFusedLocationProviderClient(context)

        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            intervalMillis
        ).setMinUpdateIntervalMillis(fastestIntervalMillis).build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                super.onLocationResult(result)
                result.lastLocation?.let { location ->
                    _userCurrentLocation.value = location
                    _userCurrentAddress.value = getPlaceNameFromLatLng(location.latitude, location.longitude)
                    printDebug("address ->> ${_userCurrentAddress.value}")
                    _locationPairData.value = Pair(location.latitude, location.longitude)
                }
            }
        }

        locationProvider.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    fun stopLocationUpdates() {
        if (::locationProvider.isInitialized && ::locationCallback.isInitialized) {
            locationProvider.removeLocationUpdates(locationCallback)
        }
    }

    private fun getPlaceNameFromLatLng(latitude: Double, longitude: Double): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        return try {
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            if (!addresses.isNullOrEmpty()) {
                "${addresses[0].locality}, ${addresses[0].countryCode}" ?: ""
            } else {
                ""
            }
        } catch (e: IOException) {
            e.printStackTrace()
            ""
        }
    }

    override fun onCleared() {
        super.onCleared()
        stopLocationUpdates()
    }
}

