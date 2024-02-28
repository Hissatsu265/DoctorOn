package com.example.doctoron.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.doctoron.R
import com.google.firebase.firestore.FirebaseFirestore

class Chinh_sua_thong_tin_ca_nhan : AppCompatActivity() {


    val userid : String = intent.getStringExtra("user_ID").toString()
    val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chinh_sua_thong_tin_ca_nhan)

        val btn_back=findViewById<ImageView>(R.id.back_btn)
        val btn_update=findViewById<Button>(R.id.btn_update)

        var edt_name=findViewById<EditText>(R.id.name)
        var edt_age=findViewById<EditText>(R.id.age)
        var edt_address=findViewById<EditText>(R.id.address)
        var edt_DoB=findViewById<EditText>(R.id.DoB)
        var edt_sex=findViewById<EditText>(R.id.sex)
        //---------------------------------------------------------------------
        db.collection("users")
            .document(userid)
            .get()
            .addOnSuccessListener { document ->
                run {
                    if (document != null) {
                        edt_name.setText(document.get("name").toString())
                        edt_DoB.setText(document.get("DoB").toString())
                        edt_address.setText(document.get("address").toString())
                        edt_age.setText(document.get("age").toString())
                        if (document.get("sex").toString()=="0"){
                            edt_sex.setText("Nam")
                        }
                        else{
                            edt_sex.setText("Nu")
                        }
                    }
                }
            }
        //---------------------------------------------------------------------
        btn_back.setOnClickListener {
            finish()
        }
        btn_update.setOnClickListener {
            var i:Int=1
            if (edt_sex.text.toString()=="Nam"||edt_sex.text.toString()=="nam")
                {i=0}
            val user_data = hashMapOf(
                "name" to edt_name.text.toString(),
                "age" to edt_age.text.toString().toInt(),
                "DoB" to edt_DoB.text.toString(),
                "address" to edt_address.text.toString(),
                "sex" to i
            )
            db.collection("users").document(userid)
                .update(user_data as Map<String, Any>)
                .addOnSuccessListener {
                    Toast.makeText(this,"Update dữ liệu thành công", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this,"Uopss có lỗi rồi", Toast.LENGTH_SHORT).show()
                }
        }
        //--------------------------------------------------------------------
    }
}