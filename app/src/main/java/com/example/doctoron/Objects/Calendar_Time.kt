package com.example.doctoron.Objects

import java.util.Calendar

class Calendar_Time(var str:String="") {

    var calendardate:String=""
    var calendarhour:String=""
    var calendarhuman:String=""
    var day:String=""
    var month:String=""
    init {
        str=str
        val numbersArray: List<String> = str.split(";")
        calendardate=numbersArray.get(0)
        calendarhour=numbersArray.get(1)
        calendarhuman=numbersArray.get(2)
        val numbersArray1: List<String> = calendardate.toString().split("/")
        day = numbersArray1.get(0)
        month = numbersArray1.get(1)
    }
    fun getNote():String{
        return "Bạn có lịch hẹn với $calendarhuman vào lúc $calendarhour giờ"
    }
    // check 2 ngày có giống nhau không
    fun checkDate1(str:String):Boolean{
        val numbersArray1: List<String> = str.split("/")
        val day1:String = numbersArray1.get(0)
        val mon1:String = numbersArray1.get(1)

        if (day1==day && mon1==month){
            return true
        }
        else
            return false
    }
    // check ngày hẹn so với ngày hiện tại có trễ không
    fun checkDate2():Boolean{
        val calendar = Calendar.getInstance()
        val day1 = calendar.get(Calendar.DAY_OF_MONTH)
        val month1 = calendar.get(Calendar.MONTH) + 1
        if (month.toInt()<month1 ||(month.toInt()==month1 && day1>day.toInt()))
            return false
        else
            return true
    }
    fun getDay():Int{
        return day.toInt()
    }
    fun getMonth():Int{
        return month.toInt()
    }

}