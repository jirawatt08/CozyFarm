package com.mac.cozyfarm.domain.model

data class CropPlot(
    val id: Long,
    val cropType: CropType?,
    val plantedAt: Long?,
    val plotIndex: Int
) {
    val isGrown: Boolean
        get() {
            val type = cropType ?: return false
            val planted = plantedAt ?: return false
            return System.currentTimeMillis() - planted >= type.growthTimeMillis
        }

    val progress: Float
        get() {
            val type = cropType ?: return 0f
            val planted = plantedAt ?: return 0f
            val elapsed = System.currentTimeMillis() - planted
            return (elapsed.toFloat() / type.growthTimeMillis).coerceIn(0f, 1f)
        }
    
    val remainingTimeMillis: Long
        get() {
            val type = cropType ?: return 0L
            val planted = plantedAt ?: return 0L
            val elapsed = System.currentTimeMillis() - planted
            return (type.growthTimeMillis - elapsed).coerceAtLeast(0L)
        }
}
