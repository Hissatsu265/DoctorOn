package com.example.doctoron.Activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
//import android.telecom.Call
import android.util.Log
import android.view.View
import android.view.WindowInsetsAnimation
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.Toast
import com.example.doctoron.Adapters.History_predict_Adapter
import com.example.doctoron.Objects.CustomDialog_Predict
import com.example.doctoron.Objects.PredictionResponse
import com.example.doctoron.R
import com.google.android.gms.common.api.Response
import com.google.firebase.firestore.FirebaseFirestore
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.time.Duration
import retrofit2.Call
import retrofit2.Callback
import java.util.Calendar

class Predict_health : AppCompatActivity() {
    var userID=""
    lateinit var cv_predict_diabete:androidx.cardview.widget.CardView
    lateinit var cv_predict_heart:androidx.cardview.widget.CardView
    lateinit var ll_predict_heart:LinearLayout
    lateinit var ll_predict_diabete:LinearLayout
    lateinit var btn_predict_heart: Button
    lateinit var btn_predict_diabetes: Button

    lateinit var edt_heart_age:EditText
    lateinit var edt_heart_tres:EditText
    lateinit var edt_heart_chol:EditText
    lateinit var edt_heart_thalach:EditText
    lateinit var edt_heart_fbs:EditText
    lateinit var edt_heart_oldpeak:EditText

    lateinit var radio_sex_nam:RadioButton
    lateinit var radio_sex_nu:RadioButton
    lateinit var radio_restec_bt:RadioButton
    lateinit var radio_restec_kbt:RadioButton
    lateinit var radio_exang_yes:RadioButton
    lateinit var radio_exang_no:RadioButton
    lateinit var radio_ca_0:RadioButton
    lateinit var radio_ca_1:RadioButton
    lateinit var radio_ca_2:RadioButton
    lateinit var radio_ca_3:RadioButton
    lateinit var radio_cp_1:RadioButton
    lateinit var radio_cp_2:RadioButton
    lateinit var radio_cp_3:RadioButton
    lateinit var radio_cp_4:RadioButton
    lateinit var radio_slope_0:RadioButton
    lateinit var radio_slope_1:RadioButton
    lateinit var radio_slope_2:RadioButton
    lateinit var radio_thal_0:RadioButton
    lateinit var radio_thal_1:RadioButton
    lateinit var radio_thal_2:RadioButton
    lateinit var radio_thal_3:RadioButton

    lateinit var edt_diabete_age:EditText
    lateinit var edt_diabete_bmi:EditText
    lateinit var edt_diabete_phyHL:EditText

    val diabete_sex = mutableListOf<RadioButton>()
    val diabete_chol = mutableListOf<RadioButton>()
    val diabete_genhl = mutableListOf<RadioButton>()
    val diabete_highbp = mutableListOf<RadioButton>()
    val diabete_phyactivity = mutableListOf<RadioButton>()
    val diabete_fruit = mutableListOf<RadioButton>()
    val diabete_vege = mutableListOf<RadioButton>()
    val diabete_diff = mutableListOf<RadioButton>()
    val diabete_heart = mutableListOf<RadioButton>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_predict_health)
        //------------------------------------------------------------
        userID=intent.getStringExtra("user_ID").toString()
        val btn_back=findViewById<ImageButton>(R.id.back_btn)
        btn_back.setOnClickListener {
            finish()
        }
        //-----------------------------------------------------------
        edt_diabete_age=findViewById(R.id.diabete_age)
        edt_diabete_bmi=findViewById(R.id.diabetes_bmi)
        edt_diabete_phyHL=findViewById(R.id.diabetes_physHL)

        for (i in 0 until 5) {
            diabete_genhl.add(findViewById(R.id.diabetes_genhl_1 + i))
        }

        for (i in 0 until 2){
            diabete_chol.add(findViewById(R.id.diabetes_chol_0+i))
            diabete_diff.add(findViewById(R.id.diabete_diff_0+i))
//            diabete_fruit.add(findViewById(R.id.diabetes_fruit_0+i))
            diabete_vege.add(findViewById(R.id.diabete_vege_0+i))
            diabete_phyactivity.add(findViewById(R.id.diabetes_Phyactivity_0+i))
            diabete_highbp.add(findViewById(R.id.diabete_highbp_0+i))
            diabete_heart.add(findViewById(R.id.diabete_heart_0+i))
        }
        diabete_fruit.add(findViewById(R.id.diabetes_fruitt_0))
        diabete_fruit.add(findViewById(R.id.diabetes_fruitt_1))

        diabete_sex.add(findViewById(R.id.diabetes_sex_nu))
        diabete_sex.add(findViewById(R.id.diabetes_sex_nam))
        try{
            setRadioEvents(arrayOf( diabete_heart[0],diabete_heart[1]))
            setRadioEvents(arrayOf( diabete_chol[0],diabete_chol[1]))
            setRadioEvents(arrayOf( diabete_sex[0],diabete_sex[1]))
            setRadioEvents(arrayOf( diabete_diff[0],diabete_diff[1]))
            setRadioEvents(arrayOf(diabete_fruit[0],diabete_fruit[1]))
            setRadioEvents(arrayOf(diabete_phyactivity[0],diabete_phyactivity[1]))
            setRadioEvents(arrayOf( diabete_vege[1],diabete_vege[0]))
            setRadioEvents(arrayOf( diabete_chol[0],diabete_chol[1]))
            setRadioEvents(arrayOf(diabete_highbp[1],diabete_highbp[0]))
            setRadioEvents(arrayOf(diabete_genhl[0],diabete_genhl[1],diabete_genhl[2],diabete_genhl[3],diabete_genhl[4]))
        }catch (e:Exception){
            Log.d("kkl", "onCreate: "+e.message.toString())
        }

        //-------------------------------------------------------------
        edt_heart_age=findViewById(R.id.heart_age)
        edt_heart_chol=findViewById(R.id.heart_chol)
        edt_heart_tres=findViewById(R.id.heart_tres)
        edt_heart_thalach=findViewById(R.id.heart_thalach)
        edt_heart_fbs=findViewById(R.id.heart_fbs)
        edt_heart_oldpeak=findViewById(R.id.heart_oldpeak)
        //-------------------------------------------------------------
        cv_predict_diabete=findViewById(R.id.btn_predict_diabete)
        cv_predict_heart=findViewById(R.id.btn_predict_heart)

        ll_predict_heart=findViewById(R.id.ll_heart)
        ll_predict_heart.visibility=View.GONE
        ll_predict_diabete=findViewById(R.id.ll_diabetes)
        ll_predict_diabete.visibility=View.GONE

        btn_predict_heart=findViewById(R.id.btn_predict_heart1)
        btn_predict_diabetes=findViewById(R.id.btn_predict_diabetes)

        radio_ca_0=findViewById(R.id.heart_ca_0)
        radio_ca_1=findViewById(R.id.heart_ca_1)
        radio_ca_2=findViewById(R.id.heart_ca_2)
        radio_ca_3=findViewById(R.id.heart_ca_3)
        radio_cp_1=findViewById(R.id.heart_cp_1)
        radio_cp_2=findViewById(R.id.heart_cp_2)
        radio_cp_3=findViewById(R.id.heart_cp_3)
        radio_cp_4=findViewById(R.id.heart_cp_4)
        radio_exang_no=findViewById(R.id.heart_exang_no)
        radio_exang_yes=findViewById(R.id.heart_exang_yes)
        radio_restec_bt=findViewById(R.id.heart_restec_bt)
        radio_restec_kbt=findViewById(R.id.heart_restec_kbt)
        radio_sex_nam=findViewById(R.id.heart_sex_nam)
        radio_sex_nu=findViewById(R.id.heart_sex_nu)
        radio_slope_0=findViewById(R.id.heart_slope_0)
        radio_slope_1=findViewById(R.id.heart_slope_1)
        radio_slope_2=findViewById(R.id.heart_slope_2)
        radio_thal_0=findViewById(R.id.heart_thal_0)
        radio_thal_1=findViewById(R.id.heart_thal_1)
        radio_thal_2=findViewById(R.id.heart_thal_2)
        radio_thal_3=findViewById(R.id.heart_thal_3)
        //-------------------------------------------------------------
        val radioCa = arrayOf(radio_ca_0, radio_ca_1, radio_ca_2, radio_ca_3)
        val radioCp = arrayOf(radio_cp_1, radio_cp_2, radio_cp_3, radio_cp_4)
        val radioSex = arrayOf(radio_sex_nam, radio_sex_nu)
        val radioThal = arrayOf(radio_thal_0, radio_thal_1, radio_thal_2, radio_thal_3)
        val radioSlope= arrayOf(radio_slope_0,radio_slope_1,radio_slope_2)
        val radioexang= arrayOf(radio_exang_no,radio_exang_yes)
        val radiorestec= arrayOf(radio_restec_bt,radio_restec_kbt)

        setRadioEvents(radioCa)
        setRadioEvents(radioCp)
        setRadioEvents(radioSex)
        setRadioEvents(radioThal)
        setRadioEvents(radioSlope)
        setRadioEvents(radiorestec)
        setRadioEvents(radioexang)

        //-------------------------------------------------------------
        btn_predict_diabetes.setOnClickListener{
            Predic_tieuduong()
        }
        btn_predict_heart.setOnClickListener{
            Predic_tim()
        }
    }
    fun Predic_tieuduong(){
        cv_predict_diabete.visibility= View.GONE
        cv_predict_heart.visibility=View.VISIBLE
        ll_predict_heart.visibility=View.GONE
        ll_predict_diabete.visibility=View.VISIBLE
        val btn=findViewById<Button>(R.id.btn_makepredictdiabete)
        btn.setOnClickListener {
            try{
                val inputData1 = floatArrayOf(
                    edt_diabete_age.text.toString().toFloat(),
                    Value_Radio(diabete_sex[0],diabete_sex[1]).toFloat(),
                    Value_Radio(diabete_chol[0],diabete_chol[1]).toFloat(),
                    edt_diabete_bmi.text.toString().toFloat(),
                    Value_Radio(diabete_heart[0],diabete_heart[1]).toFloat(),
                    Value_Radio(diabete_phyactivity[0],diabete_phyactivity[1]).toFloat(),
                    Value_Radio(diabete_fruit[0],diabete_fruit[1]).toFloat(),
                    Value_Radio(diabete_vege[0],diabete_vege[1]).toFloat(),
                    Value_Radio(diabete_genhl[0],diabete_genhl[1],diabete_genhl[2],diabete_genhl[3],diabete_genhl[4]).toFloat(),
                    edt_diabete_phyHL.text.toString().toFloat(),
                    Value_Radio(diabete_diff[0],diabete_diff[1]).toFloat(),
                    Value_Radio(diabete_highbp[0],diabete_highbp[1]).toFloat(),
                )
                Log.d("kfffj", "Predic_tim: " + inputData1.toList().toString())
                try{
                    makePrediction(inputData1,this,"tiểu đường")
                }catch (e:Exception){
                    Log.d("fdfdf", "Predic_tieuduong: "+e.message.toString())
                }

            }catch (e:Exception){
                Log.d("fdfdf", "Predic_tieuduong: "+e.message.toString())
            }


//            makePrediction(inputData1,this,"tiểu đường")
        }
    }
    fun Predic_tim(){
        cv_predict_diabete.visibility= View.VISIBLE
        cv_predict_heart.visibility=View.GONE
        ll_predict_heart.visibility=View.VISIBLE
        ll_predict_diabete.visibility=View.GONE
        val btn=findViewById<Button>(R.id.btn_makepredictheart)
        btn.setOnClickListener {
            val inputData1 = floatArrayOf(
                edt_heart_age.text.toString().toFloat(),
                Value_Radio(radio_sex_nu,radio_sex_nam).toFloat(),
                Value_Radio(radio_cp_1,radio_cp_2,radio_cp_3,radio_cp_4).toFloat(),
                edt_heart_tres.text.toString().toFloat(),
                edt_heart_chol.text.toString().toFloat(),
                edt_heart_fbs.text.toString().toFloat(),
                Value_Radio(radio_restec_kbt,radio_restec_bt).toFloat(),
                edt_heart_thalach.text.toString().toFloat(),
                Value_Radio(radio_exang_no,radio_exang_yes).toFloat(),
                edt_heart_oldpeak.text.toString().toFloat(),
                Value_Radio(radio_slope_0,radio_slope_1,radio_slope_2).toFloat(),
                Value_Radio(radio_ca_0,radio_ca_1,radio_ca_2,radio_ca_3).toFloat(),
                Value_Radio(radio_thal_0,radio_thal_1,radio_thal_2,radio_thal_3).toFloat()
            )

//            Log.d("kfffj", "Predic_tim: " + inputData1.toList().toString())
            try {
                makePrediction(inputData1,this,"bệnh tim")
            }catch (e:Exception){
                Log.d("loiii", "Predic_tim: "+e.message.toString())
            }
        }

    }
    //---------------------------------------------------------------------------------------------------------------------
    fun HandleEvent_Radio(vararg radioButtons: RadioButton?) {
        radioButtons.forEach { radioButton ->
            radioButton?.isChecked = false
        }
    }
    fun setRadioEvents(radios: Array<RadioButton>) {
        radios.forEachIndexed { index, radioButton ->
            radioButton.setOnClickListener {
                val otherRadios = radios.filterIndexed { idx, _ -> idx != index }
                HandleEvent_Radio(*otherRadios.toTypedArray())
            }
        }
    }

    fun Value_Radio(vararg radioButtons:RadioButton?):Int{
        radioButtons.forEachIndexed() { idx,radioButton ->
            if (radioButton != null) {
                if(radioButton.isChecked==true)
                    return idx
            }
        }
        return 0
    }
    // Define Retrofit interface------------------------------------------------------------------------------------------------------
    interface PredictionApi {
        @POST("/predict")
        fun predictHeartDisease(@Body inputData: Map<String, FloatArray>): Call<PredictionResponse>
    }
    fun makePrediction(inputData: FloatArray,context_1:Context,loaibenh:String) {
        if ((inputData.size != 13 && loaibenh.equals("bệnh tim")) || inputData.size<12) {
            Toast.makeText(this,"Bạn chưa nhập đủ dữ liệu",Toast.LENGTH_SHORT).show()
            return
        }

        // Create Retrofit instance
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.1.67:5000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Create Retrofit service
        val predictionApi = retrofit.create(PredictionApi::class.java)

        // Prepare data to be sent
        val data = hashMapOf<String, FloatArray>()
        var t : Int=1
        data["features"] = inputData
        if(loaibenh=="bệnh tim"){
            data["type"] = floatArrayOf(t.toFloat())
        }else{
            t=t+1
            data["type"] = floatArrayOf(t.toFloat())
        }

        // Make the prediction request
        val call = predictionApi.predictHeartDisease(data)

        // Asynchronously handle the response
        call.enqueue(object : Callback<PredictionResponse> {


            override fun onFailure(call: Call<PredictionResponse>, t: Throwable) {
//                Toast.makeText(this, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("loisa", t.message.toString())
            }

            override fun onResponse(
                call: Call<PredictionResponse>,
                response: retrofit2.Response<PredictionResponse>
            ) {
                if (response.isSuccessful) {

                    try{
                        val predictionResponse = response.body()
                        val result = predictionResponse?.prediction ?: -1
                        val kq:Double = result*1.0/100.0
                        val check= kq > 50
                        CustomDialog_Predict(context_1,"khả năng mắc $loaibenh của bạn khoảng $kq%",check).show()

                        UpdatedatetoPredict(kq,loaibenh)


                    }
                    catch (e:Exception){
                        Log.d("loii", e.message.toString())
                    }

                } else {
                    Log.d("lloisa", response.message())
//                    Toast.makeText(context, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
    fun UpdatedatetoPredict(rate1:Double,type2:String){
        var type1="1"
        if(type2=="bệnh tim"){
            type1="0"
        }
        val db=FirebaseFirestore.getInstance()
        db.collection("Prediction").document(userID).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    var time=document.get("time") as ArrayList<String>
                    var type=document.get("type") as ArrayList<String>
                    var rate=document.get("rate") as ArrayList<String>

                    type.add(type1)
                    rate.add(rate1.toString())
                    time.add(getCurrentDateTimeUsingCalendar())
                    val data= hashMapOf<String, Any>(
                        "time" to time,
                        "rate" to rate,
                        "type" to type
                    )
                    //-------------------------đẩy lên firebase lại-----------------------
                    FirebaseFirestore.getInstance().collection("Prediction")
                        .document(userID).update(data)

                }
            }
    }
    fun getCurrentDateTimeUsingCalendar(): String{
        val calendar: Calendar = Calendar.getInstance()
        // Lấy thông tin thời gian hiện tại
        val year: Int = calendar.get(Calendar.YEAR)
        val month: Int = calendar.get(Calendar.MONTH) + 1 // Tháng bắt đầu từ 0
        val dayOfMonth: Int = calendar.get(Calendar.DAY_OF_MONTH)
        val hourOfDay: Int = calendar.get(Calendar.HOUR_OF_DAY) // 24-giờ format
        val minute: Int = calendar.get(Calendar.MINUTE)

        // Định dạng ngày tháng năm và giờ phút
        val time:String = "$hourOfDay:$minute $dayOfMonth/$month/$year"

        return time
    }

}