package com.example.doctoron.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.ImageView.ScaleType
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.example.doctoron.Activities.Doctor_Profile
import com.example.doctoron.Activities.Doctor_list
import com.example.doctoron.Adapters.Topdoctor
import com.example.doctoron.Interface.OnItemClickListener
import com.example.doctoron.Objects.Doctor
import com.example.doctoron.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.math.log

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
class DashboardFragment : Fragment() , OnItemClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var doctors: ArrayList<Doctor>
    private var userId:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userId = arguments?.getString("user_ID").toString()
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        //-------------------------------Image slider ------------------------------------------------
        var slideModels:ArrayList<SlideModel> = ArrayList()
        var imageSlider:ImageSlider= view.findViewById(R.id.imageSlider)

        slideModels.add(SlideModel(R.drawable.doctor_splashscreen, ScaleTypes.FIT))
        slideModels.add(SlideModel(R.drawable.img_drug,ScaleTypes.FIT))
        slideModels.add(SlideModel(R.drawable.img_lich,ScaleTypes.FIT))

        imageSlider.setImageList(slideModels,ScaleTypes.FIT)
        imageSlider.setItemClickListener(object : ItemClickListener {
            override fun onItemSelected(position: Int) {
                when(position){
                    0 -> {
                        Log.d("click", "onItemSelected: 1")
                    }
                    1 -> {
                        Log.d("click", "onItemSelected: 2")
                    }
                    2 -> {
                        Log.d("click", "onItemSelected: 3")
                    }
                }
            }
        })
        //----------------------------------Top doctor----------------------------------------
        recyclerView=view.findViewById(R.id.rv_topdoctor)
        recyclerView.layoutManager=LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL, false)
        recyclerView.setHasFixedSize(true)
        doctors= ArrayList<Doctor>()
        val db=FirebaseFirestore.getInstance()
        db.collection("Doctors")
            .orderBy("star",com.google.firebase.firestore.Query.Direction.DESCENDING)
            .limit(5)
            .get().addOnSuccessListener { querySnapshot ->
                for (doc in querySnapshot.documents) {
                    val data=doc.data
                    val doctor1 = Doctor(
                        data?.get("name").toString(), "", "", 0, "", 0, data?.get("star").toString().toInt(),
                        data?.get("CN").toString(),doc.id,data?.get("BV").toString())
                    doctors.add(doctor1)
                }
                var adapter_topdoctor = Topdoctor(doctors,this)
                recyclerView.adapter=adapter_topdoctor
            }


        //----------------------------------------------------------
        val tv_listdoctor=view.findViewById<TextView>(R.id.tv_listdoctor_xt)
        tv_listdoctor.setOnClickListener {
            val intent= Intent(activity,Doctor_list::class.java)
            intent.putExtra("User_ID_user",userId)
            startActivity(intent)
        }
        //-----------------------------------------------------------
        return view
    }
    override fun onItemClick(position: Int) {
//        Log.d("ha",position.toString())
        val intent= Intent(activity,Doctor_Profile::class.java)
        intent.putExtra("User_ID_user",userId)
        intent.putExtra("User_ID",doctors[position].getID())
        startActivity(intent)
    }
    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DashboardFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}