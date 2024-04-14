package com.example.doctoron.Activities

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.doctoron.Objects.Doctor_userchat
import com.example.doctoron.Objects.Drug
import com.example.doctoron.R

class Chatwithdoctor : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_chatwithdoctor)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        InitChat()
        //------------------------------------------------------------------------------------
        val btn_back=findViewById<ImageView>(R.id.chatBackBtn)
        btn_back.setOnClickListener {
            finish()
        }
        //-----------------------nhân dữ liệu từ chat from home-------------------------------------------
        val Iv_userchat=findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.chatImageViewUser)
        val Tv_nameuser=findViewById<TextView>(R.id.chatUserName)
        val bundle: Bundle? = intent.getExtras()
        if (bundle != null) {
            val data_receive: Doctor_userchat? = bundle.getSerializable("Userchat") as? Doctor_userchat
            if(data_receive!=null){
                Tv_nameuser.setText(data_receive.getName())
                data_receive.getUrlavatar { url->
                    Glide.with(this)
                        .load(url)
                        .centerCrop()
                        .thumbnail(0.5f)
                        .into(Iv_userchat)
                }

            }
        }
//---------------------------------------------------------------------------------------------------------------
    }

    fun InitChat(){

    }
}