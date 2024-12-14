package com.example.doctorlocationapp.data.model

data class ZipCodeRequest(
    val tieredView: String = "false",
    val sortByZipClass: String = "true",
    val clientId: String = "member",
    val address: Address
)

data class Address(val zip: String)
