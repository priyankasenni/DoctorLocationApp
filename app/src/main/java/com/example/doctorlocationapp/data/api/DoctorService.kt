package com.example.doctorlocationapp.data.api

import com.example.doctorlocationapp.data.model.ZipCodeRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface DoctorService {
    @POST("eyedoclocator/api/v1/search")
    suspend fun searchByZip(@Body request: ZipCodeRequest): Any
}