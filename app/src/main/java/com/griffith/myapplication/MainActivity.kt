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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.griffith.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

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
                MenuScreen(navController)
                }
            composable("options"){
                OptionsScreen(navController)
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
            Text(text = "Home")
            Spacer(modifier = Modifier.padding(10.dp))
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
fun MenuScreen(navController: NavController) {
    Surface(modifier = Modifier.fillMaxSize()){
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(text = "Menu")
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

@Composable
fun OptionsScreen(navController: NavController) {
    Surface(modifier = Modifier.fillMaxSize()){
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(text = "Menu")
            Spacer(modifier = Modifier.padding(10.dp))
            Button(onClick = { navController.navigate("home") }) {
                Text(text = "Go to Home")
            }
            Button(onClick = { navController.navigate("menu")}) {
                Text(text = "Go to Menu")
            }
        }
    }
}

