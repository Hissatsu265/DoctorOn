package com.example.doctoron.Fragments

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import com.example.doctoron.R
import com.google.firebase.firestore.FirebaseFirestore
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import java.util.Calendar

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class CalenderFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var userId:String=""
    lateinit var  calendar: MaterialCalendarView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userId = arguments?.getString("user_ID").toString()

        val view=inflater.inflate(R.layout.fragment_calender, container, false)
        var cv_calendar_note=view.findViewById<androidx.cardview.widget.CardView>(R.id.cv_note)
        cv_calendar_note.visibility=View.GONE
        calendar=view.findViewById<MaterialCalendarView>(R.id.calendarView)
        var tv_note=view.findViewById<TextView>(R.id.note)

        var calendardate:String=""
        var calendarhour:String=""
        var calendardoctor:String=""

        val db=FirebaseFirestore.getInstance()
        db.collection("users").document(userId)
            .get().addOnSuccessListener { document ->
                run {
                    if (document != null)
                    {
                        val numbersArray: List<String> = document.get("Calender").toString().split(";")
                        calendardate=numbersArray.get(0)
                        calendarhour=numbersArray.get(1)
                        calendardoctor=numbersArray.get(2)
                        val numbersArray1: List<String> = calendardate.toString().split("/")
                        val num1:String=numbersArray1.get(0)
                        val num2:String=numbersArray1.get(1)
                        if(checkDate(num1.toInt(),num2.toInt())){
                            Highlightdate(num1.toInt(),num2.toInt())
                        }
                        else{
                            calendardate=""
                        }
                    }
                }
            }


        calendar.setOnDateChangedListener { widget, date, selected ->
            val selectedDate = "${date.day}/${date.month + 1}"
            Log.d("tag", "onCreateView: "+selectedDate)
            if (calendardate == selectedDate) {
                cv_calendar_note.visibility=View.VISIBLE
                tv_note.setText("Bạn có hẹn với bác sĩ ${calendardoctor} vào lúc ${calendarhour} giờ")
            } else {
                cv_calendar_note.visibility=View.GONE
            }
        }
        return view
    }
    fun checkDate(day:Int,month:Int):Boolean{
        val calendar = Calendar.getInstance()
        val day1 = calendar.get(Calendar.DAY_OF_MONTH)
        val month1 = calendar.get(Calendar.MONTH) + 1
        if (month<month1 ||(month==month1 && day1>day))
            return false
        else
            return true
    }
    fun Highlightdate(day:Int,month:Int){
        val specialDays = listOf(
            CalendarDay.from(2024, month-1, day)

        )

        calendar.addDecorator(object : DayViewDecorator {
            override fun shouldDecorate(day: CalendarDay?): Boolean {
                return specialDays.contains(day)
            }

            override fun decorate(view: DayViewFacade?) {
                view?.setBackgroundDrawable(ColorDrawable(Color.GREEN))
            }
        })
    }
    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CalenderFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}