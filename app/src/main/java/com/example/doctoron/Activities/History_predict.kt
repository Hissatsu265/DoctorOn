package com.example.doctoron.Activities

import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.doctoron.Adapters.DoctorList
import com.example.doctoron.Adapters.History_predict_Adapter
import com.example.doctoron.R
import com.google.firebase.firestore.FirebaseFirestore

class History_predict : AppCompatActivity() {
    var userID:String=""
    lateinit var time:ArrayList<String>
    lateinit var type:ArrayList<String>
    lateinit var rate:ArrayList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_history_predict)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val btn_back=findViewById<ImageButton>(R.id.back_btn)
        btn_back.setOnClickListener {
            finish()
        }

        userID=intent.getStringExtra("UserID").toString()

        time= ArrayList()
        rate= ArrayList()
        type= ArrayList()

        val recyclerView=findViewById<RecyclerView>(R.id.rv_hp)
        recyclerView.layoutManager=
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.setHasFixedSize(true)
        //=============================================================
        val db=FirebaseFirestore.getInstance()
        db.collection("Prediction").document(userID).get()
            .addOnSuccessListener { document ->
            if (document != null) {
                time=document.get("time") as ArrayList<String>
                type=document.get("type") as ArrayList<String>
                rate=document.get("rate") as ArrayList<String>
                var adapter_topdoctor = History_predict_Adapter(rate,time,type)
                recyclerView.adapter=adapter_topdoctor
            }
            }
    }
}