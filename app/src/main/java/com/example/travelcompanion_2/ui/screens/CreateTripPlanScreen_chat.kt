package com.example.travelcompanion_2.ui.screens


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.travelcompanion.model.TripPlan
import com.example.travelcompanion.model.TripType
import com.example.travelcompanion.viewmodel.TripPlanViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTripPlanScreen_chat(
    viewModel: TripPlanViewModel = viewModel(),
    onTripCreated: (TripPlan) -> Unit
) {
    val context = LocalContext.current
    var selectedLocation by remember { mutableStateOf<LatLng?>(null) }
    var startDate by remember { mutableStateOf<LocalDate?>(null) }
    var endDate by remember { mutableStateOf<LocalDate?>(null) }
    var expanded by remember { mutableStateOf(false) }
    val tripTypes = TripType.entries.toTypedArray()

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = viewModel.name,
            onValueChange = { viewModel.name = it },
            label = { Text("Trip name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = viewModel.description,
            onValueChange = { viewModel.description = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = viewModel.trip_type.label,
                onValueChange = {},
                readOnly = true,
                label = { Text("Type of Trip") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable)
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

        Spacer(modifier = Modifier.height(16.dp))
        Text("Select Destination", style = MaterialTheme.typography.titleMedium)
        LocationSelector(selectedLocation) { latLng ->
            selectedLocation = latLng
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Select Dates", style = MaterialTheme.typography.titleMedium)
        DateRangeSelector(startDate, endDate) { start, end ->
            startDate = start
            endDate = end
        }

        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = {
            val trip = viewModel.createTripPlan()
            onTripCreated(trip)
        }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text("Create")
        }
    }
}

@Composable
fun LocationSelector(selectedLocation: LatLng?, onLocationSelected: (LatLng) -> Unit) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(41.9028, 12.4964), 5f)
    }


    GoogleMap(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        cameraPositionState = cameraPositionState,
        onMapClick = {
            onLocationSelected(it)
        }
    ) {
        selectedLocation?.let {
            Marker(state = rememberMarkerState(position = it))
        }
    }
}

@Composable
fun DateRangeSelector(
    startDate: LocalDate?,
    endDate: LocalDate?,
    onRangeSelected: (LocalDate, LocalDate) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")

    val startMillis = calendar.timeInMillis

    Button(onClick = {
        val picker = com.google.android.material.datepicker.MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText("Select trip dates")
            .build()

        val activity = context as? androidx.fragment.app.FragmentActivity
        picker.addOnPositiveButtonClickListener { range ->
            val start = Instant.ofEpochMilli(range.first!!).atZone(ZoneId.systemDefault()).toLocalDate()
            val end = Instant.ofEpochMilli(range.second!!).atZone(ZoneId.systemDefault()).toLocalDate()
            onRangeSelected(start, end)
        }

        picker.show(activity!!.supportFragmentManager, "date_range_picker")
    }) {
        Text(
            text = if (startDate != null && endDate != null)
                "${startDate.format(dateFormatter)} - ${endDate.format(dateFormatter)}"
            else "Select Dates"
        )
    }
}
