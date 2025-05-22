package com.fueltracker.presentation.utils

import android.graphics.Color
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

object ChartsHandler {

    private lateinit var linearDataset: LineDataSet

    fun createLinearChart(entries: List<Entry>, lineChart: LineChart?) {
        linearDataset = LineDataSet(entries, "Fuel consumption flow")
        linearDataset.color = Color.BLUE
        linearDataset.valueTextColor = Color.BLACK
        linearDataset.lineWidth = 2f
        linearDataset.circleRadius = 5f

        val lineData = LineData(linearDataset)

        lineChart?.data = lineData
        lineChart?.description?.text = "Daily Fuel Usage"
        lineChart?.animateX(3000)
        lineChart?.invalidate()
    }
}
