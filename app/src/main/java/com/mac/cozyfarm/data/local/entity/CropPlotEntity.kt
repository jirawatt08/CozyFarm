package com.mac.cozyfarm.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mac.cozyfarm.domain.model.CropType

@Entity(tableName = "crop_plots")
data class CropPlotEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val cropTypeId: Int?, // null if empty
    val plantedAt: Long?, // null if empty
    val plotIndex: Int
)
