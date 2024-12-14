package com.example.doctorlocationapp.data.repository

import com.example.doctorlocationapp.data.api.DoctorService
import com.example.doctorlocationapp.data.model.Address
import com.example.doctorlocationapp.data.model.ZipCodeRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DoctorRepository {
    private val service: DoctorService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://eyedoclocatorashuat.eyemedvisioncare.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        service = retrofit.create(DoctorService::class.java)
    }

    suspend fun searchByZip(zip: String): Any {
        val request = ZipCodeRequest(address = Address(zip))
        return service.searchByZip(request)
    }
}