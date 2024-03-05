package com.example.doctoron.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.doctoron.R
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore

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
    var userID:String="ggzWTRVO2eZOvyW9X4dPGC0rv7k1"
    lateinit var week_2:ArrayList<Long>
    lateinit var week_3:ArrayList<Long>
    lateinit var week_4:ArrayList<Long>
    lateinit var week_5:ArrayList<Long>
    lateinit var week_6:ArrayList<Long>
    lateinit var week_7:ArrayList<Long>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_profile)
    ///-------------------------------------------------
        week_copy1= ArrayList<Long>().apply {
            repeat(11){
                add(0)
            }
        }
        week_2= ArrayList()
        week_4=ArrayList()
        week_6=ArrayList()
        week_3=ArrayList()
        week_5=ArrayList()
        week_7=ArrayList()
        //-------------- sau nhớ mở ra đàng hoàng
//        userID=intent.getStringExtra("User_ID")
        val db=FirebaseFirestore.getInstance()
        //----------------quay ve -------------------------------
        val btn_back=findViewById<ImageView>(R.id.back_btn)
        btn_back.setOnClickListener {
            finish()
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
                        // Xử lý thành công
                    }
                    .addOnFailureListener {
                        // Xử lý lỗi
                    }
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
                        Name.setText(document.get("name").toString())
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
                        Log.d("minhhy", "onCreate: "+week_2.toString())
                        Log.d("minhhy", "onCreate: "+week_3.toString())
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
        btn_Sat_week=findViewById(R.id.btn_Sat)

        btn_Mon_week.setOnClickListener {
            setBackgroundOriginal(2)
        }
        btn_Tue_week.setOnClickListener {
            setBackgroundOriginal(3)
        }
        btn_Wed_week.setOnClickListener {
            setBackgroundOriginal(4)
        }
        btn_Sat_week.setOnClickListener {
            setBackgroundOriginal(7)
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
            2 -> if (week_2 != null ) {
                setWeek(week_2)
                return true
            }

            3 -> if ( week_3.get(i).toInt() == 0) {
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
                10 -> setColor(btn_hour_10am,R.color.cyan_300)
                9->   setColor(btn_hour_9am,R.color.cyan_300)
                8->   setColor(btn_hour_8am,R.color.cyan_300)
                1-> setColor(btn_hour_1pm,R.color.cyan_300)
                2->       setColor(btn_hour_2pm,R.color.cyan_300)
                3->   setColor(btn_hour_3pm,R.color.cyan_300)
                4-> setColor(btn_hour_4pm,R.color.cyan_300)
                5->       setColor(btn_hour_5pm,R.color.cyan_300)
                6->  setColor(btn_hour_6pm,R.color.cyan_300)
            }
        }
        else{
            Toast.makeText(this,"Bác sĩ đã bận vào giờ này",Toast.LENGTH_SHORT).show()
        }
    }
    fun setBackgroundOriginal(i:Int){
        week=i
        btn_Fri_week.setBackgroundColor(resources.getColor(R.color.primary))
        btn_Mon_week.setBackgroundColor(resources.getColor(R.color.primary))
        btn_Wed_week.setBackgroundColor(resources.getColor(R.color.primary))
        btn_Thu_week.setBackgroundColor(resources.getColor(R.color.primary))
        btn_Sat_week.setBackgroundColor(resources.getColor(R.color.primary))
        btn_Tue_week.setBackgroundColor(resources.getColor(R.color.primary))
        when (i){
            2 -> btn_Mon_week.setBackgroundColor(resources.getColor(R.color.cyan_300))
            3 -> btn_Tue_week.setBackgroundColor(resources.getColor(R.color.cyan_300))
            4 -> btn_Wed_week.setBackgroundColor(resources.getColor(R.color.cyan_300))
            5 -> btn_Thu_week.setBackgroundColor(resources.getColor(R.color.cyan_300))
            6 -> btn_Fri_week.setBackgroundColor(resources.getColor(R.color.cyan_300))
            7 -> btn_Sat_week.setBackgroundColor(resources.getColor(R.color.cyan_300))
        }
    }
}