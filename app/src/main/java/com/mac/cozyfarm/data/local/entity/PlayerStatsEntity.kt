package com.mac.cozyfarm.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "player_stats")
data class PlayerStatsEntity(
    @PrimaryKey val id: Int = 1, // Only one player stats row
    val coins: Int,
    val level: Int = 1,
    val experience: Int = 0
)
