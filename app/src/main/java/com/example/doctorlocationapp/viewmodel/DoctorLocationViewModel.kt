package com.example.doctorlocationapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.doctorlocationapp.data.repository.DoctorRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DoctorLocationViewModel : ViewModel() {
    private val repository = DoctorRepository()

    private val _response = MutableStateFlow("")
    val response = _response.asStateFlow()

    private val _error = MutableStateFlow("")
    val error = _error.asStateFlow()

    fun searchByZip(zip: String) {
        viewModelScope.launch {
            try {
                if (zip.length == 5) {
                    val result = repository.searchByZip(zip)
//                    _response.value = "Success: $result"
                    Log.d("HTTP Response", result.toString())

                } else {
                    _error.value = "Invalid Zip Code. Must be 5 digits."
                }
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
            }
        }
    }

    fun searchByCountyTapped() {
        _response.value = "Search by County Tapped"
    }

    fun useLocation() {
        _response.value = "Fetching device location..."
    }
}