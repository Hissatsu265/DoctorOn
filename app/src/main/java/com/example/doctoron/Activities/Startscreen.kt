package com.example.doctoron.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.doctoron.R
import androidx.appcompat.widget.AppCompatButton

class Startscreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_startscreen)
        val collection=intent.getStringExtra("Collection")
        val btnlogin =findViewById<AppCompatButton>(R.id.btnLogin)
        val btnsign =findViewById<AppCompatButton>(R.id.btnSignUp)

        btnlogin.setOnClickListener{
            val intent= Intent (applicationContext,Lognin::class.java)
            intent.putExtra("Collection",collection)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left)
        }
        btnsign.setOnClickListener {
            val intent= Intent (applicationContext,Signup::class.java)
            intent.putExtra("Collection",collection)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left)
        }
    }
}