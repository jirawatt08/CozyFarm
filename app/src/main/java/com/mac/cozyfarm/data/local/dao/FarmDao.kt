package com.mac.cozyfarm.data.local.dao

import androidx.room.*
import com.mac.cozyfarm.data.local.entity.CropPlotEntity
import com.mac.cozyfarm.data.local.entity.PlayerStatsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FarmDao {
    @Query("SELECT * FROM crop_plots ORDER BY plotIndex ASC")
    fun getAllPlots(): Flow<List<CropPlotEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlot(plot: CropPlotEntity)

    @Update
    suspend fun updatePlot(plot: CropPlotEntity)

    @Query("SELECT * FROM player_stats WHERE id = 1")
    fun getPlayerStats(): Flow<PlayerStatsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePlayerStats(stats: PlayerStatsEntity)

    @Transaction
    suspend fun harvestCrop(plotId: Long, coinsToAdd: Int) {
        val plot = getPlotById(plotId)
        if (plot != null) {
            updatePlot(plot.copy(cropTypeId = null, plantedAt = null))
            val currentStats = getPlayerStatsSync() ?: PlayerStatsEntity(coins = 0)
            updatePlayerStats(currentStats.copy(coins = currentStats.coins + coinsToAdd))
        }
    }

    @Query("SELECT * FROM crop_plots WHERE id = :plotId")
    suspend fun getPlotById(plotId: Long): CropPlotEntity?

    @Query("SELECT * FROM player_stats WHERE id = 1")
    suspend fun getPlayerStatsSync(): PlayerStatsEntity?
}
