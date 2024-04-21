package com.example.doctoron.Activities

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.doctoron.Adapters.ChatwithDoc_Adapter
import com.example.doctoron.Adapters.UserchatDoctor
import com.example.doctoron.Objects.Chat
import com.example.doctoron.Objects.Doctor_userchat
import com.example.doctoron.Objects.Drug
import com.example.doctoron.R
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalTime

class Chatwithdoctor : AppCompatActivity() {
    private var idConversation:String=""
    private var Id_User:String=""
    lateinit var doctorsuserchat_data: ArrayList<String>
    lateinit var doctorsuserchat_time: ArrayList<String>
    lateinit var doctorsuserchat_send: ArrayList<String>
    private lateinit var recyclerView: RecyclerView
    lateinit var adapter_topdoctor:ChatwithDoc_Adapter
    private var isActive:Boolean=false
    var isSender:String=""
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_chatwithdoctor)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        Id_User=intent.getStringExtra("Id_user").toString()
        idConversation=intent.getStringExtra("Id_Con").toString()
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
//--------------------------------------------------------------------------------------------
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
//------------------------------------------------------------------------------------------------------
                    db.collection("users").document(Id_User)
                        .get().addOnSuccessListener { documentSnapshot1 ->
                            if (documentSnapshot1.exists()) {
                                if(documentSnapshot1.get("isDoctor").toString().isNullOrEmpty()){
                                    isSender="0"
                                    adapter_topdoctor = ChatwithDoc_Adapter(doctorsuserchat_data,
                                        doctorsuserchat_time,doctorsuserchat_send,"0")
                                    recyclerView.adapter=adapter_topdoctor
                                }else{
                                    isSender="1"
                                    adapter_topdoctor = ChatwithDoc_Adapter(doctorsuserchat_data,
                                        doctorsuserchat_time,doctorsuserchat_send,"1")
                                    recyclerView.adapter=adapter_topdoctor
                                }
                                isActive=true
                            }
                        }
//---------------------------------------------------------------------------------------------------------
                }
            }
//---------------------------------Gửi tin nhắn----------------------------------------------------------
        val btn_send=findViewById<FrameLayout>(R.id.sendBtn)
        val edt_mess=findViewById<EditText>(R.id.editTextMessage)
        btn_send.setOnClickListener {
            if(isActive){
                doctorsuserchat_data.add(edt_mess.text.toString())
                doctorsuserchat_time.add(Timeminute())
                doctorsuserchat_send.add(isSender)
                SendtoFirebase()
                adapter_topdoctor.notifyDataSetChanged()
                recyclerView.scrollToPosition(doctorsuserchat_data.size - 1)
                edt_mess.setText("")
            }
        }
//-----------------------------------------Lắng nghe tin nhắn-------------------------------------------------------------------------
        db.collection("Conversation").document(idConversation).addSnapshotListener { documentSnapshot, exception ->
            if (exception != null) {
                Log.e("Firestore", "Listen failed"+ exception)
                return@addSnapshotListener
            }
            if (documentSnapshot != null && documentSnapshot.exists() && isSender!="") {
                val data = documentSnapshot.data
                if (data != null && doctorsuserchat_data.size>1) {
                    if(data.get("sender")!=isSender){
                        doctorsuserchat_data.add(data.get("lastmess").toString())
                        doctorsuserchat_time.add(data.get("time").toString())
                        doctorsuserchat_send.add(data.get("sender").toString())
                        adapter_topdoctor.notifyDataSetChanged()
                    }
                }
            }
        }

    }
    fun SendtoFirebase(){
        val chat_data: Chat =Chat(idConversation,doctorsuserchat_data,doctorsuserchat_time,doctorsuserchat_send)
        chat_data.UpdateDataonFirebase()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun Timeminute():String {
        val currentTime = LocalTime.now()
        val hour = currentTime.hour
        val minute = currentTime.minute
        var t:String =""
        var t1:String=""
        if(minute<10) { t="0"}
        if(hour<10){t1="0"}
        return t1+hour.toString()+":"+t+minute.toString()
    }
}