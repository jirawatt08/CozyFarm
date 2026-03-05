package com.mac.cozyfarm.ui.viewmodel

import android.content.Context
import android.util.Log
import androidx.glance.appwidget.updateAll
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mac.cozyfarm.data.local.entity.PlayerStatsEntity
import com.mac.cozyfarm.domain.model.CropPlot
import com.mac.cozyfarm.domain.model.CropType
import com.mac.cozyfarm.domain.repository.FarmRepository
import com.mac.cozyfarm.ui.widget.FarmWidget
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

private const val TAG = "CozyFarm_VM"

data class FarmUiState(
    val plots: List<CropPlot> = emptyList(),
    val stats: PlayerStatsEntity = PlayerStatsEntity(coins = 0),
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val currentTime: Long = System.currentTimeMillis()
)

class FarmViewModel(
    private val repository: FarmRepository,
    private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(FarmUiState())
    val uiState: StateFlow<FarmUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.initializeGame()
            
            combine(
                repository.getAllPlots(),
                repository.getPlayerStats()
            ) { plots, stats ->
                plots.map { entity ->
                    CropPlot(
                        id = entity.id,
                        cropType = CropType.entries.find { it.id == entity.cropTypeId },
                        plantedAt = entity.plantedAt,
                        plotIndex = entity.plotIndex
                    )
                } to (stats ?: PlayerStatsEntity(coins = 0))
            }.collect { (plots, stats) ->
                _uiState.update { 
                    it.copy(
                        plots = plots,
                        stats = stats,
                        isLoading = false
                    )
                }
                Log.d(TAG, "Data changed, updating widget")
                FarmWidget().updateAll(context)
            }
        }

        // Timer for updating UI (growth progress)
        viewModelScope.launch {
            while (true) {
                delay(1000)
                val now = System.currentTimeMillis()
                Log.d(TAG, "Timer loop: currentTime = $now")
                _uiState.update { it.copy(currentTime = now) }
                // Also trigger widget update periodically to show progress
                Log.d(TAG, "Triggering widget update from timer loop")
                FarmWidget().updateAll(context)
            }
        }
    }

    fun plantCrop(plotId: Long, cropType: CropType) {
        viewModelScope.launch {
            val result = repository.plantCrop(plotId, cropType)
            if (result.isFailure) {
                _uiState.update { it.copy(errorMessage = result.exceptionOrNull()?.message) }
            }
        }
    }

    fun harvestCrop(plotId: Long) {
        viewModelScope.launch {
            val result = repository.harvestCrop(plotId)
            if (result.isFailure) {
                _uiState.update { it.copy(errorMessage = result.exceptionOrNull()?.message) }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}

class FarmViewModelFactory(
    private val repository: FarmRepository,
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FarmViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FarmViewModel(repository, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
