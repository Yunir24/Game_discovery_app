package com.dauto.gamediscoveryapp.data.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit


object ApiFactory {
    val contentType = "application/json".toMediaType()

    private val json = Json { ignoreUnknownKeys = true
   coerceInputValues = true}


    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.rawg.io")
        .addConverterFactory(json.asConverterFactory(contentType))
        .build()

    // Create Service
    val service = retrofit.create(ApiService::class.java)
}