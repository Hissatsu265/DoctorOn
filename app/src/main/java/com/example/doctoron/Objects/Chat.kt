package com.example.doctoron.Objects

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.firestore.FirebaseFirestore
import java.io.Serializable
import java.time.LocalTime

class Chat(private var idchat:String="") : Serializable {
    private lateinit var mess:ArrayList<String>
    private lateinit var time:ArrayList<String>
    private lateinit var send:ArrayList<String>
    init {
        idchat=idchat
        mess= ArrayList()
        time= ArrayList()
        send= ArrayList()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun Timeminute():String {
        val currentTime = LocalTime.now()
        val hour = currentTime.hour
        val minute = currentTime.minute
        return hour.toString()+":"+minute.toString()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun Initchat(){
        mess.add("Chào bạn, tôi có thể giúp gì cho bạn")
        time.add(Timeminute())
        send.add("1")
        val data = hashMapOf(
            "messs" to mess,
            "times" to time,
            "sends" to send,
        )
        val db=FirebaseFirestore.getInstance()
        db.collection("Messages").document(idchat)
            .set(data)
            .addOnSuccessListener {
            }
            .addOnFailureListener { exception ->
            }
    }
    fun getDataonFirebase(){

    }
    fun Addmess(content:String="",sender:String="0"){

    }
}