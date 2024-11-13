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
            composable("menu"){
                AboutScreen(navController)
                }
            composable("options"){
                SettingsScreen(navController)
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
            Text(text = "Welcome to the Fitness App", fontSize = 25.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.padding(7.dp))
            Text(
                text = "The #1 personal training app",
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.padding(20.dp))
            Button(onClick = { navController.navigate("menu") }) {
                Text(text = "Go to Menu")
            }
            Button(onClick = { navController.navigate("options")}) {
                Text(text = "Go to Options")
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
            Spacer(modifier = Modifier.padding(10.dp))
            Button(onClick = { navController.navigate("home") }) {
                Text(text = "Go to Home")
            }
            Button(onClick = { navController.navigate("options")}) {
                Text(text = "Go to Options")
            }
        }
    }
}

private var enteredText = mutableStateOf("")

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
                value = enteredText.value,
                onValueChange = {enteredText.value = it},
                label = { Text("Enter Weight") }
            )
            Text(text = "Height (cm):")
            // Added TextField for height input
            TextField(
                value = enteredText.value,
                onValueChange = {enteredText.value = it},
                label = { Text("Enter Height") }
            )
            Text(text = "Daily Step Goal:")
            // Added TextField goal input
            TextField(
                value = enteredText.value,
                onValueChange = {enteredText.value = it},
                label = { Text("Enter Goal") }
            )
            Spacer(modifier = Modifier.padding(20.dp))
            Button(onClick = { navController.navigate("home") }) {
                Text(text = "Save and return to Dashboard")
            }
        }
    }
}

