package com.example.doctoron.Adapters

import android.content.ContentProvider
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.doctoron.Interface.OnItemClickListener
import com.example.doctoron.Objects.Doctor
import com.example.doctoron.R
import com.google.firebase.firestore.FirebaseFirestore

class DoctorList ( private val doctor:ArrayList<Doctor>, private val listener: OnItemClickListener)
:RecyclerView.Adapter<DoctorList.MyViewHolder_Lisdoctor>()  {
//    private var filteredDoctorList: ArrayList<Doctor> = ArrayList(doctor)
    class MyViewHolder_Lisdoctor(itemView: View, private val listener: OnItemClickListener): RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var tv_Name: TextView =itemView.findViewById(R.id.tv_name)
        var tv_Star: TextView =itemView.findViewById(R.id.tv_star)
        var tv_CN: TextView =itemView.findViewById(R.id.tv_CN)
        var tv_BV: TextView =itemView.findViewById(R.id.tv_bv)
        var iv_Avatar: ImageView =itemView.findViewById(R.id.iv_avatar)
        //-------------------------------------------------------------------------
        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            listener.onItemClick(adapterPosition)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder_Lisdoctor {
        val itemView=
            LayoutInflater.from(parent.context).inflate(R.layout.item_listdoctor,parent,false)
        return MyViewHolder_Lisdoctor(itemView,listener)
    }

    override fun onBindViewHolder(holder: MyViewHolder_Lisdoctor, position: Int) {
        val currentitem=doctor[position]

        holder.tv_Name.setText(currentitem.getName())
        holder.tv_Star.setText(currentitem.getStar().toString())
        holder.tv_CN.setText(currentitem.getCN())
        holder.tv_BV.setText(currentitem.getHospital())
        //----------------------------------------------------------------
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
                            .thumbnail(0.5f)
                            .into(holder.iv_Avatar)
                    }
                }
            }
        }
//        }

    }

    override fun getItemCount(): Int {
        return doctor.size
    }
}