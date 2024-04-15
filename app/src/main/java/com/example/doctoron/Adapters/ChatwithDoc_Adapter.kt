package com.example.doctoron.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.doctoron.Interface.OnItemClickListener
import com.example.doctoron.Objects.Chat
import com.example.doctoron.R

class ChatwithDoc_Adapter(private val data:ArrayList<String>,private val time:ArrayList<String>,
                          private val send:ArrayList<String>,private val isdoctor:String)
    : RecyclerView.Adapter<ChatwithDoc_Adapter.MyViewHolder>() {
    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    {
        var tv_Mess: TextView =itemView.findViewById(R.id.show_message)
        var tv_Time: TextView =itemView.findViewById(R.id.timeView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_LEFT -> {
                val view = inflater.inflate(R.layout.itemchatleft, parent, false)
                MyViewHolder(view)
            }
            VIEW_TYPE_RIGHT -> {
                val view = inflater.inflate(R.layout.itemchatright, parent, false)
                MyViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
    override fun getItemViewType(position: Int): Int {
        val sender = send[position]
        return if ((sender=="1"&& isdoctor=="0")||
            (sender=="0"&& isdoctor=="1")) VIEW_TYPE_LEFT else VIEW_TYPE_RIGHT
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentitem_data=data[position]
        val currentitem_time=time[position]
        holder.tv_Mess.setText(currentitem_data)
        holder.tv_Time.setText(currentitem_time)
    }
    //-------------------------------------------------------------------------
    private companion object {
        const val VIEW_TYPE_LEFT = 1
        const val VIEW_TYPE_RIGHT = 2
    }
}