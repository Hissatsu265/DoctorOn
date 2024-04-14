package com.example.doctoron.Fragments

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.doctoron.Activities.Chatwithdoctor
import com.example.doctoron.Activities.Doctor_Profile
import com.example.doctoron.Activities.Drug_info
import com.example.doctoron.Adapters.Topdoctor
import com.example.doctoron.Adapters.UserchatDoctor
import com.example.doctoron.Interface.OnItemClickListener
import com.example.doctoron.Objects.Doctor
import com.example.doctoron.Objects.Doctor_userchat

import com.example.doctoron.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.io.Serializable
import java.time.LocalTime
class MessagesFragment : Fragment() , OnItemClickListener{

    private var userId:String=""
    lateinit var doctorsuserchat: ArrayList<Doctor_userchat>
    private lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userId = arguments?.getString("user_ID").toString()
        val view= inflater.inflate(R.layout.fragment_chat_from_home, container, false)
        //----------------------------------------------------------------------------------------------
        recyclerView=view.findViewById(R.id.rvUsers)
        recyclerView.layoutManager=
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.setHasFixedSize(true)
        doctorsuserchat= ArrayList<Doctor_userchat>()
        val db=FirebaseFirestore.getInstance()
        db.collection("Doctors")
            .orderBy("star",com.google.firebase.firestore.Query.Direction.DESCENDING)
            .limit(5)
            .get().addOnSuccessListener { querySnapshot ->
                for (doc in querySnapshot.documents) {
                    val data=doc.data
                    val Uesrchat = Doctor_userchat(doc.id, data?.get("name").toString())
                    doctorsuserchat.add(Uesrchat)
                }
                var adapter_topdoctor = UserchatDoctor(doctorsuserchat,this)
                recyclerView.adapter=adapter_topdoctor
            }
        return view
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onItemClick(position: Int) {
        try {
            val intent122= Intent(activity, Chatwithdoctor::class.java)
            val bundle = Bundle()
            val data=doctorsuserchat[position] as Serializable
            bundle.putSerializable("Userchat",data)
            intent122.putExtras(bundle)

            CheckandInit(userId,doctorsuserchat[position].getId())

            startActivity(intent122)
        }catch (e:Exception){
            Log.d("TAGhhhhhhhhhh", "onItemClick: "+e.message.toString())
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun CheckandInit(a:String, b:String){
        var idConversation:String=""
        if(b<a){idConversation=b+a}
        else{ idConversation=a+b}

        val db = FirebaseFirestore.getInstance()
        val collectionRef = db.collection("tên_bộ_sưu_tập")


        collectionRef.document(idConversation)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    // Tài liệu tồn tại trong bộ sưu tập
                } else {
                    var t1:String=""
                    var t2:String=""
                    if (b<a) {
                         t1=b
                         t2=a
                    }else{
                        t1=a
                        t2=b
                    }
//        ----------------------------------------------------------------------
                    val data = hashMapOf(
                        "userA" to t1,
                        "userB" to t2,
                        "time" to Timeminute(),
                        "lastmess" to "Chào bạn, tôi có thể giúp gì cho bạn",
                    )
                    collectionRef.document(idConversation)
                        .set(data)
                        .addOnSuccessListener {
                        }
                        .addOnFailureListener { exception ->
                        }

//        ----------------------------------------------------------------------

                }
            }
            .addOnFailureListener { exception ->

            }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun Timeminute():String {
        val currentTime = LocalTime.now()
        val hour = currentTime.hour
        val minute = currentTime.minute
        return hour.toString()+":"+minute.toString()
    }

}