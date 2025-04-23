package com.example.travelcompanion_2



import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.travelcompanion.ui.screens.CreateTripPlanScreen
import com.example.travelcompanion.viewmodel.TripPlanViewModel
import com.example.travelcompanion_2.ui.screens.CreateTripPlanScreen_chat
import com.example.travelcompanion_2.ui.screens.HomeScreen



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TripPlanApp()
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripPlanApp() {

    val navController = rememberNavController()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Travel Companion") }
            )
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(paddingValues)
        ) {

            composable("home") {
                HomeScreen(
                    navController=navController
                )
            }

            composable("create_trip_plan") {
                CreateTripPlanScreen_chat (
                    viewModel = TripPlanViewModel(),
                    onTripCreated = { _ ->
                        navController.popBackStack("home", false)
                    }
                )
            }


        }
    }
}

