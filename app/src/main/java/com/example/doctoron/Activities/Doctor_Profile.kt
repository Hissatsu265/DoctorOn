package com.example.doctoron.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.doctoron.R

class Doctor_Profile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_profile)
        //----------------quay ve -------------------------------
        val btn_back=findViewById<ImageView>(R.id.back_btn)
        btn_back.setOnClickListener {
            finish()
        }
        //-----------------------------------------------------

    }
}