package com.example.doctoron.Activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.doctoron.Objects.Calendar_Time
import com.example.doctoron.Objects.Chat
import com.example.doctoron.Objects.Doctor_userchat
import com.example.doctoron.R
import com.google.firebase.firestore.FirebaseFirestore
import java.io.Serializable
import java.time.LocalTime
import java.util.Calendar

class Doctor_Profile : AppCompatActivity() {
    lateinit var btn_Mon_week:Button
    lateinit var btn_Tue_week:Button
    lateinit var btn_Wed_week:Button
    lateinit var btn_Thu_week:Button
    lateinit var btn_Fri_week:Button
    lateinit var btn_Sat_week:Button

    lateinit var btn_hour_8am:Button
    lateinit var btn_hour_9am:Button
    lateinit var btn_hour_10am:Button
    lateinit var btn_hour_1pm:Button
    lateinit var btn_hour_2pm:Button
    lateinit var btn_hour_3pm:Button
    lateinit var btn_hour_4pm:Button
    lateinit var btn_hour_5pm:Button
    lateinit var btn_hour_6pm:Button
    lateinit var week_copy1:ArrayList<Long>
    var week:Int=0
    var hour:Int=0

    var userID:String="Y4G2Fkx4VKaTpxv7xkwvlTTArsF3"// của doctor

    var userID_user:String="gTt4CW1WkAVfIWxVcTJEyQ1Akwi2"//của người dùng
    lateinit var week_2:ArrayList<Long>
    lateinit var week_3:ArrayList<Long>
    lateinit var week_4:ArrayList<Long>
    lateinit var week_5:ArrayList<Long>
    lateinit var week_6:ArrayList<Long>
    lateinit var week_7:ArrayList<Long>
    var tenbacsi:String=" "
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_profile)
        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH) + 1
        val year = calendar.get(Calendar.YEAR)
    ///-------------------------------------------------
        week_copy1= ArrayList<Long>().apply {
            repeat(11){
                add(0)
            }
        }
        week_2=ArrayList()
        week_4=ArrayList()
        week_6=ArrayList()
        week_3=ArrayList()
        week_5=ArrayList()
        week_7=ArrayList()
        //-------------- sau nhớ mở ra đàng hoàng
        userID= intent.getStringExtra("User_ID").toString()
        userID_user=intent.getStringExtra("User_ID_user").toString()
        val db=FirebaseFirestore.getInstance()
        //----------------quay ve -------------------------------
        val btn_back=findViewById<ImageView>(R.id.back_btn)
        btn_back.setOnClickListener {
//            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left)
            finish()
        }
        //----------------------------Chat-------------------------------
        val btn_chat=findViewById<ImageButton>(R.id.btnChat)
        btn_chat.setOnClickListener {
            val intent122= Intent(this, Chatwithdoctor::class.java)
            val bundle = Bundle()
            val dataDoctor= Doctor_userchat(userID,tenbacsi)

            val data=dataDoctor as Serializable
            bundle.putSerializable("Userchat",data)
            intent122.putExtras(bundle)
            if(userID_user<userID){
                CheckandInit(userID_user,userID)
                intent122.putExtra("Id_Con",userID_user+userID)
            }else{
                CheckandInit(userID,userID_user)
                intent122.putExtra("Id_Con",userID+userID_user)
            }
            intent122.putExtra("Id_user",userID_user)
            startActivity(intent122)
        }
        //-------------------------------------------------------------
        val btn_booking=findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.btn_booking)
        btn_booking.setOnClickListener {
            if(week!=0 && hour!=0){
                (week_copy1 as MutableList<Int>)[hour]=1
                val doctorInfo = hashMapOf(
                    "week_$week" to week_copy1
                )
                db.collection("Doctors")
                    .document(userID)
                    .update(doctorInfo as Map<String, Any>)
                    .addOnSuccessListener {
                       Toast.makeText(this,"Đặt hẹn thành công!",Toast.LENGTH_LONG).show()
                    }
                    .addOnFailureListener {

                        Toast.makeText(this,"Đặt hẹn thất bại!",Toast.LENGTH_LONG).show()
                    }
                val str:String=shiftDate(day,month,year,week)

                UpdateDLUser("$str;$hour;bác sĩ $tenbacsi",userID_user)
                UpdateDLUser("$str;$hour;bệnh nhân",userID)

            }
        }
        //-----------------------set profile-------------------------------

        var Name=findViewById<TextView>(R.id.tv_name)
        var star=findViewById<TextView>(R.id.tv_star)
        var about=findViewById<TextView>(R.id.tv_about)
        var CN=findViewById<TextView>(R.id.tv_CN)
        var BV=findViewById<TextView>(R.id.tv_bv)
        var Avatar=findViewById<ImageView>(R.id.iv_avatar)
        db.collection("Doctors")
            .document(userID)
            .get()
            .addOnSuccessListener { document ->
                run {
                    if (document != null) {
                        tenbacsi=document.get("name").toString()
                        Name.setText(tenbacsi)
                        star.setText(document.get("star").toString())
                        about.setText(document.get("about").toString())
                        CN.setText(document.get("CN").toString())
                        BV.setText(document.get("BV").toString())

                        week_2= document.get("week_2") as ArrayList<Long>
                        week_3= document.get("week_3") as ArrayList<Long>
                        week_4= document.get("week_4") as ArrayList<Long>
                        week_5= document.get("week_5") as ArrayList<Long>
                        week_6= document.get("week_6") as ArrayList<Long>
                        week_7= document.get("week_7") as ArrayList<Long>

                    }
                }
            }
        db.collection("users")
            .document(userID)
            .get()
            .addOnSuccessListener { document ->
                run {
                    if (document != null) {
                        val avatarUrl:String=document.get("avatarUrl").toString()
                        if (!avatarUrl.isNullOrEmpty()) {
                            Glide.with(this)
                                .load(avatarUrl)
                                .into(Avatar)
                        }
                    }
                }
            }

        //----------------chon thu-------------------------------------
        btn_Mon_week=findViewById(R.id.btn_mon)
        btn_Tue_week=findViewById(R.id.btn_Tue)
        btn_Wed_week=findViewById(R.id.btn_wed)
        btn_Thu_week=findViewById(R.id.btn_Thu)
        btn_Fri_week=findViewById(R.id.btn_Fri)
//        btn_Sat_week=findViewById(R.id.btn_Sat)

        btn_Mon_week.setText(shiftDate(day,month,year,2))
        btn_Tue_week.setText(shiftDate(day,month,year,3))
        btn_Wed_week.setText(shiftDate(day,month,year,4))
        btn_Thu_week.setText(shiftDate(day,month,year,5))
        btn_Fri_week.setText(shiftDate(day,month,year,6))


        btn_Mon_week.setOnClickListener {
            setBackgroundOriginal(2)
        }
        btn_Tue_week.setOnClickListener {
            setBackgroundOriginal(3)
        }
        btn_Wed_week.setOnClickListener {
            setBackgroundOriginal(4)
        }

        btn_Fri_week.setOnClickListener {
            setBackgroundOriginal(6)
        }
        btn_Thu_week.setOnClickListener {
            setBackgroundOriginal(5)
        }
        //-----------------------chon gio-------------------------------
        btn_hour_10am=findViewById(R.id.hour_10am)
        btn_hour_1pm=findViewById(R.id.hour_1pm)
        btn_hour_2pm=findViewById(R.id.hour_2pm)
        btn_hour_3pm=findViewById(R.id.hour_3pm)
        btn_hour_4pm=findViewById(R.id.hour_4pm)
        btn_hour_5pm=findViewById(R.id.hour_5pm)
        btn_hour_6pm=findViewById(R.id.hour_6pm)
        btn_hour_8am=findViewById(R.id.hour_8am)
        btn_hour_9am=findViewById(R.id.hour_9am)


        btn_hour_10am.setOnClickListener {
            setBacgroundOriginalforHour(10)
        }
        btn_hour_9am.setOnClickListener {
            setBacgroundOriginalforHour(9)
        }
        btn_hour_8am.setOnClickListener {
            setBacgroundOriginalforHour(8)
        }
        btn_hour_1pm.setOnClickListener {
            setBacgroundOriginalforHour(1)
        }
        btn_hour_2pm.setOnClickListener {
            setBacgroundOriginalforHour(2)
        }
        btn_hour_3pm.setOnClickListener {
            setBacgroundOriginalforHour(3)
        }
        btn_hour_4pm.setOnClickListener {
            setBacgroundOriginalforHour(4)
        }
        btn_hour_5pm.setOnClickListener {
            setBacgroundOriginalforHour(5)
        }
        btn_hour_6pm.setOnClickListener {
            setBacgroundOriginalforHour(6)
        }
    }
    fun setColor(btn:Button,i: Int){
        btn.setBackgroundColor(resources.getColor(i))
    }
    fun setWeek(a:ArrayList<Long>){
        try{if (a != null) {
            week_copy1.clear()
            for (i in 0..10)
                week_copy1.add(a.get(i))
        }}
        catch (e:Exception){
            Log.d("minh", "CheckFreeinWeek: "+e.message.toString())
        }
    }
    fun CheckFreeinWeek(i:Int):Boolean {

        when (week) {
            2 -> if (week_2 != null && week_2.get(i).toInt() == 0) {
                setWeek(week_2)
                return true
            }

            3 -> if (week_3 != null && week_3.get(i).toInt() == 0) {
                setWeek(week_3)
                return true
            }

            4 -> if (week_4 != null && week_4.get(i).toInt() == 0) {
                setWeek(week_4)
                return true
            }

            5 -> if (week_5 != null && week_5.get(i).toInt() == 0) {
                setWeek(week_5)
                return true
            }

            6 -> if (week_6 != null && week_6.get(i).toInt() == 0) {
                setWeek(week_6)
                return true
            }

            7 -> if (week_7 != null && week_7.get(i).toInt() == 0) {
                setWeek(week_7)
                return true
            }
        }
        return false
    }
    fun setBacgroundOriginalforHour(i:Int){
        if(CheckFreeinWeek(i)){
            setColor(btn_hour_10am,R.color.primary)
            setColor(btn_hour_9am,R.color.primary)
            setColor(btn_hour_8am,R.color.primary)
            setColor(btn_hour_1pm,R.color.primary)
            setColor(btn_hour_2pm,R.color.primary)
            setColor(btn_hour_3pm,R.color.primary)
            setColor(btn_hour_4pm,R.color.primary)
            setColor(btn_hour_5pm,R.color.primary)
            setColor(btn_hour_6pm,R.color.primary)
            hour=i
            when(i){
                10->  setColor(btn_hour_10am,R.color.cyan_300)
                9->   setColor(btn_hour_9am,R.color.cyan_300)
                8->   setColor(btn_hour_8am,R.color.cyan_300)
                1->   setColor(btn_hour_1pm,R.color.cyan_300)
                2->   setColor(btn_hour_2pm,R.color.cyan_300)
                3->   setColor(btn_hour_3pm,R.color.cyan_300)
                4->   setColor(btn_hour_4pm,R.color.cyan_300)
                5->   setColor(btn_hour_5pm,R.color.cyan_300)
                6->   setColor(btn_hour_6pm,R.color.cyan_300)
            }
        }
        else{
            Toast.makeText(this,"Bác sĩ đã bận vào giờ này",Toast.LENGTH_SHORT).show()
        }
    }
    fun setBackgroundOriginal(i:Int){
        week=i
        SetButonDisable()
        btn_Fri_week.setBackgroundColor(resources.getColor(R.color.primary))
        btn_Mon_week.setBackgroundColor(resources.getColor(R.color.primary))
        btn_Wed_week.setBackgroundColor(resources.getColor(R.color.primary))
        btn_Thu_week.setBackgroundColor(resources.getColor(R.color.primary))
//        btn_Sat_week.setBackgroundColor(resources.getColor(R.color.primary))
        btn_Tue_week.setBackgroundColor(resources.getColor(R.color.primary))
        when (i){
            2 -> btn_Mon_week.setBackgroundColor(resources.getColor(R.color.cyan_300))
            3 -> btn_Tue_week.setBackgroundColor(resources.getColor(R.color.cyan_300))
            4 -> btn_Wed_week.setBackgroundColor(resources.getColor(R.color.cyan_300))
            5 -> btn_Thu_week.setBackgroundColor(resources.getColor(R.color.cyan_300))
            6 -> btn_Fri_week.setBackgroundColor(resources.getColor(R.color.cyan_300))
//            7 -> btn_Sat_week.setBackgroundColor(resources.getColor(R.color.cyan_300))
        }
    }
    //-----------------------------------------------------------------------------------------
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
                    var chatwithdoc: Chat = Chat(idConversation,arr,arr,arr)
                    if(a==userID_user)
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
    //----------------------------------------------------------------------------------------
    fun getCurrentDate(): Int {
        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH) + 1
        val year = calendar.get(Calendar.YEAR)
        return day
    }
    fun shiftDate(day:Int,month:Int,year:Int, dayshift: Int): String {
        var day_1=day+dayshift
        var month_1=month

        when(month){
            1,3,5,7,8,10,12-> if (day_1>31) {
                day_1=day_1-31
                month_1+=1
                if (month_1>12){
                    month_1=1
                }
            }
            4,6,9,11 ->if (day_1>30) {
                day_1 = day_1 - 30
                month_1 += 1
            }
            2->if(day_1>28){
                if (((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) && day_1>29)
                {
                    day_1=day_1-29
                }
                else{ day_1=day_1-28}
                month_1+=1
            }
        }
        return "$day_1/$month_1"
    }
    fun UpdateDLUser(str:String,id:String){
        val db=FirebaseFirestore.getInstance()
        var a:ArrayList<String> = ArrayList()
        db.collection("users")
            .document(id)
            .get()
            .addOnSuccessListener { document ->
                run {
                    if (document != null) {
                        a=document.get("Calender")as ArrayList<String>
                        a.add(str)
                        val userbooking= hashMapOf(
                            "Calender" to a
                        )
                        db.collection("users")
                            .document(id)
                            .update(userbooking as Map<String, Any>)
                            .addOnSuccessListener {
                            }
                            .addOnFailureListener {

                            }
                    }
                }
            }
    }
    fun SetButonDisable(){
        val arr = intArrayOf(10,1,4,3,2,5,6,8,9)
        val arr_btn= arrayOf(btn_hour_10am,btn_hour_1pm,btn_hour_4pm,btn_hour_3pm,btn_hour_2pm,btn_hour_5pm
        ,btn_hour_6pm,btn_hour_8am,btn_hour_9am)

        arr_btn.forEachIndexed() { idx,Button ->
            if(!CheckFreeinWeek(arr[idx])){
                Button.setTextColor(ContextCompat.getColor(this, R.color.primaryContainer))
            }
            else{
                Button.setTextColor(ContextCompat.getColor(this, R.color.black))
            }
        }
    }
}