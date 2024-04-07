package com.example.doctoron.Activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.example.doctoron.Objects.CustomDialog_Predict
import com.example.doctoron.Objects.PredictionResponse
import com.example.doctoron.R
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.io.ByteArrayOutputStream
import java.io.File

private lateinit var imageView_anh:ImageView
class Xray_predict : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_xray_predict)
        val btn1=findViewById<Button>(R.id.btn)
        btn1.setOnClickListener {
//            val image=R.drawable.img_xray
            sendImage(this)
        }
//----------------------------------------------------------
        imageView_anh=findViewById(R.id.upload_anh)
        imageView_anh.setOnClickListener {
            chooseImage()
        }
    }
//    -----------------------------------------------------------------------
    private val PICK_IMAGE_REQUEST = 1

    fun chooseImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri: Uri? = data.data
            imageView_anh.setImageURI(selectedImageUri)
        }
    }
//    -----------------------------------------------------------------------
    interface PredictionApi {
        @Multipart
        @POST("/predict_xray")
        fun predictXray(@Part image: MultipartBody.Part): Call<PredictionResponse>
    }
    private fun sendImage(context_1:Context) {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.1.67:5000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val predictionApi = retrofit.create(PredictionApi::class.java)
//--------------------------------------------------------------------------------------

//        val imageBitmap = getBitmapFromDrawable(drawableId)
        imageView_anh.isDrawingCacheEnabled = true
        imageView_anh.buildDrawingCache(true)
        val imageBitmap = Bitmap.createBitmap(imageView_anh.drawingCache)
        imageView_anh.isDrawingCacheEnabled = false
//--------------------------------------------------------------------------------------

        if (imageBitmap != null) {
            val imageByteArray = convertBitmapToByteArray(imageBitmap)

            if (imageByteArray != null) {
                val requestBody = RequestBody.create(MediaType.parse("image/*"), imageByteArray)
                val imagePart = MultipartBody.Part.createFormData("image", "image.jpg", requestBody)

                val call = predictionApi.predictXray(imagePart)

                call.enqueue(object : Callback<PredictionResponse> {
                    override fun onResponse(call: Call<PredictionResponse>, response: retrofit2.Response<PredictionResponse>) {
                        if (response.isSuccessful) {
                            try{
                                val predictionResponse = response.body()
                                val result = predictionResponse?.prediction ?: -1
                                val kq:Double = result/100.0
                                if(kq>50){
                                    CustomDialog_Predict(context_1,"khả năng mắc bệnh viêm phổi của bạn khá cao đó $kq% hãy cẩn thận",true).show()
                                }else{
                                    CustomDialog_Predict(context_1,"khả năng mắc bệnh viêm phổi của bạn thấp $kq% hãy giữ gìn sức khỏe nhé").show()
                                }

                            }
                            catch (e:Exception){
                                Log.d("loii", e.message.toString())
                            }

                        } else {
                            Log.d("lloisa1", response.message())
                        }
                    }

                    override fun onFailure(call: Call<PredictionResponse>, t: Throwable) {
                        Log.e("loisa", t.message.toString())
                    }
                })
            }
        }
    }

//    private fun getBitmapFromDrawable(drawableId: Int): Bitmap? {
//        return try {
//            BitmapFactory.decodeResource(resources, drawableId)
//        } catch (e: Exception) {
//            null
//        }
//    }

    private fun convertBitmapToByteArray(bitmap: Bitmap): ByteArray? {
        val byteArrayOutputStream = ByteArrayOutputStream()
        return try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
            byteArrayOutputStream.toByteArray()
        } catch (e: Exception) {
            null
        } finally {
            byteArrayOutputStream.close()
        }
    }
}