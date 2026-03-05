package com.mac.cozyfarm

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.mac.cozyfarm.ui.dashboard.DashboardScreen
import com.mac.cozyfarm.ui.theme.CozyFarmTheme
import com.mac.cozyfarm.ui.viewmodel.FarmViewModel
import com.mac.cozyfarm.ui.viewmodel.FarmViewModelFactory

private const val TAG = "CozyFarm_MainActivity"

class MainActivity : ComponentActivity() {
    private val viewModel: FarmViewModel by viewModels {
        FarmViewModelFactory((application as FarmApplication).repository, applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: Initializing MainActivity with DashboardScreen")
        enableEdgeToEdge()
        setContent {
            CozyFarmTheme {
                DashboardScreen(viewModel = viewModel)
            }
        }
    }
}
