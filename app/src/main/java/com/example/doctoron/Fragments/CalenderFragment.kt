package com.example.doctoron.Fragments

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.doctoron.Adapters.Calendar_Note
import com.example.doctoron.Adapters.DoctorList
import com.example.doctoron.Objects.Calendar_Time
import com.example.doctoron.R
import com.google.firebase.firestore.FirebaseFirestore
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import java.util.Calendar
import kotlin.math.log

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

        calendar=view.findViewById<MaterialCalendarView>(R.id.calendarView)
        //-------------------------------------------------------------------
        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_notification)
        recyclerView.layoutManager = LinearLayoutManager(activity,LinearLayoutManager.VERTICAL, false)
        recyclerView.setHasFixedSize(true)

        var arr_calendar:ArrayList<String> = ArrayList()
        var arr_calendar_copy:ArrayList<String> = ArrayList()
        var adapter = Calendar_Note(arr_calendar_copy)
        recyclerView.adapter=adapter
        //----------------------------------------------------------------
        val db=FirebaseFirestore.getInstance()
        db.collection("users").document(userId)
            .get().addOnSuccessListener { document ->
                run {
                    if (document != null)
                    {
                        arr_calendar=document.get("Calender") as ArrayList<String>
                        for (i in arr_calendar){
                            val t=Calendar_Time(i)
                            if(t.checkDate2()){
                                Highlightdate(t.getDay(),t.getMonth())
                                arr_calendar_copy.add(i)
                            }
                        }
                        if (arr_calendar_copy.size!=arr_calendar.size)
                        {
                            UpdateCalendar(arr_calendar_copy,userId)
                            arr_calendar.clear()
                            arr_calendar.addAll(arr_calendar_copy)
                        }
                    }
                }
            }


        calendar.setOnDateChangedListener { widget, date, selected ->
            val selectedDate = "${date.day}/${date.month + 1}"
            arr_calendar_copy.clear()
            for(i in arr_calendar)
            {
                val t=Calendar_Time(i)
                if(t.checkDate1(selectedDate)){
                    arr_calendar_copy.add(i)
                }
            }
            adapter.notifyDataSetChanged()
        }
        return view
    }
    fun UpdateCalendar(arr:ArrayList<String>,id:String){
        val db=FirebaseFirestore.getInstance()
        val hashMap= hashMapOf(
            "Calender" to arr
        )
        db.collection("users").document(id).
        update(hashMap as Map<String, Any>)
    }
    fun Highlightdate(day:Int,month:Int){
        val specialDays = listOf(
            CalendarDay.from(2024, month-1, day)
        )

//        calendar.addDecorator(object : DayViewDecorator {
//            override fun shouldDecorate(day: CalendarDay?): Boolean {
//                return specialDays.contains(day)
//            }
//
//            override fun decorate(view: DayViewFacade?) {
//                view?.setBackgroundDrawable(ColorDrawable(Color.GREEN))
//            }
//        })
        //----------------------------------------------------------------------------------------
        calendar.addDecorator(object : DayViewDecorator {
            override fun shouldDecorate(day: CalendarDay?): Boolean {
                return specialDays.contains(day)
            }

            override fun decorate(view: DayViewFacade?) {
                // Tạo một ImageView để hiển thị hình tròn
                val imageView = ImageView(context)
                imageView.setImageResource(R.drawable.circle_shape) // Đặt tệp drawable hình tròn

                // Thiết lập kích thước và vị trí cho ImageView
                imageView.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                )
                imageView.scaleType = ImageView.ScaleType.CENTER_INSIDE

                // Đặt ImageView làm nền cho ngày
//                view?.setBackgroundDrawable(ShapeDrawable(OvalShape()))
                view?.setSelectionDrawable(imageView.drawable)
//                view?.setBackgroundDrawable(R.drawable.circle_shape)
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