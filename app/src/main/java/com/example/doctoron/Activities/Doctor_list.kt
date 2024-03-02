package com.example.doctoron.Activities

import android.content.ContentProvider
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.doctoron.Adapters.DoctorList
import com.example.doctoron.Adapters.Topdoctor
import com.example.doctoron.Interface.OnItemClickListener
import com.example.doctoron.Objects.Doctor
import com.example.doctoron.R

class Doctor_list : AppCompatActivity() , OnItemClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_list)
        val recyclerView=findViewById<RecyclerView>(R.id.rv_listdoctor)
        recyclerView.layoutManager=
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.setHasFixedSize(true)
        var  doctors:ArrayList<Doctor> = ArrayList<Doctor>()
        for (i in 1 until 8) {
            val data = Doctor("Minh hy", "", "", 0, "", 0, 5, "Thai sản","","Thủ Đức")
            doctors.add(data)
        }
        var adapter_topdoctor = DoctorList( doctors,this)
        recyclerView.adapter=adapter_topdoctor
        //----------------------------------------------------------
    }


    override fun onItemClick(position: Int) {
        Toast.makeText(this,position.toString(),Toast.LENGTH_SHORT).show()
    }
}