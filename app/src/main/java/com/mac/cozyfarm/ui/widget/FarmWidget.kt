package com.mac.cozyfarm.ui.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.*
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.*
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.mac.cozyfarm.FarmApplication
import com.mac.cozyfarm.domain.model.CropType
import com.mac.cozyfarm.domain.model.CropPlot

class FarmWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val repository = (context.applicationContext as FarmApplication).repository
        
        provideContent {
            val plots by repository.getAllPlots().collectAsState(initial = emptyList())
            val stats by repository.getPlayerStats().collectAsState(initial = null)
            
            val domainPlots = plots.map { entity ->
                CropPlot(
                    id = entity.id,
                    cropType = CropType.entries.find { it.id == entity.cropTypeId },
                    plantedAt = entity.plantedAt,
                    plotIndex = entity.plotIndex
                )
            }

            FarmWidgetContent(domainPlots, stats?.coins ?: 0)
        }
    }

    @Composable
    private fun FarmWidgetContent(plots: List<CropPlot>, coins: Int) {
        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .padding(8.dp)
                .background(ColorProvider(androidx.compose.ui.graphics.Color(0xFFF7FBF1))),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = GlanceModifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$coins 🪙",
                    style = TextStyle(fontSize = 14.sp)
                )
            }

            Spacer(GlanceModifier.height(8.dp))

            plots.take(3).forEach { plot ->
                PlotItem(plot)
                Spacer(GlanceModifier.height(4.dp))
            }
        }
    }

    @Composable
    private fun PlotItem(plot: CropPlot) {
        Row(
            modifier = GlanceModifier
                .fillMaxWidth()
                .padding(4.dp)
                .background(ColorProvider(androidx.compose.ui.graphics.Color(0xFFB9F0B9))),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val cropName = plot.cropType?.displayName ?: "Empty"
            Text(
                text = "P${plot.plotIndex + 1}: $cropName",
                modifier = GlanceModifier.defaultWeight(),
                style = TextStyle(fontSize = 12.sp)
            )

            if (plot.cropType == null) {
                Button(
                    text = "Plant",
                    onClick = actionRunCallback<PlantActionCallback>(
                        actionParametersOf(
                            ActionParameters.Key<Long>("plotId") to plot.id,
                            ActionParameters.Key<Int>("cropTypeId") to CropType.CARROT.id
                        )
                    )
                )
            } else if (plot.isGrown) {
                Button(
                    text = "Harvest",
                    onClick = actionRunCallback<HarvestActionCallback>(
                        actionParametersOf(
                            ActionParameters.Key<Long>("plotId") to plot.id
                        )
                    )
                )
            } else {
                val progressPercent = (plot.progress * 100).toInt()
                Text(
                    text = "$progressPercent%",
                    style = TextStyle(fontSize = 12.sp)
                )
            }
        }
    }
}

class PlantActionCallback : ActionCallback {
    override suspend fun onAction(context: Context, glanceId: GlanceId, parameters: ActionParameters) {
        val plotId = parameters[ActionParameters.Key<Long>("plotId")] ?: return
        val cropTypeId = parameters[ActionParameters.Key<Int>("cropTypeId")] ?: return
        val cropType = CropType.entries.find { it.id == cropTypeId } ?: return
        
        val repository = (context.applicationContext as FarmApplication).repository
        repository.plantCrop(plotId, cropType)
        FarmWidget().update(context, glanceId)
    }
}

class HarvestActionCallback : ActionCallback {
    override suspend fun onAction(context: Context, glanceId: GlanceId, parameters: ActionParameters) {
        val plotId = parameters[ActionParameters.Key<Long>("plotId")] ?: return
        
        val repository = (context.applicationContext as FarmApplication).repository
        repository.harvestCrop(plotId)
        FarmWidget().update(context, glanceId)
    }
}
