package com.example.doctoron.Fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.doctoron.R
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate
import java.time.format.DateTimeFormatter
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
        var calendar=view.findViewById<CalendarView>(R.id.calendarView)
        var tv_note=view.findViewById<TextView>(R.id.note)

//        var Calendar_user:String=""
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
//                        Log.i("hiiii", "onCreateView: "+Calendar_user)
                    }
                }
            }

//        Log.i("hiiii", "onCreateView: "+Calendar_user)

//        try {
//            Log.i("hiiii", "onCreateView: "+calendardate.toString())
//            Log.i("hiiii", "onCreateView: "+calendardoctor.toString())
//            Log.i("hiiii", "onCreateView: "+calendarhour.toString())
//        }
//        catch (e:Exception){
//            Log.d("hiiiiii", "onCreateView: "+e.message.toString())
//        }
        calendar.setOnDateChangeListener { _, year, month, dayOfMonth ->
            try{
                val selectedDate = "$dayOfMonth/${month + 1}"
                if (calendardate==selectedDate){
                    tv_note.setText("Bạn có hẹn với bác sĩ ${calendardoctor} vào lúc ${calendarhour} giờ")
                }else{
                    tv_note.setText("")
                }
            }
            catch (e:Exception){
                Log.d("hiiiiii", "onCreateView: "+e.message.toString())
            }
        }
        return view
    }
    companion object {

        // TODO: Rename and change types and number of parameters
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