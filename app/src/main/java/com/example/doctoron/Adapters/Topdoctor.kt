package com.example.doctoron.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.doctoron.Interface.OnItemClickListener
import com.example.doctoron.Objects.Doctor
import com.example.doctoron.R
import com.google.firebase.firestore.FirebaseFirestore

class Topdoctor(private val doctor:ArrayList<Doctor>,private val listener: OnItemClickListener)
    :RecyclerView.Adapter<Topdoctor.MyViewHolder>() {
        class MyViewHolder(itemView: View,private val listener: OnItemClickListener):RecyclerView.ViewHolder(itemView),View.OnClickListener {
            var tv_Name:TextView=itemView.findViewById(R.id.textView_Name)
            var tv_Star:TextView=itemView.findViewById(R.id.textView_Ranking)
            var tv_CN:TextView=itemView.findViewById(R.id.textView_CN)
            var tv_Bv:TextView=itemView.findViewById(R.id.textView_Bv)
            var iv_Avatar:ImageView=itemView.findViewById(R.id.imageView_avatardoctor)
            //-------------------------------------------------------------------------
            init {
                itemView.setOnClickListener(this)
            }
            override fun onClick(v: View?) {
                listener.onItemClick(adapterPosition)
            }
        }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView=LayoutInflater.from(parent.context).inflate(R.layout.item_topdoctor,parent,false)
        return MyViewHolder(itemView,listener)
    }

    override fun getItemCount(): Int {
        return doctor.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentitem=doctor[position]
        holder.tv_Name.setText(currentitem.getName())
        holder.tv_Star.setText(currentitem.getStar().toString())
        holder.tv_CN.setText(currentitem.getCN())
        holder.tv_Bv.setText(currentitem.getHospital())
        //----------------------------------------------------------------------------
        val firestore = FirebaseFirestore.getInstance()

        if (currentitem.getID()!=""){
            val userRef = firestore.collection("users").document(currentitem.getID())
            userRef.addSnapshotListener { snapshot, _ ->
                if (snapshot != null && snapshot.exists()) {
                    val avatarUrl = snapshot.getString("avatarUrl")
                    if (!avatarUrl.isNullOrEmpty()) {
                        Glide.with(holder.itemView.context)
                            .load(avatarUrl)
                            .centerCrop()
                            .apply(RequestOptions.circleCropTransform())
                            .thumbnail(0.5f)
                            .into(holder.iv_Avatar)
                    }
                }
            }
        }
    }
}