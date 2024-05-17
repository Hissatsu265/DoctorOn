package com.example.doctoron.Activities

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.doctoron.Objects.CustomDialog_Predict
import com.example.doctoron.Objects.MQTT
import com.example.doctoron.R
import com.github.mikephil.charting.charts.LineChart

import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.math.log

class iot : AppCompatActivity() {
    lateinit var chart: LineChart
    val values = ArrayList<Entry>()
    var userId:String=""
    var kt_check=0
    var index=0
    lateinit var heart_race:ArrayList<Int>
    //-------------------------------------------------------------------------------------------
    lateinit var tv_nhiptim:TextView
    //-------------------------------------------------------------------------------------------
    override fun onPause() {
        super.onPause()
//        if(kt_check<0){
//            Updatedata("hr",heart_race)
//        }
    }

    override fun onDestroy() {
        super.onDestroy()
//        if(kt_check<0){
//            Updatedata("hr",heart_race)
//        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_iot)
        heart_race= ArrayList()
//--------------------------------------------------------------------------------------
//        // Đăng ký để nhận dữ liệu từ topic "test" với QoS 1
//        mqttHelper.subscribeToTopic("test", 1)
//--------------------------------------------------------------------------------------
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        userId = intent.getStringExtra("userId").toString()
        Log.d("TAGiddd", "onCreate: "+userId)
        val btn_back=findViewById<ImageButton>(R.id.back_btn)
        btn_back.setOnClickListener {
//            values.clear()
            finish()
        }
//        --------------------------------------------------------------
        var edt_privateid=findViewById<EditText>(R.id.privateid)
        var btn_watch=findViewById<Button>(R.id.xemkq_nk)
        var edt_idnk=findViewById<EditText>(R.id.idanother)
        tv_nhiptim=findViewById(R.id.nhiptim)
        var btn_update=findViewById<Button>(R.id.updateid)
//=============================================================================
        chart = findViewById(R.id.chart)
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
        // chỉnh thông số cho trục dọc
        chart.axisRight.isEnabled=false
        chart.axisLeft.apply {
            axisMinimum = 60f //giá trị thấp nhất
            axisMaximum = 100f// giá trị cao nhất
            setDrawGridLines(true)
        }
        // chỉnh thông số cho trục kẻ ngang
        chart.xAxis.apply {
            axisMinimum = 0f
            axisMaximum = 30f
            isGranularityEnabled = true
            setDrawGridLines(true)
            position = XAxis.XAxisPosition.BOTTOM
        }
        chart.animateX(1500)
//---------------------------Lấy data lúc đầu------------------------------------------------------------
        val db=FirebaseFirestore.getInstance()
        db.collection("Iot").document(userId).get()
            .addOnSuccessListener { document ->
            if (document != null) {
                try{
                    val data = document.data
                    if (data != null) {
                        if(data.get("idprivate").toString()!=""){
                            edt_privateid.setText(data.get("idprivate").toString())
                        }
                        tv_nhiptim.setText(data.get("data").toString())
                        heart_race=data.get("hr") as ArrayList<Int>
                        setData(heart_race)
                        if(data.get("alert")!="0") {
                            CustomDialog_Predict(this, data.get("alert").toString(), true).show()
                        }
                        kt_check=-1
                    }
                }
                catch (e:Exception){
                    Log.d("TAGloiii", "onCreate: "+e.message.toString())
                }

            } else {
                println("Không tìm thấy tài liệu.")
            }
        }
            .addOnFailureListener { exception ->
                println("Lỗi khi lấy dữ liệu: $exception")
            }
    //------------------------lắng nghe cập nhập---------------------------------------
        try {
//            val db3=FirebaseFirestore.getInstance()
            db.collection("Iot").document(userId).addSnapshotListener { snapshot, e ->
                if (e != null || kt_check>0) {
                    Log.d("ttttt","Lỗi khi lắng nghe sự kiện: $e")
                    return@addSnapshotListener
                }
                index++
                if (snapshot != null && snapshot.exists() && index>1) {
                    val data = snapshot.data
                    val fieldData= data?.get("data").toString().toInt()
                    tv_nhiptim.setText(fieldData.toString())
                    XuliLine(fieldData)
                }

            }
        }
        catch (e:Exception){
            Log.d("TAGlopp", "XuliLine: "+e.message.toString())
        }

        //-------------------------------xem id người khác-----------------------------
        btn_watch.setOnClickListener {
            val idnk=edt_idnk.text.toString()
            if(idnk!=""){
                kt_check=1
                Watchotherdata(idnk)
            }
        }
        //-------------------------------Cập nhập private id---------------------------
        btn_update.setOnClickListener {
            val privateid:String=edt_privateid.text.toString()
            if(privateid!=""){
                Updatedata("idprivate",privateid)
            }
        }
    }
    private fun XuliLine(a:Int){
        heart_race.add(a.toInt())
        if(heart_race.size>30){
            heart_race.removeAt(0)
        }
        Log.d("TAGloppp", "XuliLine: "+a.toString())
        Log.d("TAGloppp", "XuliLine: "+heart_race.toString())
        Updatedata("hr",heart_race)
        setData(heart_race)

    }
    private fun Watchotherdata(id:String){
       val db2=FirebaseFirestore.getInstance()
       db2.collection("Iot").get()
           .addOnSuccessListener { documents ->
               for (document in documents) {
                   val data = document.data
                   if(data.get("idprivate").toString()==id){
                       tv_nhiptim.setText(data.get("data").toString())
                       heart_race.clear()
                       heart_race=data.get("hr") as ArrayList<Int>
                       setData(heart_race)
                       Log.d("TAGhiii", "Watchotherdata: "+heart_race[0].toString())
                       kt_check=3
                       break
                   }
               }
               if(kt_check!=3){
                   kt_check=-1
                   Toast.makeText(this,"Không tìm thấy ai có id này",Toast.LENGTH_SHORT).show()
               }
           }
           .addOnFailureListener { exception ->
                kt_check=-1
           }
    }
    private fun Updatedata(key:String,value:Any){
        kt_check=1
        val db1=FirebaseFirestore.getInstance()
        val update_data = hashMapOf<String, Any>(
            key to value,
        )
        db1.collection("Iot").document(userId).update(update_data)
            .addOnSuccessListener {
                if(key!="hr"){
                    Toast.makeText(this,"Update Success",Toast.LENGTH_SHORT).show()
                }
                kt_check=-1
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this,"Update Fail",Toast.LENGTH_SHORT).show()
            }
    }
    private fun setData(a:ArrayList<Int>)
    {
        values.clear()
        chart.xAxis.apply {
            axisMinimum = 0f
            axisMaximum = a.size.toString().toFloat()
            isGranularityEnabled = true
            setDrawGridLines(true)
            position = XAxis.XAxisPosition.BOTTOM
        }
        Log.d("tagky", "setData: "+a.toString())
        for (i in 0 until a.size){
            values.add(Entry(i.toFloat(), a[i].toFloat()))
        }
//======================================================================
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
            set1.setCircleColor(Color.GRAY)// có dấu chấm tại giá trị
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
            set1.fillDrawable = getDrawable(R.drawable.bg_line)

            val dataSets = ArrayList<ILineDataSet>()
            dataSets.add(set1) // add the data sets

            // create a data object with the data sets
            val data = LineData(dataSets)
            // set data
            chart.data = data
        }
    }
}