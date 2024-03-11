package com.example.doctoron.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.doctoron.R
import com.google.firebase.firestore.FirebaseFirestore

class Chinh_sua_thong_tin_ca_nhan : AppCompatActivity() {


    var userid : String = ""
    val db = FirebaseFirestore.getInstance()
    lateinit var edt_bv:EditText
    lateinit var edt_About:EditText
    lateinit var edt_CN:EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userid=intent.getStringExtra("user_ID").toString()
        setContentView(R.layout.activity_chinh_sua_thong_tin_ca_nhan)

        val btn_back=findViewById<ImageView>(R.id.back_btn)
        val btn_update=findViewById<Button>(R.id.btn_update)

        var edt_name=findViewById<EditText>(R.id.name)
        var edt_age=findViewById<EditText>(R.id.age)
        var edt_address=findViewById<EditText>(R.id.address)
        var edt_DoB=findViewById<EditText>(R.id.DoB)
        var edt_sex=findViewById<EditText>(R.id.sex)
        edt_bv=findViewById<EditText>(R.id.BV)
        edt_CN=findViewById<EditText>(R.id.CN)
        edt_About=findViewById<EditText>(R.id.About)
        var kt:Boolean=false
        // phan khai bao cho bac si
        var cv_bv=findViewById<androidx.cardview.widget.CardView>(R.id.cv_doctorBV)
        var cv_about=findViewById<androidx.cardview.widget.CardView>(R.id.cv_doctorabout)
        var cv_cn=findViewById<androidx.cardview.widget.CardView>(R.id.cv_doctorCN)
        cv_bv.visibility=View.GONE
        cv_about.visibility=View.GONE
        cv_cn.visibility=View.GONE
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
                        if (document.get("isDoctor").toString()!=""){
                            cv_bv.visibility=View.VISIBLE
                            cv_about.visibility=View.VISIBLE
                            cv_cn.visibility=View.VISIBLE
                            kt=true
                            Getdltudoctor()
                        }
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
            if(kt){
                UpdateDL()
            }
        }
        //--------------------------------------------------------------------
    }
    fun Getdltudoctor(){
        db.collection("Doctors")
            .document(userid)
            .get()
            .addOnSuccessListener { document ->
                run {
                    if (document != null) {
                        edt_bv.setText(document.get("BV").toString())
                        edt_About.setText(document.get("about").toString())
                        edt_CN.setText(document.get("CN").toString())
                    }
                }
            }
    }
    fun UpdateDL(){
        val user_data = hashMapOf(
            "about" to edt_About.text.toString(),
            "BV" to edt_bv.text.toString(),
            "CN" to edt_CN.text.toString(),
        )
        db.collection("Doctors").document(userid)
            .update(user_data as Map<String, Any>)
            .addOnSuccessListener {

            }
            .addOnFailureListener { e ->

            }
    }
}