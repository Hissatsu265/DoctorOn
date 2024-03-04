package com.example.doctoron.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import com.example.doctoron.R

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
    var week:Int=0
    var hour:Int=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_profile)
        //----------------quay ve -------------------------------
        val btn_back=findViewById<ImageView>(R.id.back_btn)
        btn_back.setOnClickListener {
            finish()
        }
        //-----------------------set profile-------------------------------



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
    fun setBacgroundOriginalforHour(i:Int){

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