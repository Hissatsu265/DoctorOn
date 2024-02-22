package com.example.doctoron.Objects

class Doctor(private var name:String="",private var pass:String="",private var gmail:String="",
             private var age:Int=0,private var DoB:String="",private var sex:Int=0,
             private var star:Int=0,private var CN:String="") {
    init{
        name=name
        age=age
        pass=pass
        gmail=gmail
        DoB=DoB
        sex=sex
        star=star
        CN=CN
    }
    fun getName():String{
        return name
    }
    fun getCN():String{
        return CN
    }
    fun getStar():Int{
        return star
    }
}