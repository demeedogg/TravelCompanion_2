package com.example.travelcompanion.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

import com.example.travelcompanion.model.*
import com.example.travelcompanion.viewmodel.TripPlanViewModel
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTripPlanScreen(
    viewModel: TripPlanViewModel = viewModel(),
    onTripCreated: (TripPlan) -> Unit
) {

    var selectedLocation by remember { mutableStateOf<LatLng?>(null) }
    var startDate by remember { mutableStateOf<LocalDate?>(null) }
    var endDate by remember { mutableStateOf<LocalDate?>(null) }
    var expanded by remember { mutableStateOf(false) }
    val tripTypes = TripType.entries.toTypedArray()

    Column(modifier = Modifier.padding(16.dp)) {

        /*NOME DEL VIAGGIO*/
        OutlinedTextField(
            value = viewModel.name,
            onValueChange = { viewModel.name = it },
            label = { Text("Trip name") }
        )

        /*DESCRIZIONE DEL VIAGGIO*/
        OutlinedTextField(
            value = viewModel.description,
            onValueChange = { viewModel.description = it },
            label = { Text("Description") }
        )

        /*TIPO DI VIAGGIO*/
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = viewModel.trip_type.label,
                onValueChange = {}, // Non serve: la selezione avviene dal menu
                readOnly = true,
                label = { Text("Type of Trip") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable, enabled = true)
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                tripTypes.forEach { type ->
                    DropdownMenuItem(
                        text = { Text(type.label) },
                        onClick = {
                            viewModel.trip_type = type
                            expanded = false
                        }
                    )
                }
            }
        }


        /*GOOGLE MAPPA */
        Text("Select Destination")
        // Google Maps composable (devi configurarlo nel progetto)
        GoogleMapView(
            selectedLocation = selectedLocation,
            onLocationSelected = { latLng ->
                selectedLocation = latLng
            }
        )


        // Date Picker per data di inizio e fine viaggio
        Text("Start Date")
        DatePickerField(date = startDate, onDateSelected = { startDate = it })

        Text("End Date")
        DatePickerField(date = endDate, onDateSelected = { endDate = it })

        /*bottone per confermare la selezione*/
        Button(onClick = {
            val trip = viewModel.createTripPlan()
            onTripCreated(trip)
        }) {
            Text("Create")
        }
    }
}

@Composable
fun GoogleMapView(
    selectedLocation: LatLng?,
    onLocationSelected: (LatLng) -> Unit
) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition(LatLng(41.9028, 12.4964), 5f, 0f, 0f) // Roma
    }

    GoogleMap(
        modifier = Modifier.height(300.dp),
        cameraPositionState = cameraPositionState,
        onMapClick = { latLng ->
            onLocationSelected(latLng)
        }
    ) {
        selectedLocation?.let { location ->
            Marker(
                state = rememberMarkerState(position = location),
                title = "Selected Location"
            )
        }
    }
}
@Composable
fun DatePickerField(date: LocalDate?, onDateSelected: (LocalDate) -> Unit) {
    TextField(
        value = date?.toString() ?: "Select Date",
        onValueChange = {},
        label = { Text("Date") },
        modifier = Modifier.fillMaxWidth(),
        readOnly = true,
        trailingIcon = {
            DatePickerDialog(
                onDateSelected = onDateSelected
            )
        }
    )
}

@Composable
fun DatePickerDialog(onDateSelected: (LocalDate) -> Unit) {
    // Implementazione del date picker qui
    // Puoi usare una libreria come `MaterialDatePicker` o creare un custom dialog
}




