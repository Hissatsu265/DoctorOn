package com.example.doctoron.Objects

import java.io.Serializable

class Drug(private var name: String = "", private var dactri:String="",private var chongchidinh:String="",
    private var tp:String="",private var price:String="",private var use:String="",
           private var tdphu:String="",private var id:String ="",private var url:String =""):
    Serializable {
    init {
        name=name
        dactri=dactri
        chongchidinh=chongchidinh
        tp=tp
        price=price
        id=id
        tdphu=tdphu
        use=use
        url=url
    }
    fun getUse():String{
        return use
    }
    fun getUrl():String{
        return url
    }
    fun setUrl(urlimage:String){
        url=urlimage
    }
    fun getTdphu():String{
        return tdphu
    }
    fun getID():String{
        return id
    }
    fun getTP():String{
        return tp
    }
    fun getPrice():String{
        return price
    }
    fun getName():String{
        return name
    }
    fun getDactri():String{
        return dactri
    }
    fun getChongchi():String{
        return chongchidinh
    }
}