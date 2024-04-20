package com.example.doctoron.Objects

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import java.io.Serializable

class Doctor_userchat(private var id:String="",private var name:String=""): Serializable {
    private var url_image:String=""
    init {
        id=id
        name=name
    }
    fun getId():String{
        return id
    }
    fun getName():String{
        return name
    }
    fun getUrlavatar(callback: (String) -> Unit) {
        if (url_image.isNotEmpty()) {
            callback(url_image)
        } else {
            val db = FirebaseFirestore.getInstance()
            db.collection("users")
                .document(id)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        url_image = document.get("avatarUrl").toString()
                        Log.d("1", "get " + url_image)
                        callback(url_image)
                    }
                }
                .addOnFailureListener { exception ->
//                    Log.w("TAG", "Error getting documents.", exception)
                }
        }
    }
}