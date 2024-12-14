package com.example.doctorlocationapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.doctorlocationapp.data.repository.DoctorRepository
import com.example.doctorlocationapp.utils.isValidZipCode
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
                _error.value = ""
                if (isValidZipCode(zip)) {
                    val result = repository.searchByZip(zip)
                    Log.d("HTTP Response", result.toString())
                } else {
                    _error.value = "Invalid Zip Code. Must be 5 digits."
                }
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
            }
        }
    }
}
