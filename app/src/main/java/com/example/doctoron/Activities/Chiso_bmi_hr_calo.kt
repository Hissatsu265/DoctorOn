package com.example.doctoron.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.example.doctoron.R
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries

class Chiso_bmi_hr_calo : AppCompatActivity() {

    lateinit var lineGraphView: GraphView
    lateinit var lineGraphView1: GraphView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chiso_bmi_hr_calo)

        val btn_back=findViewById<ImageView>(R.id.back_btn)
        btn_back.setOnClickListener {
            finish()
        }
        //--------------------Khai báo------------------------------------------------
        lineGraphView = findViewById(R.id.idGraphView_month)
        lineGraphView1 = findViewById(R.id.idGraphView_year)
        lineGraphView.animate()
        lineGraphView.viewport.isScrollable = true
        lineGraphView.viewport.isScalable = true
        lineGraphView.viewport.setScalableY(true)
        lineGraphView.viewport.setScrollableY(true)
        lineGraphView1.animate()
        lineGraphView1.viewport.isScrollable = true
        lineGraphView1.viewport.isScalable = true
        lineGraphView1.viewport.setScalableY(true)
        lineGraphView1.viewport.setScrollableY(true)
        //-------------------------------------------------------------------
        var month_bmi:ArrayList<DataPoint> = ArrayList()
        for (i in 0 until 30) {
            month_bmi.add(DataPoint(i.toDouble(), 20.0))
        }

        var month_heartrace:ArrayList<DataPoint> = ArrayList()
        for (i in 0 until 30) {
            month_heartrace.add(DataPoint(i.toDouble(), 89.0))
        }
        AddSeries(month_bmi,0,0)
        AddSeries(month_heartrace,1,0)

        var year_heartrace:ArrayList<DataPoint> = ArrayList()
        for (i in 0 until 30) {
            year_heartrace.add(DataPoint(i.toDouble(), 91.0))
        }
        AddSeries(year_heartrace,1,1)
        //--------------------------------------------------------------------------------------
    }
    fun AddSeries(a:ArrayList<DataPoint>,color:Int,type:Int){
        val series: LineGraphSeries<DataPoint> = LineGraphSeries(a.toTypedArray())
        when (color){
            0 -> series.color=ContextCompat.getColor(this, R.color.cyan_300)
            1 -> series.color=ContextCompat.getColor(this, R.color.purple_500)
            2 -> series.color=ContextCompat.getColor(this, R.color.primaryContainer)
        }
        series.thickness=20
        when(type){
            0 -> lineGraphView.addSeries(series)
            1 -> lineGraphView1.addSeries(series)
        }
    }
}