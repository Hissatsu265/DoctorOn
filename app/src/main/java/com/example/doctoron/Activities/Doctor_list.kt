package com.example.doctoron.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.doctoron.Adapters.DoctorList
import com.example.doctoron.Interface.OnItemClickListener
import com.example.doctoron.Objects.Doctor
import com.example.doctoron.R
import com.google.firebase.firestore.FirebaseFirestore

class Doctor_list : AppCompatActivity() , OnItemClickListener {
    private lateinit var doctors:ArrayList<Doctor>
    private lateinit var userID_user:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userID_user=intent.getStringExtra("User_ID_user").toString()

        setContentView(R.layout.activity_doctor_list)
        val recyclerView=findViewById<RecyclerView>(R.id.rv_listdoctor)
        recyclerView.layoutManager=
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.setHasFixedSize(true)
        doctors = ArrayList()
        var  doctors_copy:ArrayList<Doctor> = ArrayList()
        val btn_back=findViewById<ImageView>(R.id.back_btn)
        btn_back.setOnClickListener {
            finish()
        }
        //------------------------Lay du lieu----------------------------------------------------
//        LayDulieutuFirebase()
        val Listeneractivity=this
        LayDulieutuFirebase(object : OnDataFetchListener {
            override fun onDataFetchComplete() {
                //-----------sao chep mang qua --------------
                doctors_copy.addAll(doctors)
                //------------------------------------------

                var adapter_topdoctor = DoctorList( doctors_copy,Listeneractivity)
                recyclerView.adapter=adapter_topdoctor
                //----------------------------------------------------------

                val searchView = findViewById<androidx.appcompat.widget.SearchView>(R.id.search_view_doctor)
                searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return false
                    }
                    override fun onQueryTextChange(newText: String?): Boolean {
                        if (newText!=null && newText!=""){
                            val query = newText.toString().toLowerCase()
                            val filteredList = doctors.filter {
                                it.getName().toLowerCase().contains(query)
                            }
                            doctors_copy.clear()
                            doctors_copy.addAll(filteredList)
                            adapter_topdoctor.notifyDataSetChanged()
                        }
                        else if(doctors.size != doctors_copy.size){
                            doctors_copy.clear()
                            doctors_copy.addAll(doctors)
                            adapter_topdoctor.notifyDataSetChanged()
                        }
                        return true
                    }
                })
            }
        })


    }
    fun LayDulieutuFirebase(listener: OnDataFetchListener) {
        val db = FirebaseFirestore.getInstance()
        // Tham chiếu đến collection cụ thể
        val collectionRef = db.collection("Doctors")

        // Lấy tất cả các document trong collection
        collectionRef.get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    // Lấy data của document
                    val data = document.data
                    val doctor = Doctor(data.get("name").toString(), "", "", 0, "", 0, data.get("star").toString().toInt(),
                        data.get("CN").toString(),document.id,data.get("BV").toString())
                    doctors.add(doctor)
                }
                listener.onDataFetchComplete()
            }
            .addOnFailureListener { exception ->
            }
    }
    interface OnDataFetchListener {
        fun onDataFetchComplete()
    }

    override fun onItemClick(position: Int) {
//        Toast.makeText(this,position.toString(),Toast.LENGTH_SHORT).show()
        val intent= Intent(this,Doctor_Profile::class.java)
        intent.putExtra("User_ID",doctors.get(position).getID())
        intent.putExtra("User_ID_user",userID_user)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left)
    }
}