package com.example.doctorlocationapp.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Repository {
    private val api: ApiService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://eyedoclocatorashuat.eyemedvisioncare.com/eyedoclocator/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(ApiService::class.java)
    }

    fun searchByZip(zip: String) = api.searchByZip(ZipCodeRequest(address = Address(zip)))
}
