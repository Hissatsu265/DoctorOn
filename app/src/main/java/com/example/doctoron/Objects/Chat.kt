package com.example.doctoron.Objects

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.firestore.FirebaseFirestore
import java.io.Serializable
import java.time.LocalTime

class Chat(private var idchat:String="",private var arr_mess:ArrayList<String>,
           private var arr_time:ArrayList<String>,private var arr_send:ArrayList<String>) : Serializable {
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

        var t:String =""
        var t1:String=""
        if(minute<10) { t="0"}
        if(hour<10){t1="0"}
        return t1+hour.toString()+":"+t+minute.toString()
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
    fun UpdateDataonFirebase(){
        val data = hashMapOf(
            "messs" to arr_mess,
            "times" to arr_time,
            "sends" to arr_send,
        )
        val data1 = hashMapOf(
            "lastmess" to arr_mess.last(),
            "time" to arr_time.last(),
            "sender" to arr_send.last(),
        )
        val db=FirebaseFirestore.getInstance()
        db.collection("Messages").document(idchat)
            .update(data as Map<String, Any>)
            .addOnSuccessListener {
            }
            .addOnFailureListener { exception ->
            }
        db.collection("Conversation").document(idchat)
            .update(data1 as Map<String, Any>)
            .addOnSuccessListener {
            }
            .addOnFailureListener { exception ->
            }
    }

}