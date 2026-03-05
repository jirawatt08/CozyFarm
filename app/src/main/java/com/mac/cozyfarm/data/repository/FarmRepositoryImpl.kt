package com.mac.cozyfarm.data.repository

import com.mac.cozyfarm.data.local.dao.FarmDao
import com.mac.cozyfarm.data.local.entity.CropPlotEntity
import com.mac.cozyfarm.data.local.entity.PlayerStatsEntity
import com.mac.cozyfarm.domain.model.CropType
import com.mac.cozyfarm.domain.repository.FarmRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class FarmRepositoryImpl(
    private val farmDao: FarmDao
) : FarmRepository {

    override fun getAllPlots(): Flow<List<CropPlotEntity>> = farmDao.getAllPlots()

    override fun getPlayerStats(): Flow<PlayerStatsEntity?> = farmDao.getPlayerStats()

    override suspend fun plantCrop(plotId: Long, cropType: CropType): Result<Unit> {
        val stats = farmDao.getPlayerStatsSync() ?: PlayerStatsEntity(coins = 100)
        if (stats.coins < cropType.seedPrice) {
            return Result.failure(Exception("Not enough coins"))
        }

        val plot = farmDao.getPlotById(plotId) ?: return Result.failure(Exception("Plot not found"))
        if (plot.cropTypeId != null) {
            return Result.failure(Exception("Plot already occupied"))
        }

        farmDao.updatePlayerStats(stats.copy(coins = stats.coins - cropType.seedPrice))
        farmDao.updatePlot(plot.copy(
            cropTypeId = cropType.id,
            plantedAt = System.currentTimeMillis()
        ))
        return Result.success(Unit)
    }

    override suspend fun harvestCrop(plotId: Long): Result<Unit> {
        val plot = farmDao.getPlotById(plotId) ?: return Result.failure(Exception("Plot not found"))
        val cropType = CropType.values().find { it.id == plot.cropTypeId }
            ?: return Result.failure(Exception("No crop to harvest"))

        val plantedAt = plot.plantedAt ?: return Result.failure(Exception("Invalid crop state"))
        val now = System.currentTimeMillis()
        if (now - plantedAt < cropType.growthTimeMillis) {
            return Result.failure(Exception("Crop not yet grown"))
        }

        farmDao.harvestCrop(plotId, cropType.sellPrice)
        return Result.success(Unit)
    }

    override suspend fun initializeGame() {
        val plots = farmDao.getAllPlots().first()
        if (plots.isEmpty()) {
            for (i in 0 until 6) {
                farmDao.insertPlot(CropPlotEntity(plotIndex = i, cropTypeId = null, plantedAt = null))
            }
        }
        val stats = farmDao.getPlayerStatsSync()
        if (stats == null) {
            farmDao.updatePlayerStats(PlayerStatsEntity(coins = 50))
        }
    }
}
