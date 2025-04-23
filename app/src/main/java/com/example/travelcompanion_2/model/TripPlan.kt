package com.example.travelcompanion.model

import com.google.android.gms.maps.model.LatLng
import java.time.LocalDate

data class TripPlan(
    val id: Int,
    val name: String,
    val description: String,
    val trip_type: TripType,
    val destination: LatLng,
    val startDate: LocalDate,
    val endDate: LocalDate
){

}
