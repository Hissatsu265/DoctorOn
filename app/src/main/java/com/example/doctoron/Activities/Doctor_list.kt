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
import kotlin.math.log

class Doctor_list : AppCompatActivity() , OnItemClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_list)
        val recyclerView=findViewById<RecyclerView>(R.id.rv_listdoctor)
        recyclerView.layoutManager=
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.setHasFixedSize(true)
        var  doctors:ArrayList<Doctor> = ArrayList()
        var  doctors_copy:ArrayList<Doctor> = ArrayList()
        val btn_back=findViewById<ImageView>(R.id.back_btn)
        btn_back.setOnClickListener {
            finish()
        }

        for (i in 1 until 8) {
            val data = Doctor("Minh hy", "", "", 0, "", 0, 5, "Thai sản","","Bệnh viện Thủ Đức")
            doctors.add(data)
        }
        val data = Doctor("Toàn", "", "", 0, "", 0, 5, "Khoa ngoại","","Bệnh viện Thủ Đức")
        doctors.add(data)
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
    override fun onItemClick(position: Int) {
        Toast.makeText(this,position.toString(),Toast.LENGTH_SHORT).show()
        val intent= Intent(this,Doctor_Profile::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left)
    }
}