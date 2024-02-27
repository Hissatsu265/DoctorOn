package com.example.doctoron.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.doctoron.R
import com.google.firebase.firestore.FirebaseFirestore
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import java.text.DecimalFormat
import java.util.Calendar
import kotlin.math.log


class Chiso_bmi_hr_calo : AppCompatActivity() {

    lateinit var lineGraphView: GraphView
    lateinit var lineGraphView1: GraphView

    var month:String=getCurrentDate()
    // sau này nhớ đặt lại --------------------------------------
    lateinit var chisotb_bmi1:TextView
    lateinit var chisotb_calo1:TextView
    lateinit var chisotb_hr1:TextView

    val userid : String = "vtnB0tljUVnbTZLCqgPD"
    val db = FirebaseFirestore.getInstance()
    //-----------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chiso_bmi_hr_calo)

        //--------------------Khai báo------------------------------------------------
        lineGraphView = findViewById(R.id.idGraphView_month)
        lineGraphView1 = findViewById(R.id.idGraphView_year)
        val db = FirebaseFirestore.getInstance()
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
        Capnhapchiso()
        //--------------------------------------------------------------------------------------
        chisotb_bmi1=findViewById(R.id.chisotb_bmi)
        chisotb_calo1=findViewById(R.id.chisotb_calo)
        chisotb_hr1=findViewById(R.id.chisotb_hr)

        var btn_update1=findViewById<Button>(R.id.btn_update)
        var edt_bmi=findViewById<EditText>(R.id.update_bmi)
        var edt_calo=findViewById<EditText>(R.id.update_calo)
        var edt_hr=findViewById<EditText>(R.id.update_hr)

        val edt_month=findViewById<EditText>(R.id.month)
        val btn_back=findViewById<ImageView>(R.id.back_btn)
        val btn_kt=findViewById<Button>(R.id.btn_kt)
        edt_month.setText(month)
        btn_back.setOnClickListener {
            finish()
        }
        btn_kt.setOnClickListener {
            var t=edt_month.text.toString().toInt()
            if (t in 1..12){
                month=edt_month.text.toString()
                Capnhapchiso()
            }
        }
        btn_update1.setOnClickListener {
            val so1:Double=edt_bmi.text.toString().toDouble()
            val so2=edt_calo.text.toString().toDouble()
            val so3=edt_hr.text.toString().toDouble()
            val month1=getCurrentDate()
            db.collection("Chiso")
                .document(userid)
                .get()
                .addOnSuccessListener { document ->
                    run {
                        if (document != null) {
                            val bmi = document.get("bmi_$month1") as? ArrayList<Double>
                            val calo = document.get("calo_$month1") as? ArrayList<Double>
                            val heartrace = document.get("heartrace_$month1") as? ArrayList<Double>

                            Updatechiso(bmi,so1)
                            Updatechiso(calo,so2)
                            Updatechiso(heartrace,so3)
                        }
                    }
                }
        }
    }

    fun Capnhapchiso(){
//        Toast.makeText(this,"hú",Toast.LENGTH_SHORT).show()
        lineGraphView.removeAllSeries()
        db.collection("Chiso")
            .document(userid)
            .get()
            .addOnSuccessListener { document ->
                run {
                    if (document != null) {
                        val bmi = document.get("bmi_$month") as? ArrayList<Double>
                        val calo = document.get("calo_$month") as? ArrayList<Double>
                        val heartrace = document.get("heartrace_$month") as? ArrayList<Double>

                        chisotb_bmi1.setText(TinhchisoTB(bmi))
                        chisotb_calo1.setText(TinhchisoTB(calo))
                        chisotb_hr1.setText(TinhchisoTB(heartrace))

                        try{
                            AddSeries(bmi, 0, 0)
                            AddSeries(calo, 1, 0)
                            AddSeries(heartrace, 2, 0)
                        }
                        catch (e:Exception){
                            Log.d("huhuhu",e.message.toString())
                        }
                    }
                }
            }
    }
    fun getCurrentDate(): String {
        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH) + 1
        val year = calendar.get(Calendar.YEAR)
        return month.toString()
    }

    fun TinhchisoTB(a:ArrayList<Double>?):String{
        var tb:Double=0.0

        if (a != null) {
            for (i in a ){
                tb+=i
            }
            tb=tb/a.size

        }
        val decimalFormat = DecimalFormat("#.###")
        val formattedNumber = decimalFormat.format(tb)
        return formattedNumber.toString()
    }
    fun AddSeries(a:ArrayList<Double>?,color:Int,type:Int){
        var index:Int=0
        var mangchiso:ArrayList<DataPoint> = ArrayList()
        if (a != null) {
            for(i in a){
                if ( i > 0.0){
                    mangchiso.add(DataPoint(index.toDouble(),i.toDouble()))
                    index=index+1
                }
            }
        }
        val series: LineGraphSeries<DataPoint> = LineGraphSeries(mangchiso.toTypedArray())
        when (color){
            0 -> series.color=ContextCompat.getColor(this, R.color.cyan_300)
            1 -> series.color=ContextCompat.getColor(this, R.color.purple_500)
            2 -> series.color=ContextCompat.getColor(this, R.color.primaryContainer)
        }
        series.thickness=15
        when(type){
            0 -> lineGraphView.addSeries(series)
            1 -> lineGraphView1.addSeries(series)
        }
    }
    fun Updatechiso(a:ArrayList<Double>?,b:Double){
//        val calendar = Calendar.getInstance()
//        val day = calendar.get(Calendar.DAY_OF_MONTH)
//        val arrayList: ArrayList<Double> = List(31) { 0.0 }.toMutableList() as ArrayList<Double>
//
//        if (a!=null){
//            for (i in a){
//               arrayList.set()
//            }
//        }
    }
}