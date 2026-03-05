package com.mac.cozyfarm.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mac.cozyfarm.data.local.dao.FarmDao
import com.mac.cozyfarm.data.local.entity.CropPlotEntity
import com.mac.cozyfarm.data.local.entity.PlayerStatsEntity

@Database(
    entities = [CropPlotEntity::class, PlayerStatsEntity::class],
    version = 1,
    exportSchema = false
)
abstract class FarmDatabase : RoomDatabase() {
    abstract fun farmDao(): FarmDao

    companion object {
        const val DATABASE_NAME = "cozy_farm_db"
    }
}
