package com.example.doctoron.Objects

import com.google.firebase.firestore.FirebaseFirestore

import kotlin.collections.ArrayList

open class User(private var name:String="", private var pass:String="", private var gmail:String="",
                private var age:Int=0, private var DoB:String="", private var sex:Int=0) {
    init{
        name=name
        age=age
        pass=pass
        gmail=gmail
        DoB=DoB
        sex=sex
    }
    open fun getName():String{
        return name
    }
    // gửi dữ liệu lên firebase
    open fun SendtoFirebase(id:String){
        val db = FirebaseFirestore.getInstance()

        val arr :ArrayList<String> = ArrayList()
        val user = hashMapOf(
            "name" to name,
            "age" to age,
            "pass" to pass,
            "gmail" to gmail,
            "DoB" to DoB,
            "sex" to sex,
            "address" to "",
            "isDoctor" to "",
            "Calender" to arr
        )
        db.collection("users")
            .document(id)
            .set(user)
            .addOnSuccessListener {
            }
            .addOnFailureListener {
            }
        val ar= DoubleArray(31) { 0.5 }.toList()
        val data = hashMapOf<String, List<Double>>()

        for (i in 1..12) {
            data["bmi_$i"] = ar
            data["calo_$i"] = ar
            data["heartrace_$i"] = ar
        }
        db.collection("Chiso")
            .document(id)
            .set(data)
            .addOnSuccessListener {
            }
            .addOnFailureListener {
            }
    }


}