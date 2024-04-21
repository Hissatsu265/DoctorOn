package com.example.doctoron.Fragments

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.doctoron.Activities.Chatwithdoctor
import com.example.doctoron.Activities.Doctor_Profile
import com.example.doctoron.Activities.Drug_info
import com.example.doctoron.Adapters.RecentChat_Adapter
import com.example.doctoron.Adapters.Topdoctor
import com.example.doctoron.Adapters.UserchatDoctor
import com.example.doctoron.Interface.OnItemClickListener
import com.example.doctoron.Interface.OnItemClickListener1
import com.example.doctoron.Objects.Chat
import com.example.doctoron.Objects.Doctor
import com.example.doctoron.Objects.Doctor_userchat
import com.example.doctoron.Objects.RecentChat_Object

import com.example.doctoron.R
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.io.Serializable
import java.time.LocalTime
import kotlin.math.log

class MessagesFragment : Fragment() , OnItemClickListener,OnItemClickListener1{

    private var userId:String=""
    lateinit var doctorsuserchat: ArrayList<Doctor_userchat>
    lateinit var recentChat:ArrayList<RecentChat_Object>
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerView_recenrchat:RecyclerView
    lateinit var adapter_recent:RecentChat_Adapter
    var t:Int=1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userId = arguments?.getString("user_ID").toString()
        val view= inflater.inflate(R.layout.fragment_chat_from_home, container, false)
        //--------------------------------Render top user ở trên------------------------------------
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
        //-----------------------Render recent message----------------------------------------

        recyclerView_recenrchat=view.findViewById(R.id.rvRecentChats)
        recyclerView_recenrchat.layoutManager=
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerView_recenrchat.setHasFixedSize(true)

        recentChat= ArrayList()

        try {
            val db1 = FirebaseFirestore.getInstance()
            val collectionRef1 = db1.collection("Conversation")
            collectionRef1.get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val data=document.data
                        if(data.get("userA")==userId){
                            t=0
                            val data_recent=RecentChat_Object(data.get("lastmess").toString()
                                ,data.get("time").toString(),data.get("userB").toString()
                                ,document.id.toString())
                            recentChat.add(data_recent)
                        }else if(data.get("userB")==userId){
                            t=0
                            val data_recent=RecentChat_Object(data.get("lastmess").toString()
                                ,data.get("time").toString(),data.get("userA").toString()
                                ,document.id.toString())
                            recentChat.add(data_recent)
                        }
                    }
                    Log.d("TAGloooo", "onCreateView: "+recentChat.toString())
                    adapter_recent = RecentChat_Adapter(recentChat,this)
                    recentChat.sortWith(Comparator { obj1, obj2 ->
                            obj1.getTime().compareTo(obj2.getTime())
                        })
                    recyclerView_recenrchat.adapter=adapter_recent
                }
                .addOnFailureListener { exception ->
                    Log.w("Error", "Error getting documents: ", exception)
                }
        }catch (e:Exception){
            Log.d("TAGloiiii", "onCreateView: "+e.message.toString())
        }
        //------------------------------Lắng nghe trên conversation--------------------------------
        val db2=FirebaseFirestore.getInstance()
        db2.collection("Conversation").addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                println("Có lỗi xảy ra: $exception")
                return@addSnapshotListener
            }
            if (snapshot != null && t==0) {
                for (document in snapshot.documents) {
                   AddRecentChat(document)
                }
            }
        }
        //-----------------------------------------------------------------------------
        return view
    }
    fun AddRecentChat(documentID:DocumentSnapshot){
        val data=documentID.data
        if (data != null) {
            Log.d("TAGtttttttttt", "AddRecentChat: "+data.get("time"))
            Log.d("TAGtttttttttt", "AddRecentChat: "+data.get("userB"))
        }
        if (data != null) {
            if (data.get("userA") == userId) {
                val data_recent = RecentChat_Object(
                    data.get("lastmess").toString(),
                    data.get("time").toString(),
                    data.get("userB").toString(),
                    documentID.id
                )
                recentChat.add(0, data_recent)
            } else if (data.get("userB") == userId) {
                val data_recent = RecentChat_Object(
                    data.get("lastmess").toString(),
                    data.get("time").toString(),
                    data.get("userA").toString(),
                    documentID.id
                )
                recentChat.add( data_recent)
            }

            var i: Int = -1
            for (index in 0 until recentChat.size-1) {
                if (recentChat[index].getIDConver() == documentID.id ){
                    i=index
                    break
                }
            }
            if (i > -1) {
                recentChat.removeAt(i)
            }

            try{
                adapter_recent.notifyDataSetChanged()
            }
            catch (e:Exception){
                Log.d("TAGloiii", "AddRecentChat: "+e.message.toString())
            }

        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onItemClick(position: Int) {
        try {
            val intent122= Intent(activity, Chatwithdoctor::class.java)
            val bundle = Bundle()
            val data=doctorsuserchat[position] as Serializable
            bundle.putSerializable("Userchat",data)
            intent122.putExtras(bundle)
            if(userId<doctorsuserchat[position].getId()){
                CheckandInit(userId,doctorsuserchat[position].getId())
                intent122.putExtra("Id_Con",userId+doctorsuserchat[position].getId())
            }else{
                CheckandInit(doctorsuserchat[position].getId(),userId)
                intent122.putExtra("Id_Con",doctorsuserchat[position].getId()+userId)
            }
            intent122.putExtra("Id_user",userId)
            startActivity(intent122)
        }catch (e:Exception){
            Log.d("TAGhhhhhhhhhh", "onItemClick: "+e.message.toString())
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun CheckandInit(a:String, b:String){
        var idConversation:String=a+b
        val db = FirebaseFirestore.getInstance()
        val collectionRef = db.collection("Conversation")
        collectionRef.document(idConversation)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                } else {
//        ---------------------------Khi chưa bao giờ nhắn cho nhau----------------------------------------
                    val data = hashMapOf(
                        "userA" to a,
                        "userB" to b,
                        "time" to Timeminute(),
                        "lastmess" to "Chào bạn, tôi có thể giúp gì cho bạn!",
                        "sender" to "1",
                    )
                    collectionRef.document(idConversation)
                        .set(data)
                        .addOnSuccessListener {
                        }
                        .addOnFailureListener { exception1 ->
                        }
//            --------------------------------------------------
                    var arr :ArrayList<String>
                    arr=ArrayList()
                    var chatwithdoc: Chat =Chat(idConversation,arr,arr,arr)
                    if(a==userId)
                        chatwithdoc.Initchat()
                    else
                        chatwithdoc.Initchat()
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
        var t:String =""
        var t1:String=""
        if(minute<10) { t="0"}
        if(hour<10){t1="0"}
        return t1+hour.toString()+":"+t+minute.toString()
    }

    override fun onItemClick1(position: Int) {
        val intent1221= Intent(activity, Chatwithdoctor::class.java)
        val bundle = Bundle()
        recentChat[position].getUrlavatarandName { _ , name ->
            val dataDoctor=Doctor_userchat(recentChat[position].getId(),name)
            val data= dataDoctor as Serializable
            bundle.putSerializable("Userchat",data)
            intent1221.putExtras(bundle)
            if(userId<recentChat[position].getId()){
                intent1221.putExtra("Id_Con",userId+recentChat[position].getId())
            }else{
                intent1221.putExtra("Id_Con",recentChat[position].getId()+userId)
            }
            intent1221.putExtra("Id_user",userId)
            startActivity(intent1221)
        }
    }

}