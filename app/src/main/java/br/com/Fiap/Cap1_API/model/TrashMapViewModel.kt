package br.com.Fiap.Cap1_API.model

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.places.Place
import kotlinx.coroutines.launch
import br.com.Fiap.Cap1_API.service.MapsFactory

class TrashMapViewModel : ViewModel() {
    private val _searchResults = mutableStateListOf<Place>()
    val searchResults: List<Place> = _searchResults

    private val mapsFactory = MapsFactory()

    fun searchPlaces(query: String) {
        viewModelScope.launch {
            val response = mapsFactory.mapsService.searchNearbyPlaces(
                location = "YOUR_LOCATION", // Replace with user's location
                radius = 5000,
                type = query,
                apiKey = "AIzaSyCWmH0PrIGHNpI_K9tLWs-OeyxTmWsiTwA"
            )
            if (response.isSuccessful) {
                _searchResults.clear()
                _searchResults.addAll((response.body()?.results ?: emptyList()) as Collection<Place>)
            } else {
                // Handle error
            }
        }
    }
}