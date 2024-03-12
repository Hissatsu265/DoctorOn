package com.example.doctoron.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.doctoron.Interface.OnItemClickListener
import com.example.doctoron.Objects.Calendar_Time
import com.example.doctoron.R

class Calendar_Note(private var arr_note : ArrayList<String>)
    : RecyclerView.Adapter<Calendar_Note.MyViewHolder>(){
    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var tv_Note: TextView =itemView.findViewById(R.id.tv_note)
        //-------------------------------------------------------------------------
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Calendar_Note.MyViewHolder {
        val itemView=
            LayoutInflater.from(parent.context).inflate(R.layout.item_calendar_note,parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: Calendar_Note.MyViewHolder, position: Int) {
        val currentitem: Calendar_Time =Calendar_Time(arr_note[position])
        holder.tv_Note.setText(currentitem.getNote())
    }

    override fun getItemCount(): Int {
        return  arr_note.size
    }
}