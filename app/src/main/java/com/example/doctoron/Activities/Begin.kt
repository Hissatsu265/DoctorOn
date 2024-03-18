package com.example.doctoron.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton
import com.example.doctoron.R

class Begin : AppCompatActivity() {
    //shdfhsjs
    //jdsdhfs
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_begin)
        val btn = findViewById<AppCompatButton>(R.id.btn_next)
        btn.setOnClickListener {
            val intent= Intent(applicationContext,Startscreen::class.java)
            intent.putExtra("Collection","users")
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left)
            finish()
        }
        val btn_1 = findViewById<AppCompatButton>(R.id.btn_next1)
        btn_1.setOnClickListener {
            val intent= Intent(applicationContext,Startscreen::class.java)
            intent.putExtra("Collection","Doctors")
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left)
            finish()
        }
    }
}