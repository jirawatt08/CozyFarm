package com.mac.cozyfarm.ui.dashboard

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mac.cozyfarm.domain.model.CropPlot
import com.mac.cozyfarm.domain.model.CropType
import com.mac.cozyfarm.ui.viewmodel.FarmViewModel

private const val TAG = "CozyFarm_Dashboard"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(viewModel: FarmViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    var showShopDialog by remember { mutableStateOf<Long?>(null) }

    Log.d(TAG, "Recomposing with currentTime: ${uiState.currentTime}")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CozyFarm", fontWeight = FontWeight.Bold) },
                actions = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(end = 16.dp)
                    ) {
                        Icon(
                            Icons.Default.MonetizationOn,
                            contentDescription = "Coins",
                            tint = Color(0xFFFFD700)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            "${uiState.stats.coins}",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                "My Farm",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(uiState.plots) { plot ->
                    PlotCard(
                        plot = plot,
                        currentTime = uiState.currentTime,
                        onPlantClick = { showShopDialog = plot.id },
                        onHarvestClick = { viewModel.harvestCrop(plot.id) }
                    )
                }
            }
        }

        if (showShopDialog != null) {
            ShopDialog(
                onDismiss = { showShopDialog = null },
                onCropSelected = { cropType ->
                    viewModel.plantCrop(showShopDialog!!, cropType)
                    showShopDialog = null
                },
                currentCoins = uiState.stats.coins
            )
        }

        uiState.errorMessage?.let { message ->
            AlertDialog(
                onDismissRequest = { viewModel.clearError() },
                title = { Text("Error") },
                text = { Text(message) },
                confirmButton = {
                    TextButton(onClick = { viewModel.clearError() }) {
                        Text("OK")
                    }
                }
            )
        }
    }
}

@Composable
fun PlotCard(
    plot: CropPlot,
    currentTime: Long,
    onPlantClick: () -> Unit,
    onHarvestClick: () -> Unit
) {
    // Re-calculate based on passed currentTime to force recomposition
    val progress = plot.progress
    val isGrown = plot.isGrown
    val remainingSecs = plot.remainingTimeMillis / 1000

    Log.d("CozyFarm_Plot", "Plot ${plot.id} progress: $progress")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (plot.cropType == null) {
                IconButton(
                    onClick = onPlantClick,
                    modifier = Modifier
                        .size(64.dp)
                        .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(32.dp))
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Plant",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        plot.cropType.displayName,
                        style = MaterialTheme.typography.titleMedium,
                        color = plot.cropType.color
                    )
                    Spacer(Modifier.height(8.dp))
                    
                    if (isGrown) {
                        Button(onClick = onHarvestClick) {
                            Text("Harvest")
                        }
                    } else {
                        Box(contentAlignment = Alignment.Center) {
                            LinearProgressIndicator(
                                progress = { progress },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(24.dp)
                                    .clip(RoundedCornerShape(12.dp)),
                                color = plot.cropType.color,
                                trackColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                            Text(
                                text = "${(progress * 100).toInt()}%",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "${remainingSecs}s remaining",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ShopDialog(
    onDismiss: () -> Unit,
    onCropSelected: (CropType) -> Unit,
    currentCoins: Int
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Seed") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                CropType.entries.forEach { crop ->
                    val canAfford = currentCoins >= crop.seedPrice
                    ListItem(
                        headlineContent = { Text(crop.displayName) },
                        supportingContent = { Text("Cost: ${crop.seedPrice} | Value: ${crop.sellPrice}") },
                        trailingContent = {
                            Button(
                                onClick = { onCropSelected(crop) },
                                enabled = canAfford
                            ) {
                                Text("Buy")
                            }
                        },
                        colors = ListItemDefaults.colors(
                            containerColor = Color.Transparent
                        )
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
