package com.griffith.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NavigationScreen()
        }
    }
}

@Composable
fun NavigationScreen(){
    val navController = rememberNavController()
    Surface(modifier = Modifier.fillMaxSize()){
        NavHost(
            navController = navController,
            startDestination = "home"){
            composable("home"){
                HomeScreen(navController)
            }
            composable("settings"){
                SettingsScreen(navController)
            }
            composable("about"){
                AboutScreen(navController)
            }

        }
    }
}

@Composable
fun HomeScreen(navController: NavController) {
    Surface(modifier = Modifier.fillMaxSize()){
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(text = "Welcome to the X-Fitness App", fontSize = 25.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.padding(10.dp))
//            Text(
//                text = "The #1 personal training app",
//                fontSize = 16.sp
//            )
            Text(text = "Daily Activity", fontSize = 20.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.padding(10.dp))
            Text(text = "Steps: 0")
            Text(text = "Distance travelled: 0 km")
            Text(text = "Calories burnt: 0 Kcal")

            Spacer(modifier = Modifier.padding(20.dp))
            Button(onClick = { navController.navigate("settings")}) {
                Text(text = "Go to Settings")
            }
            Button(onClick = { navController.navigate("about") }) {
                Text(text = "About")
            }
        }
    }
}

private var enteredWeight = mutableStateOf("")
private var enteredHeight = mutableStateOf("")
private var enteredStepGoal = mutableStateOf("")

@Composable
fun SettingsScreen(navController: NavController) {
    Surface(modifier = Modifier.fillMaxSize()){
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(text = "Settings", fontSize = 25.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.padding(10.dp))
            // Placeholder input fields for user settings
            Text(text = "Weight (kg):")
            // Added TextField for weight input
            TextField(
                value = enteredWeight.value,
                onValueChange = {enteredWeight.value = it},
                label = { Text("Enter Weight") }
            )
            Text(text = "Height (cm):")
            // Added TextField for height input
            TextField(
                value = enteredHeight.value,
                onValueChange = {enteredHeight.value = it},
                label = { Text("Enter Height") }
            )
            Text(text = "Daily Step Goal:")
            // Added TextField goal input
            TextField(
                value = enteredStepGoal.value,
                onValueChange = {enteredStepGoal.value = it},
                label = { Text("Enter Goal") }
            )
            Spacer(modifier = Modifier.padding(20.dp))
            Button(onClick = { navController.navigate("home") }) {
                Text(text = "Save and return to Dashboard")
            }
        }
    }
}

@Composable
fun AboutScreen(navController: NavController) {
    Surface(modifier = Modifier.fillMaxSize()){
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(text = "About the App", fontSize = 25.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.padding(10.dp))
            Text(text = "This app monitors the users daily physical activity. It allows the user to track their steps, calculate the distance travelled and display an amount of burned calories.")
            Spacer(modifier = Modifier.padding(10.dp))
            Button(onClick = { navController.navigate("home")}) {
                Text(text = "Return to Dashboard")
            }
        }
    }
}

