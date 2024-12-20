package com.griffith.androidproject3085192

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.griffith.androidproject3085192.ui.theme.AppTheme

// NavigationItem data class
data class NavigationItem(val label: String, val route: String, val icon: ImageVector)

// BottomNavigationBar composable
@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        NavigationItem("Home", "home", Icons.Default.Home),
        NavigationItem("Settings", "settings", Icons.Default.Settings),
        NavigationItem("About", "about", Icons.Default.Info)
    )
    NavigationBar {
        val currentRoute = navController.currentDestination?.route
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

class FitnessViewModel : ViewModel(), SensorEventListener {
    private var sensorManager: SensorManager? = null
    private var accelerometer: Sensor? = null

    private var _steps = mutableStateOf(0)
    val steps: State<Int> = _steps

    private var _distance = mutableStateOf(0.0f)
    val distance: State<Float> = _distance

    private var _calories = mutableStateOf(0.0f)
    val calories: State<Float> = _calories

    private var lastMagnitude = 0.0f
    private val stepThreshold = 10.0f
    private lateinit var sharedPreferences: SharedPreferences

    @RequiresApi(Build.VERSION_CODES.O)
    fun initializeSensors(context: Context) {
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager?.registerListener(
            this,
            accelerometer,
            SensorManager.SENSOR_DELAY_NORMAL
        )

        // Initialize SharedPreferences
        sharedPreferences = context.getSharedPreferences("FitnessData", Context.MODE_PRIVATE)

        // Load today's data
        loadTodayData()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadTodayData() {
        val today = java.time.LocalDate.now().toString()
        val savedDate = sharedPreferences.getString("last_saved_date", "")

        if (savedDate == today) {
            // Load today's data
            _steps.value = sharedPreferences.getInt("steps", 0)
            _distance.value = sharedPreferences.getFloat("distance", 0f)
            _calories.value = sharedPreferences.getFloat("calories", 0f)
        } else {
            // Reset data for new day
            _steps.value = 0
            _distance.value = 0f
            _calories.value = 0f
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveTodayData() {
        val editor = sharedPreferences.edit()
        editor.putString("last_saved_date", java.time.LocalDate.now().toString())
        editor.putInt("steps", _steps.value)
        editor.putFloat("distance", _distance.value)
        editor.putFloat("calories", _calories.value)
        editor.apply()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            if (it.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                val x = it.values[0]
                val y = it.values[1]
                val z = it.values[2]

                val magnitude = kotlin.math.sqrt((x * x + y * y + z * z).toDouble()).toFloat()
                val delta = magnitude - lastMagnitude
                lastMagnitude = magnitude

                if (delta > stepThreshold) {
                    _steps.value += 1
                    updateDistanceAndCalories()
                    // Save data after each update
                    saveTodayData()
                }
            }
        }
    }

    private fun updateDistanceAndCalories() {
        _distance.value = _steps.value * 0.7f
        _calories.value = _steps.value * 0.04f
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCleared() {
        super.onCleared()
        sensorManager?.unregisterListener(this)
        // Save data when ViewModel is cleared
        saveTodayData()
    }
}
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                NavigationScreen()
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationScreen() {
    val navController = rememberNavController()
    val fitnessViewModel: FitnessViewModel = viewModel()
    val context = LocalContext.current

    DisposableEffect(Unit) {
        fitnessViewModel.initializeSensors(context)
        onDispose { }
    }

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") { HomeScreen(fitnessViewModel, navController) }
            composable("settings") { SettingsScreen() }
            composable("about") { AboutScreen(navController) }
        }
    }
}

@Composable
fun HomeScreen(viewModel: FitnessViewModel, navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Steps Taken",style = MaterialTheme.typography.titleMedium)
                Text("${viewModel.steps.value}",style = MaterialTheme.typography.headlineMedium)

                Spacer(modifier = Modifier.height(16.dp))

                Text("Distance Traveled",style = MaterialTheme.typography.titleMedium)
                Text(String.format("%.2f meters", viewModel.distance.value),style = MaterialTheme.typography.headlineMedium)

                Spacer(modifier = Modifier.height(16.dp))

                Text("Calories Burned",style = MaterialTheme.typography.titleMedium)
                Text(String.format("%.1f kcal", viewModel.calories.value),style = MaterialTheme.typography.headlineMedium)
            }
        }
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
    }
}

@Composable
fun SettingsScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Settings Screen", style = MaterialTheme.typography.titleLarge)
    }
}

@Composable
fun AboutScreen(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Surface(modifier = Modifier.fillMaxSize()){
            Column(
                // Added padding because to prevent the text from touching the screen edge
                modifier = Modifier.padding(horizontal = 27.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(text = "About the App", fontSize = 25.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.padding(10.dp))
                Text(text = "This app monitors the users daily physical activity. It allows the user to track their steps, calculate the distance travelled and display an amount of burned calories.", fontSize = 14.sp, style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.padding(10.dp))
                Button(onClick = { navController.navigate("home")}) {
                    Text(text = "Return to Dashboard")
                }
                // Added an implicit intent to open an example website
                Button(onClick = {
                    val context =  navController.context
                    val intent = Intent(Intent.ACTION_VIEW, android.net.Uri.parse(("https://www.example.com")))
                    context.startActivity(intent)
                }) {
                    Text(text = "Visit Website")
                }
            }
        }
    }
}