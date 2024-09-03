package br.com.Fiap.Cap1_API.model

data class NearbySearchResponse(
    val results: List<Place>,
    val status: String,
)

data class Place(
    val name: String,
    val geometry: Geometry,
)

data class Geometry(
    val location: Location
)

data class Location(
    val lat: Double,
    val lng: Double
)