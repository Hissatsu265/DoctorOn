package com.example.doctoron.Activities

import android.content.ContentProvider
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.doctoron.Adapters.DoctorList
import com.example.doctoron.Adapters.Topdoctor
import com.example.doctoron.Interface.OnItemClickListener
import com.example.doctoron.Objects.Doctor
import com.example.doctoron.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.math.log

class Doctor_list : AppCompatActivity() , OnItemClickListener {
    private lateinit var doctors:ArrayList<Doctor>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        LayDulieutuFirebase()
        //-----------sao chep mang qua --------------
        doctors_copy.addAll(doctors)
        //------------------------------------------

        var adapter_topdoctor = DoctorList( doctors_copy,this)
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
    fun LayDulieutuFirebase(){
        val db = FirebaseFirestore.getInstance()
        // Tham chiếu đến collection cụ thể
        val collectionRef = db.collection("Doctors")

        // Lấy tất cả các document trong collection
        collectionRef.get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    // Lấy data của document
                    val data = document.data
                    val doctor = Doctor(data.get("name").toString(), "", "", 0, "", 0, 5,
                        data.get("CN").toString(),document.id,data.get("BV").toString())
                    doctors.add(doctor)
                }
            }
            .addOnFailureListener { exception ->
            }
    }
    override fun onItemClick(position: Int) {
        Toast.makeText(this,position.toString(),Toast.LENGTH_SHORT).show()
        val intent= Intent(this,Doctor_Profile::class.java)
        intent.putExtra("User_ID",doctors.get(position).getID())
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left)
    }
}