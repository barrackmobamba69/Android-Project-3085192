//Name - Udayy Singh Pawar
//Student Number - 3085192
//Mobile Development [BSCH-MD/Dub/FT]
//Milestone 1 - Archive,Video and document

package com.griffith.androidproject3085192

import android.content.Context
import android.content.Intent
import android.net.Uri
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.griffith.androidproject3085192.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme(darkTheme = true){ // Applied dark theme to the app
                NavigationScreen() // Start the navigation screen
            }
        }
    }
}

// NavigationScreen: Composable function to handle navigation between different screens
@Composable
fun NavigationScreen(){
    val navController = rememberNavController()
    Surface(modifier = Modifier.fillMaxSize()){
        NavHost(
            navController = navController,
            startDestination = "home"){ // Setting dashboard/home screen as the start destination
            composable("home"){
                HomeScreen(navController) // Navigate to home screen
            }
            composable("settings"){
                SettingsScreen(navController) // Navigate to settings screen
            }
            composable("about"){
                AboutScreen(navController) // Navigate to about screen
            }
        }
    }
}

// HomeScreen: Composable function that displays the main dashboard and user's statistics
@Composable
fun HomeScreen(navController: NavController) {
    Surface(modifier = Modifier.fillMaxSize()){
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(text = "Welcome to the X-Fitness App", fontSize = 25.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.padding(10.dp))
            // Placeholder text for user's fitness statistics
            Text(text = "Daily Activity", fontSize = 20.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.padding(10.dp))
            Text(text = "Steps travelled: 0")
            Text(text = "Distance travelled: 0 km")
            Text(text = "Calories burnt: 0 cal")

            Spacer(modifier = Modifier.padding(20.dp))

            // Added an implicit intent to allow the user to share his/her progress to others
            Button(onClick = {
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, "Check out my progress on the X-Fitness App!")
                }
                navController.context.startActivity(Intent.createChooser(shareIntent, "Share your progress"))
            }){
                Text(text = "Share Progress")
            }
            // Button to navigate to settings screen
            Button(onClick = { navController.navigate("settings")}) {
                Text(text = "Go to Settings")
            }
            // Button to navigate to about screen
            Button(onClick = { navController.navigate("about") }) {
                Text(text = "About")
            }
        }
    }
}

// SettingsScreen: Composable function that displays the settings screen where user can input their weight, height and daily step goal
@Composable
fun SettingsScreen(navController: NavController) {
    val context = navController.context
    val sharedPrefs = context.getSharedPreferences("fitness_prefs", Context.MODE_PRIVATE)

    Surface(modifier = Modifier.fillMaxSize()){
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(text = "Settings", fontSize = 25.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.padding(10.dp))

            // Mutable states to store input values from the user
            var enteredWeight by remember { mutableStateOf("") }
            var enteredHeight by remember { mutableStateOf("") }
            var enteredStepGoal by remember { mutableStateOf("") }

            // Placeholder input fields for user settings
            Text(text = "Weight (kg):")
            // Added TextField for weight input
            TextField(
                value = enteredWeight,
                onValueChange = {enteredWeight = it},
                label = { Text("Enter Weight") }
            )
            Spacer(modifier = Modifier.padding(5.dp))
            Text(text = "Height (cm):")
            // Added TextField for height input
            TextField(
                value = enteredHeight,
                onValueChange = {enteredHeight = it},
                label = { Text("Enter Height") }
            )
            Spacer(modifier = Modifier.padding(5.dp))
            Text(text = "Daily Step Goal:")
            // Added TextField for goal input
            TextField(
                value = enteredStepGoal,
                onValueChange = {enteredStepGoal = it},
                label = { Text("Enter Goal") }
            )
            Spacer(modifier = Modifier.padding(20.dp))
            // Button to save settings and return to dashboard (WIP | This will be done in the upcoming milestones)
            Button(onClick = { navController.navigate("home") }) {
                Text(text = "Save and return to Dashboard")
            }
        }
    }
}

// AboutScreen: Composable function that displays the about screen and an external link to the example website
@Composable
fun AboutScreen(navController: NavController) {
    Surface(modifier = Modifier.fillMaxSize()){
        Column(
            // Added padding because to prevent the text from touching the screen edge
            modifier = Modifier.padding(horizontal = 27.dp),
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
            // Added an implicit intent to open an example website
            Button(onClick = {
                val context =  navController.context
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.example.com"))
                context.startActivity(intent)
            }) {
                Text(text = "Visit Website"  )
            }

        }
    }
}

