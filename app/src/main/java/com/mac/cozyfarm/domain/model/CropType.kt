package com.mac.cozyfarm.domain.model

import androidx.compose.ui.graphics.Color

enum class CropType(
    val id: Int,
    val displayName: String,
    val growthTimeMillis: Long,
    val seedPrice: Int,
    val sellPrice: Int,
    val color: Color
) {
    CARROT(1, "Carrot", 30000L, 10, 25, Color(0xFFF57C00)), // 30s
    TOMATO(2, "Tomato", 60000L, 25, 60, Color(0xFFD32F2F)), // 1m
    CORN(3, "Corn", 120000L, 50, 130, Color(0xFFFBC02D)), // 2m
    PUMPKIN(4, "Pumpkin", 300000L, 100, 280, Color(0xFFE64A19)) // 5m
}
