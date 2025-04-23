package com.fueltracker.presentation.utils

import android.graphics.Color
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

object ChartsHandler {

    private lateinit var linearDataset: LineDataSet

    fun createLinearChart(entries: List<Entry>) : LineData {
        linearDataset = LineDataSet(entries, "Fuel consumption flow")
        linearDataset.color = Color.BLUE
        linearDataset.valueTextColor = Color.BLACK
        linearDataset.lineWidth = 2f
        linearDataset.circleRadius = 5f

        return LineData(linearDataset)
    }
}