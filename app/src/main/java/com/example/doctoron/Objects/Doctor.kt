package com.example.doctoron.Objects

import com.google.firebase.firestore.FirebaseFirestore

class Doctor(name: String = "", pass: String = "", gmail: String = "",
             age: Int = 0, DoB: String = "", sex: Int = 0, private var star:Int=0,private var CN: String = "",
             private var iddoctor:String = "",private var Hospital:String="") : User(name, pass, gmail, age, DoB, sex) {

    init {
        CN=CN
        star=star
        iddoctor=iddoctor
        Hospital=Hospital
    }
    fun getHospital():String{
        return Hospital
    }
    // Ghi đè phương thức SendtoFirebase của lớp cha User
    override fun SendtoFirebase(id: String) {
        iddoctor=id
        super.SendtoFirebase(id)

        val db = FirebaseFirestore.getInstance()
        val doctorInfo = hashMapOf(
            "CN" to CN,
            "star" to star
        )

        db.collection("Doctors")
            .document(id)
            .set(doctorInfo)
            .addOnSuccessListener {
                // Xử lý thành công
            }
            .addOnFailureListener {
                // Xử lý lỗi
            }

        val data = hashMapOf(
            "isDoctor" to id,
        )
        db.collection("users").document(id)
            .update(data as Map<String, Any>)
            .addOnSuccessListener {
            }
            .addOnFailureListener { e ->
            }
    }
    override fun getName():String{
        return super.getName()
    }
    fun getCN():String{
        return CN
    }
    fun getStar():Int{
        return star
    }
    fun getID():String{
        return iddoctor
    }

}

