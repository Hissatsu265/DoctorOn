package com.example.doctoron.Activities

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.doctoron.Adapters.ChatwithDoc_Adapter
import com.example.doctoron.Adapters.UserchatDoctor
import com.example.doctoron.Objects.Doctor_userchat
import com.example.doctoron.Objects.Drug
import com.example.doctoron.R
import com.google.firebase.firestore.FirebaseFirestore

class Chatwithdoctor : AppCompatActivity() {
    private var idConversation:String=""
    lateinit var doctorsuserchat_data: ArrayList<String>
    lateinit var doctorsuserchat_time: ArrayList<String>
    lateinit var doctorsuserchat_send: ArrayList<String>
    private lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_chatwithdoctor)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        idConversation=intent.getStringExtra("Id_Con").toString()
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
    //--------------------------------Render các tin nhắn ra------------------------------------------------------
        recyclerView=findViewById(R.id.messagesRecyclerView)
        recyclerView.layoutManager=
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.setHasFixedSize(true)

        doctorsuserchat_data= ArrayList()
        doctorsuserchat_time= ArrayList()
        doctorsuserchat_send= ArrayList()

        val db=FirebaseFirestore.getInstance()
        db.collection("Messages").document(idConversation)
            .get().addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val stringList1 = documentSnapshot.get("messs") as List<String>
                    val stringList2 = documentSnapshot.get("times") as List<String>
                    val stringList3 = documentSnapshot.get("sends") as List<String>
                    // Truyền dữ liệu vào ArrayList<String>
                    doctorsuserchat_data = ArrayList<String>(stringList1)
                    doctorsuserchat_time = ArrayList<String>(stringList2)
                    doctorsuserchat_send = ArrayList<String>(stringList3)

                    var adapter_topdoctor = ChatwithDoc_Adapter(doctorsuserchat_data,
                        doctorsuserchat_time,doctorsuserchat_send)
                    recyclerView.adapter=adapter_topdoctor
                }
            }
//---------------------------------------------------------------------------------------------------------------
    }

    fun InitChat(){

    }
}