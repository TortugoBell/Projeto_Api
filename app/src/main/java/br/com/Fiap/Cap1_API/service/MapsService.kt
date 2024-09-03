package br.com.Fiap.Cap1_API.service

import br.com.Fiap.Cap1_API.model.NearbySearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MapsService {
    @GET("place/nearbysearch/json")
    fun searchNearbyPlaces(
        @Query("location") location: String,
        @Query("radius") radius: Int,
        @Query("type") type: String,
        @Query("key") apiKey: String
    ): Response<NearbySearchResponse>
}