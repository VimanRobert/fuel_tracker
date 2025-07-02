package com.fueltracker.presentation.utils

import android.graphics.Color
import android.util.Log
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
            description.textSize = 24f

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

    fun setupGeneralCharts(chart: LineChart, entries: List<Entry>, labels: List<String>) {
        val dataSet = LineDataSet(entries, "General Avg Fuel").apply {
            color = Color.BLUE
            setCircleColor(Color.RED)
            circleRadius = 4f
            valueTextSize = 24f
            lineWidth = 2f
            setDrawValues(true)
        }

        chart.apply {
            data = LineData(dataSet)
            xAxis.valueFormatter = IndexAxisValueFormatter(labels)
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.granularity = 1f
            xAxis.labelRotationAngle = -45f
            axisLeft.axisMinimum = 0f
            axisRight.isEnabled = false
            description.text = "Fuel Consumption History"
            description.textSize = 24f
            legend.isEnabled = true
            setTouchEnabled(true)
            setPinchZoom(true)
            invalidate()
            visibility = View.VISIBLE
        }
    }

    fun setupFuelChart(chart: LineChart, entries: List<Entry>, labels: List<String>) {
        val dataSet = LineDataSet(entries, "Fuel Used (L)").apply {
            color = Color.YELLOW
            setCircleColor(Color.BLACK)
            circleRadius = 4f
            valueTextSize = 24f
            lineWidth = 2f
            setDrawValues(true)
        }

        chart.apply {
            data = LineData(dataSet)
            xAxis.apply {
                valueFormatter = IndexAxisValueFormatter(labels)
                position = XAxis.XAxisPosition.BOTTOM
                granularity = 1f
                isGranularityEnabled = true
                labelRotationAngle = -45f
            }
            axisLeft.axisMinimum = 0f
            axisRight.isEnabled = false
            description.text = "Fuel Usage Over Time"
            description.textSize = 24f
            legend.isEnabled = true
            setTouchEnabled(true)
            setPinchZoom(true)
            invalidate()
        }
    }
}
