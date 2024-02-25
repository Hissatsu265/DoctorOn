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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chiso_bmi_hr_calo)

        val btn_back=findViewById<ImageView>(R.id.back_btn)
        btn_back.setOnClickListener {
            finish()
        }
        //---------------------------------------------------------------------
        lineGraphView = findViewById(R.id.idGraphView_heartrace)

        val series: LineGraphSeries<DataPoint> = LineGraphSeries(
            arrayOf(
                DataPoint( 1.0,18.0),
                DataPoint( 2.0,18.2),
                DataPoint( 3.0,19.0),
                DataPoint( 4.0,18.5),
                DataPoint( 5.0,23.1),
                DataPoint( 6.0,18.2),
                DataPoint( 7.0,18.2),
                DataPoint( 8.0,19.0),
                DataPoint( 9.0,21.1)
            )
        )
        series.color=R.color.cyan_300
        series.thickness = 20
        lineGraphView.addSeries(series)
        //----------------------------------------------------------------

        val series1: LineGraphSeries<DataPoint> = LineGraphSeries(
            arrayOf(
                DataPoint( 1.0,81.0),
                DataPoint( 2.0,81.2),
                DataPoint( 3.0,91.0),
                DataPoint( 4.0,81.5),
                DataPoint( 5.0,83.1),
                DataPoint( 6.0,88.2),
                DataPoint( 7.0,88.2),
                DataPoint( 8.0,99.0),
                DataPoint( 9.0,81.1)
            )
        )
        series1.color = ContextCompat.getColor(this, R.color.purple_500)
        series1.thickness = 10
        lineGraphView.addSeries(series1)
        lineGraphView.animate()
        lineGraphView.viewport.isScrollable = true
        lineGraphView.viewport.isScalable = true
        lineGraphView.viewport.setScalableY(true)
        lineGraphView.viewport.setScrollableY(true)
    }
}