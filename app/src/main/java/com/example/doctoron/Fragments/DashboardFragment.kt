package com.example.doctoron.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.ImageButton
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
import com.example.doctoron.Activities.Drug_info
import com.example.doctoron.Activities.Predict_health
import com.example.doctoron.Activities.Xray_predict
import com.example.doctoron.Adapters.Drug_Adapter
import com.example.doctoron.Adapters.Topdoctor
import com.example.doctoron.Interface.OnItemClickListener
import com.example.doctoron.Interface.OnitemDrugClickListener
import com.example.doctoron.Objects.Doctor
import com.example.doctoron.Objects.Drug
import com.example.doctoron.R
import com.google.firebase.firestore.FirebaseFirestore
import java.io.Serializable
import kotlin.math.log

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
class DashboardFragment : Fragment() , OnItemClickListener,OnitemDrugClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var doctors: ArrayList<Doctor>
    private lateinit var drugs:ArrayList<Drug>
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
        //----------------------------------------------------------------------------------
        var btn_predict=view.findViewById<ImageButton>(R.id.button6)
        btn_predict.setOnClickListener {
            val intent= Intent(activity,Predict_health::class.java)
            startActivity(intent)
        }
        var btn_xray=view.findViewById<ImageButton>(R.id.button2)
        btn_xray.setOnClickListener {
            try{
                val intent9= Intent(activity,Xray_predict::class.java)
                startActivity(intent9)
            }
            catch (e:Exception) {
                Log.d("lllllloi", "onCreateView: " + e.message.toString())
            }
        }
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
        //---------------------------Drug--------------------------------------------------------
        val rview_Drug=view.findViewById<RecyclerView>(R.id.rv_drug)
        rview_Drug.layoutManager=LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL, false)
        rview_Drug.setHasFixedSize(true)
        drugs= ArrayList<Drug>()

        val db1=FirebaseFirestore.getInstance()
        db1.collection("Drug")
            .limit(3)
            .get().addOnSuccessListener { querySnapshot ->
                for (doc in querySnapshot.documents) {
                    val data=doc.data
                    val drug1 = Drug(data?.get("name").toString(),data?.get("dactri").toString(),
                        data?.get("chongchidinh").toString(),data?.get("tp").toString(),data?.get("price").toString(),
                        data?.get("use").toString(),data?.get("tdphu").toString(),doc.id,data?.get("imageUrl").toString())
                    drugs.add(drug1)
                }
                var adapter_drug = Drug_Adapter(drugs,this)
                rview_Drug.adapter=adapter_drug
            }

        //------------------------------------------------------------------
        return view
    }
    override fun onItemClick(position: Int) {
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

    override fun onItemDrugClick(position: Int) {
        val intent11= Intent(activity,Drug_info::class.java)
        val bundle = Bundle()
        val drug=drugs[position] as Serializable
        bundle.putSerializable("drugtt",drug)
        intent11.putExtras(bundle)
//        intent.putExtra("ID_drug",drugs[position].getID())
        startActivity(intent11)
//        Log.d("loiroi", "onItemDrugClick: "+ position.toString())
    }
}