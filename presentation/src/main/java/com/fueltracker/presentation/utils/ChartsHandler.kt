package com.fueltracker.presentation.utils

import android.graphics.Color
import android.view.View
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object ChartsHandler {

    private lateinit var linearDataset: LineDataSet

    fun createLinearChart(entries: List<Entry>, lineChart: LineDataSet?) {
        linearDataset = LineDataSet(entries, "Fuel consumption flow")
        linearDataset.color = Color.BLUE
        linearDataset.valueTextColor = Color.BLACK
        linearDataset.lineWidth = 2f
        linearDataset.circleRadius = 5f

        val lineData = LineData(linearDataset)

//        lineChart?.data = lineData
//        lineChart?.description?.text = "Daily Fuel Usage"
//        lineChart?.animateX(3000)
//        lineChart?.invalidate()

        lineChart?.color = Color.YELLOW
        lineChart?.valueTextColor = Color.BLUE
    }

    fun setupChart(chart: LineChart, entries: List<Entry>, labels: List<String>) {
        val dataSet = LineDataSet(entries, "Avg Fuel Consumption").apply {
            color = Color.BLUE
            valueTextSize = 24f
            setDrawValues(true)
            setCircleColor(Color.RED)
            circleRadius = 4f
            lineWidth = 2f
        }

        chart.apply {
            data = LineData(dataSet)
            description.text = "Medium Consumption (last 24h)"

            xAxis.apply {
                valueFormatter = IndexAxisValueFormatter(labels)
                granularity = 1f
                position = XAxis.XAxisPosition.BOTTOM
                labelRotationAngle = -45f
                setDrawGridLines(false)
            }

            axisLeft.axisMinimum = 0f
            axisRight.isEnabled = false

            setTouchEnabled(true)
            setScaleEnabled(true)
            setPinchZoom(true)

            legend.isEnabled = true
            invalidate()
            visibility = View.VISIBLE
        }
    }
}
