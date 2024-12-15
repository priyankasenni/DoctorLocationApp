package com.example.doctorlocationapp.utils


fun isValidZipCode(zipCode: String): Boolean {
    if (zipCode.isEmpty()) return false
    val zipCodeRegex = "^[0-9]{5}$".toRegex()
    return zipCode.matches(zipCodeRegex)
}

