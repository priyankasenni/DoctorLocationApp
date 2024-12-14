package com.example.doctorlocationapp.data

import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Call

data class ZipCodeRequest(
    val tieredView: String = "false",
    val sortByZipClass: String = "true",
    val clientId: String = "member",
    val address: Address
)

data class Address(val zip: String)

interface ApiService {
    @POST("api/v1/search")
    fun searchByZip(@Body request: ZipCodeRequest): Call<String>
}
