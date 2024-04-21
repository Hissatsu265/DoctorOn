package com.example.doctoron.Objects

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.math.log

class RecentChat_Object(private var lastmess:String="", private var time:String="",
                        private var id:String= "",private var id_conver:String="") {
    private var url_image:String=""
    private var name:String=""
    init {
        lastmess=lastmess
        time=time
        id=id
        id_conver=id_conver
    }
    fun getIDConver():String{
        return id_conver
    }
    fun getLastmess():String{
        return lastmess
    }
    fun getId():String{
            return id
    }
    fun getTime():String{
        return time
    }
    fun getUrlavatarandName(callback: (String,String) -> Unit) {
        if (url_image.isNotEmpty()) {
            callback(url_image,name)
        } else {
            try {
                val db = FirebaseFirestore.getInstance()
                db.collection("users")
                    .document(id)
                    .get()
                    .addOnSuccessListener { document ->
                        if (document != null) {
                            name=document.get("name").toString()
                            url_image = document.get("avatarUrl").toString()
                            Log.d("1", "get " + url_image)
                            callback(url_image,name)
                        }
                    }
                    .addOnFailureListener { exception ->
//                    Log.w("TAG", "Error getting documents.", exception)
                    }
            }catch (e:Exception){
                Log.d("TAGloi1", "getUrlavatarandName: "+e.message.toString())
            }

        }
    }
}