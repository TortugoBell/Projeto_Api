package br.com.Fiap.Cap1_API.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MapsFactory {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://maps.googleapis.com/maps/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val mapsService = retrofit.create(MapsService::class.java)
}