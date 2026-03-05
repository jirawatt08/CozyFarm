package com.mac.cozyfarm.domain.repository

import com.mac.cozyfarm.data.local.entity.CropPlotEntity
import com.mac.cozyfarm.data.local.entity.PlayerStatsEntity
import com.mac.cozyfarm.domain.model.CropType
import kotlinx.coroutines.flow.Flow

interface FarmRepository {
    fun getAllPlots(): Flow<List<CropPlotEntity>>
    fun getPlayerStats(): Flow<PlayerStatsEntity?>
    suspend fun plantCrop(plotId: Long, cropType: CropType): Result<Unit>
    suspend fun harvestCrop(plotId: Long): Result<Unit>
    suspend fun initializeGame()
}
