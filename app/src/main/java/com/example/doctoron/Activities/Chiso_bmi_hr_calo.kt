package com.example.doctoron.Activities

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.doctoron.R
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.google.firebase.firestore.FirebaseFirestore
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import java.text.DecimalFormat
import java.util.Calendar



class Chiso_bmi_hr_calo : AppCompatActivity() {

    //--------------------------------------------
    lateinit var chart_heartrace: LineChart
    lateinit var chart_bmi: LineChart
    lateinit var chart_calo: LineChart

    lateinit var chart_heartraceTB: LineChart
    lateinit var chart_bmiTB: LineChart
    lateinit var chart_caloTB: LineChart

    //-------------------------------------------------------------------------
    var month:String=getCurrentDate()
    // sau này nhớ đặt lại --------------------------------------
    lateinit var chisotb_bmi1:TextView
    lateinit var chisotb_calo1:TextView
    lateinit var chisotb_hr1:TextView

    var userid : String = ""
    val db = FirebaseFirestore.getInstance()
    //-----------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chiso_bmi_hr_calo)
        userid=intent.getStringExtra("user_ID").toString()
        //--------------------Khai báo------------------------------------------------

        val db = FirebaseFirestore.getInstance()

        //--------------------------------------------
        chart_bmi=findViewById(R.id.chart_bmi99)
        chart_calo=findViewById(R.id.chart_calo99)
        chart_heartrace = findViewById(R.id.chart)
        Initbangchiso(chart_heartrace,"hr")
        Initbangchiso(chart_bmi,"bmi")
        Initbangchiso(chart_calo,"calo")

        chart_bmiTB=findViewById(R.id.chart_bmi99TB)
        chart_caloTB=findViewById(R.id.chart_calo99TB)
        chart_heartraceTB = findViewById(R.id.chartTB)
        Initbangchiso(chart_heartraceTB,"hr",13F)
        Initbangchiso(chart_bmiTB,"bmi",13F)
        Initbangchiso(chart_caloTB,"calo",13F)
        //-------------------------------------------------------------------
        Capnhapchiso()
        Capnhapchisonam()
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
            Toast.makeText(this,"Done",Toast.LENGTH_SHORT).show()
            var t=edt_month.text.toString().toInt()
            if (t in 1..12){
                month=edt_month.text.toString()
                Capnhapchiso()
            }
        }
        btn_update1.setOnClickListener {
            val so1:Double=edt_bmi.text.toString().toDouble()
            val so2=edt_calo.text.toString().toDouble()/100
            val so3=edt_hr.text.toString().toDouble()/10
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

                            Updatechiso(bmi,so1,"bmi_")
                            Updatechiso(calo,so2,"calo_")
                            Updatechiso(heartrace,so3,"heartrace_")
                        }
                    }
                }
        }
    }

    fun Capnhapchiso(){
        db.collection("Chiso")
            .document(userid)
            .get()
            .addOnSuccessListener { document ->
                run {
                    if (document != null) {
                        val bmi = document.get("bmi_$month") as? ArrayList<Double>
                        val calo = document.get("calo_$month") as? ArrayList<Double>
                        val heartrace = document.get("heartrace_$month") as? ArrayList<Double>
                        if (heartrace != null) {
                            setData(heartrace,chart_heartrace,"hr")
                        }
                        if(calo!=null){
                            setData(calo,chart_calo,"calo")
                        }
                        if(bmi!=null){
                            setData(bmi,chart_bmi,"bmi")
                        }
                        chisotb_bmi1.setText(TinhchisoTB(bmi))
                        chisotb_calo1.setText(TinhchisoTB(calo))
                        chisotb_hr1.setText(TinhchisoTB(heartrace))
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
        var size:Int=0
        if (a != null) {
            for (i in a ){
                if(i>1.0){
                    tb+=i
                    size+=1
                }
            }
            tb=tb/size
        }
        val decimalFormat = DecimalFormat("#.###")
        val formattedNumber = decimalFormat.format(tb)
        return formattedNumber.toString()
    }

    fun Updatechiso(a:ArrayList<Double>?,b:Double,type:String){
        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        if (a != null) {
            a.set(day,b)
        }
        val path : String = type+getCurrentDate()
        val db = FirebaseFirestore.getInstance()
        val data = hashMapOf(
            path to a
        )
        db.collection("Chiso").document(userid)
            .update(data as Map<String, Any>)
            .addOnSuccessListener {
                Toast.makeText(this,"Update dữ liệu thành công",Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this,"Uopss có lỗi rồi",Toast.LENGTH_SHORT).show()
            }
    }
    fun Capnhapchisonam(){
        db.collection("Chiso")
            .document(userid)
            .get()
            .addOnSuccessListener { document ->
                run {
                    if (document != null) {
                        val bmi : ArrayList<Double> = ArrayList()
                        val calo : ArrayList<Double> = ArrayList()
                        val heartrace : ArrayList<Double> = ArrayList()

                        for (i in 1 ..12 ){
                            val bmi1 = document.get("bmi_$i") as? ArrayList<Double>
                            val calo1 = document.get("calo_$i") as? ArrayList<Double>
                            val heartrace1 = document.get("heartrace_$i") as? ArrayList<Double>
                            if (bmi1 != null){
                                bmi.add(TinhchisoTB(bmi1).toDouble())
                            }
                            if (calo1 != null){
                                calo.add(TinhchisoTB(calo1).toDouble())
                            }
                            if (heartrace1 != null){
                                heartrace.add(TinhchisoTB(heartrace1).toDouble())
                            }
                        }
                        setData(bmi,chart_bmiTB,"bmi",12)
                        setData(calo,chart_caloTB,"calo",12)
                        setData(heartrace,chart_heartraceTB,"hr",12)
                    }
                }
            }
    }
    private fun setData(a:ArrayList<Double>,chart: LineChart,type:String,number:Int=31)
    {
        chart.xAxis.apply {
            axisMinimum = 1f
            axisMaximum = number.toFloat()
            isGranularityEnabled = true
            setDrawGridLines(true)
            position = XAxis.XAxisPosition.BOTTOM
        }
        val values = ArrayList<Entry>()
        for (i in 0 until number)
        if(a[i].toFloat()>2F)
        {
            values.add(Entry((i+1).toFloat(),a[i].toFloat()))
        }
        val set1: LineDataSet
        if (chart.data != null && chart.data.dataSetCount > 0) {
            set1 = chart.data.getDataSetByIndex(0) as LineDataSet
            set1.values = values
            set1.notifyDataSetChanged()
            chart.data.notifyDataChanged()
            chart.notifyDataSetChanged()
        } else {
            // create a dataset and give it a type
            set1 = LineDataSet(values, "DataSet 1")
            set1.setDrawIcons(false)

            // Chỉnh màu cho line
            set1.color = Color.WHITE
            //set1.setDrawCircleHole(true)
            set1.setDrawValues(false)// hiển thị giá trị lên biểu đồ
            set1.setCircleColor(Color.BLACK)// có dấu chấm tại giá trị
            set1.setDrawCircles(true)
            set1.mode = LineDataSet.Mode.CUBIC_BEZIER
            set1.isHighlightEnabled =true
            set1.setDrawHighlightIndicators(false)

            // chỉnh kích thước đường kẻ và kích thước dấu chấm
            set1.lineWidth = 4f
            set1.circleRadius = 2f

            // kích thước và màu của chữ
            set1.valueTextSize = 19f
            set1.valueTextColor = R.color.black


            // Tạo vùng phủ màu dưới đường kẻ
            set1.setDrawFilled(true)
            if(type=="hr"){
                set1.fillDrawable = getDrawable(R.drawable.bg_line) }
            else if (type=="bmi"){
                set1.fillDrawable = getDrawable(R.drawable.bg_line_bmi) }
            else{
                set1.fillDrawable = getDrawable(R.drawable.bg_line_calo)
            }

            val dataSets = ArrayList<ILineDataSet>()
            dataSets.add(set1) // add the data sets

            // create a data object with the data sets
            val data = LineData(dataSets)

            // set data
            chart.data = data
        }
    }
    fun Initbangchiso(chart: LineChart,typeChart1:String,number:Float=31F){
        //chart.setBackgroundColor()
        chart.description.isEnabled = false //hiện chú thích
        chart.legend.isEnabled = false
        chart.setNoDataText("No data available")
        chart.setTouchEnabled(true)
        //chart.setOnChartValueSelectedListener(this)

        chart.setDrawGridBackground(false)
        chart.isDragEnabled = true
        chart.setScaleEnabled(true)
        chart.setPinchZoom(true)
        var max:Float
        if(typeChart1=="bmi"){
            max=28F
        }else{
            max=18F
        }
        // chỉnh thông số cho trục dọc
        chart.axisRight.isEnabled=false
        chart.axisLeft.apply {
            axisMinimum = 0f //giá trị thấp nhất
            axisMaximum = max// giá trị cao nhất
            setDrawGridLines(true)
        }
        // chỉnh thông số cho trục kẻ ngang
        chart.xAxis.apply {
            axisMinimum = 1f
            axisMaximum = number
            isGranularityEnabled = true
            setDrawGridLines(true)
            position = XAxis.XAxisPosition.BOTTOM
        }
        //---------------------------------------------
        chart.animateX(1800)
    }
    fun Fakechiso(){
        //--------------------tạo chỉ số fake-------------------------------
//            val db1 = FirebaseFirestore.getInstance()
//            try{
//                val aa= listOf(19.29, 19.15, 20.25, 20.91, 19.64, 19.24, 20.81, 19.26, 20.58, 19.78, 19.01, 20.72, 20.94, 19.69, 19.5, 19.43, 20.89, 19.49, 20.52, 19.36, 20.56, 20.01, 20.24, 19.5, 19.06, 19.25, 20.31, 19.85, 19.42, 20.99, 19.23)
//                val cc=listOf(12.29, 12.02, 12.07, 13.9, 10.45, 14.67, 11.47, 14.48, 12.86, 14.86, 14.88, 10.98, 13.37, 13.72, 10.34, 13.52, 12.71, 14.73, 10.59, 11.58, 12.74, 11.46, 13.43, 14.61, 14.49, 12.55, 11.87, 14.79, 12.54, 12.94, 14.01)
//                val bb=listOf(8.79, 9.02, 8.54, 9.21, 8.81, 9.67, 8.83, 8.33, 8.66, 8.62, 9.52, 8.58, 9.39, 9.62, 9.29, 8.46, 8.88, 9.6, 8.52, 8.74, 8.77, 8.74, 8.72, 9.63, 9.28, 8.89, 9.38, 9.48, 9.08, 8.61, 9.13)
//
//                val data = hashMapOf(
//                    "bmi_5" to aa,
//                    "calo_5" to cc,
//                    "heartrace_5" to bb,
//                    "bmi_4" to aa,
//                    "calo_4" to cc,
//                    "heartrace_4" to bb,
//                    "bmi_6" to aa,
//                    "calo_6" to cc,
//                    "heartrace_6" to bb,
//                    "bmi_7" to aa,
//                    "calo_7" to cc,
//                    "heartrace_7" to bb,
//                )
//                db1.collection("Chiso").document(userid)
//                    .update(data as Map<String, Any>)
//                    .addOnSuccessListener {
//                        Toast.makeText(this,"Update dữ liệu thành công",Toast.LENGTH_SHORT).show()
//                    }
//                    .addOnFailureListener { e ->
//                        Toast.makeText(this,"Uopss có lỗi rồi",Toast.LENGTH_SHORT).show()
//                    }
//            }
//            catch (e:Exception){
//                Log.d("TAGloii", "onCreate: "+e.message.toString())
//            }
        //---------------------------------------------------------------------
    }
}