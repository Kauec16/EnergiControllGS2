package com.example.energycontrol

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.energycontrol.databaseHandler.DatabaseHandler
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate

class Chart : AppCompatActivity() {

    public lateinit var barChart: BarChart
    val dbHandler = DatabaseHandler(this)

    lateinit var spinnerValue: Spinner

    lateinit var dataList: MutableList<Pair<String, Double>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_chart)

        prepareSpinner()

    }

    fun prepareSpinner() {
        val groupList = dbHandler.listGroups()
        val nomes = groupList.map { it.name }

        val spinner: Spinner = findViewById(R.id.GroupInputChart)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, nomes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>,
                selectedItemView: android.view.View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parentView.getItemAtPosition(position) as String
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
            }
        }
    }


    fun backScreen(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun reloadChart(view: View){
        spinnerValue = findViewById(R.id.GroupInputChart)
        dataList = dbHandler.generateChart(spinnerValue.selectedItem.toString());

        var barChart: BarChart = findViewById(R.id.barChart)

        val entries = ArrayList<BarEntry>()
        val labels = ArrayList<String>()

        for (i in dataList.indices) {
            val pair = dataList[i]
            entries.add(
                BarEntry(
                    i.toFloat(),
                    pair.second.toFloat()
                )
            )
            labels.add(pair.first)
        }


        val dataSet = BarDataSet(entries, "Exemplo de Dados")
        dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()

        // Criando o objeto BarData
        val barData = BarData(dataSet)
        barChart.data = barData

        // Configuração do eixo X (Textos)
        val xAxis: XAxis = barChart.xAxis
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM)
        xAxis.textSize = 12f


        xAxis.valueFormatter = IndexAxisValueFormatter(labels)


        val leftAxis: YAxis = barChart.axisLeft
        leftAxis.textSize = 12f
        leftAxis.axisMinimum = 0f
        leftAxis.setDrawGridLines(true)


        barChart.axisRight.isEnabled = false

        barChart.invalidate()
    }



}

