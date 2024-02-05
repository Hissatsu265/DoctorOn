package com.example.doctoron.Objects

import com.google.firebase.firestore.FirebaseFirestore

class User(private var name:String="",private var pass:String="",private var gmail:String="",
           private var age:Int=0,private var DoB:String="",private var sex:Int=0) {
    init{
        name=name
        age=age
        pass=pass
        gmail=gmail
        DoB=DoB
        sex=sex
    }
    // gửi dữ liệu lên firebase
    fun SendtoFirebase(){
        val db = FirebaseFirestore.getInstance()
        // Tham chiếu đến collection trong Firestore
        val collectionRef = db.collection("users")
        val user = hashMapOf(
            "name" to name,
            "age" to age,
            "pass" to pass,
            "gmail" to gmail,
            "DoB" to DoB,
            "sex" to sex
        )
        collectionRef.add(user)
            .addOnSuccessListener { documentReference ->
                // xử lí thành công
            }
            .addOnFailureListener { e ->
                //xữ lí thất  bại
            }
    }

}