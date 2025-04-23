package com.example.travelcompanion.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.travelcompanion.model.TripPlan
import com.example.travelcompanion.model.TripType
import com.google.android.gms.maps.model.LatLng
import java.time.LocalDate

class TripPlanViewModel : ViewModel() {

    var id by mutableStateOf(0)
    var name by mutableStateOf("")
    var description by mutableStateOf("")
    var trip_type by mutableStateOf(TripType.LOCAL_TRIP)
    var destination by mutableStateOf(LatLng(0.0, 0.0))
    var startDate by mutableStateOf(LocalDate.now())
    var endDate by mutableStateOf(LocalDate.now())

    fun createTripPlan(): TripPlan {
        return TripPlan(id, name, description, trip_type, destination, startDate, endDate)
    }

}