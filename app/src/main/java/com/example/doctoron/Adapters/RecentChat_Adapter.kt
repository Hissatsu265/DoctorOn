package com.example.doctoron.Adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.doctoron.Interface.OnItemClickListener
import com.example.doctoron.Interface.OnItemClickListener1
import com.example.doctoron.Objects.Doctor
import com.example.doctoron.Objects.RecentChat_Object
import com.example.doctoron.R
import com.google.firebase.firestore.FirebaseFirestore

class RecentChat_Adapter (private val recentchat:ArrayList<RecentChat_Object>,private val listener: OnItemClickListener1)
: RecyclerView.Adapter<RecentChat_Adapter.MyViewHolder>(){
    class MyViewHolder(itemView: View, private val listener: OnItemClickListener1): RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var iv_Avatar=itemView.findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.recentChatImageView)
        var tv_name=itemView.findViewById<TextView>(R.id.recentChatTextName)
        var tv_lastmess=itemView.findViewById<TextView>(R.id.recentChatTextLastMessage)
        var tv_time=itemView.findViewById<TextView>(R.id.recentChatTextTime)
        //-------------------------------------------------------------------------
        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            listener.onItemClick1(adapterPosition)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val itemView=
            LayoutInflater.from(parent.context).inflate(R.layout.item_recentchat,parent,false)
        return MyViewHolder(itemView, listener)
    }

    override fun onBindViewHolder(
        holder: RecentChat_Adapter.MyViewHolder,
        position: Int
    ) {
        try{
            val currentitem=recentchat[position]
            holder.tv_lastmess.setText(currentitem.getLastmess())
            holder.tv_time.setText(currentitem.getTime())
            currentitem.getUrlavatarandName { url, name ->
                Glide.with(holder.itemView.context)
                    .load(url)
                    .centerCrop()
                    .thumbnail(0.5f)
                    .into(holder.iv_Avatar)
                holder.tv_name.setText(name)
            }
        }
        catch (e:Exception){
            Log.d("TAGloiii", "onBindViewHolder: "+e.message.toString())
        }
    }
    override fun getItemCount(): Int {
        return recentchat.size
    }

}