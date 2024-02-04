package com.example.doctoron.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.example.doctoron.R
import com.google.firebase.auth.FirebaseAuth

class Confirm_email : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_email)
        val pass = intent.getStringExtra("pass")
        val gmail = intent.getStringExtra("gmail")
        Log.d("hus", pass.toString())
        Log.d("hus", gmail.toString())

        val button = findViewById<AppCompatButton>(R.id.btn_next)

        button.setOnClickListener {
            firebaseAuth = FirebaseAuth.getInstance()

            firebaseAuth.signInWithEmailAndPassword(gmail.toString(), pass.toString())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val firebaseUser = firebaseAuth.currentUser
                            try{
                                if (firebaseUser != null) {
                                    if(firebaseUser.isEmailVerified) {
                                        Toast.makeText(applicationContext, "Đăng nhập thành công", Toast.LENGTH_SHORT).show()
                                        val intent = Intent(applicationContext, Home::class.java)
                                        startActivity(intent)
                                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left)
                                    }else{
                                        Toast.makeText(applicationContext, "Bạn chưa xác thực tài khoản hãy check lại mail", Toast.LENGTH_SHORT).show()
                                        firebaseUser.sendEmailVerification()
                                    }
                                }
                            } catch (e: Exception)
                            {
                                Toast.makeText(applicationContext, e.message.toString(), Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(applicationContext, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show()
                        }
                    }

        }
    }
}
