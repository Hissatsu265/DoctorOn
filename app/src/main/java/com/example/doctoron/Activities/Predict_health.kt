package com.example.doctoron.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
//import android.telecom.Call
import android.util.Log
import android.view.View
import android.view.WindowInsetsAnimation
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.Toast
import com.example.doctoron.Objects.PredictionResponse
import com.example.doctoron.R
import com.google.android.gms.common.api.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.time.Duration
import retrofit2.Call
import retrofit2.Callback

class Predict_health : AppCompatActivity() {
    lateinit var cv_predict_diabete:androidx.cardview.widget.CardView
    lateinit var cv_predict_heart:androidx.cardview.widget.CardView
    lateinit var ll_predict_heart:LinearLayout
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_predict_health)
        //------------------------------------------------------------
        val btn_back=findViewById<ImageView>(R.id.back_btn)
        btn_back.setOnClickListener {
            finish()
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
        btn_predict_heart=findViewById(R.id.btn_predict_heart1)
        btn_predict_diabetes=findViewById(R.id.btn_predict_diabetes)
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
    }
    fun Predic_tim(){
        cv_predict_diabete.visibility= View.VISIBLE
        cv_predict_heart.visibility=View.GONE
        ll_predict_heart.visibility=View.VISIBLE
        val inputData1 = floatArrayOf(57.0f, 1.0f, 3.0f, 145.0f, 233.0f, 1.0f, 0.0f, 150.0f, 0.0f, 2.3f, 0.0f, 0.0f, 1.0f)
        makePrediction(inputData1)

    }
    // Define Retrofit interface
    interface PredictionApi {
        @POST("/predict")
        fun predictHeartDisease(@Body inputData: Map<String, FloatArray>): Call<PredictionResponse>
    }
    fun makePrediction(inputData: FloatArray) {
        if (inputData.size != 13) {
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
        data["features"] = inputData

        // Make the prediction request
        val call = predictionApi.predictHeartDisease(data)

        // Asynchronously handle the response
        call.enqueue(object : Callback<PredictionResponse> {


            override fun onFailure(call: Call<PredictionResponse>, t: Throwable) {
//                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("loisa", t.message.toString())
            }

            override fun onResponse(
                call: Call<PredictionResponse>,
                response: retrofit2.Response<PredictionResponse>
            ) {
                if (response.isSuccessful) {
                    val predictionResponse = response.body()
                    val result = predictionResponse?.prediction ?: -1
                    val kq:Double = result*1.0/100.0
//                     ""
                    Log.d("hihii", "kha nang mac benh tim của bạn khoảng $kq%")
                } else {
                    Log.d("lloisa", response.message())
//                    Toast.makeText(context, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

}