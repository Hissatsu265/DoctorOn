package com.example.doctoron.Adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.doctoron.Interface.OnItemClickListener
import com.example.doctoron.Objects.Doctor_userchat
import com.example.doctoron.R
import com.squareup.picasso.Picasso

class UserchatDoctor(private val arrUserchatdoctor:ArrayList<Doctor_userchat>, private val listener: OnItemClickListener)
: RecyclerView.Adapter<UserchatDoctor.MyViewHolder>(){
    class MyViewHolder(itemView: View, private val listener: OnItemClickListener): RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
//        var iv_Avatar=itemView.findViewById<ImageView>(R.id.imageViewUser)
        var iv_Avatar=itemView.findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.imageViewUser)
        var tv_name=itemView.findViewById<TextView>(R.id.userName)
        //-------------------------------------------------------------------------
        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            listener.onItemClick(adapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView=
            LayoutInflater.from(parent.context).inflate(R.layout.item_userchat,parent,false)
        return UserchatDoctor.MyViewHolder(itemView, listener)
    }

    override fun getItemCount(): Int {
        return arrUserchatdoctor.size
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentitem=arrUserchatdoctor[position]
        holder.tv_name.setText(currentitem.getName())
        currentitem.getUrlavatar { url->
            Glide.with(holder.itemView.context)
                .load(url)
                .centerCrop()
                .thumbnail(0.5f)
                .into(holder.iv_Avatar)
        }
    }
}