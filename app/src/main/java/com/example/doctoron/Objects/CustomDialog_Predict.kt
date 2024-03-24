package com.example.doctoron.Objects
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.example.doctoron.R

class CustomDialog_Predict(context: Context,str:String ) : Dialog(context)  {
    val text_dialog:String=str
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_predict)

        val textView = findViewById<TextView>(R.id.textView)

        textView.text = text_dialog
    }
}