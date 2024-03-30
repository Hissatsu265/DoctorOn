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
import com.example.doctoron.Interface.OnitemDrugClickListener
import com.example.doctoron.Objects.Drug
import com.example.doctoron.R
import com.google.firebase.firestore.FirebaseFirestore

class Drug_Adapter(private val drug:ArrayList<Drug>,
                   private val listener: OnitemDrugClickListener
)
    : RecyclerView.Adapter<Drug_Adapter.MyViewHolder>() {
    class MyViewHolder(itemView: View, private val listener: OnitemDrugClickListener):RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var tv_Name: TextView =itemView.findViewById(R.id.drug_name)
        var tv_DT: TextView =itemView.findViewById(R.id.drug_dactri)
        var iv_Avatar: ImageView =itemView.findViewById(R.id.drug_anh)
        //-------------------------------------------------------------------------
        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            listener.onItemDrugClick(adapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView=
            LayoutInflater.from(parent.context).inflate(R.layout.item_drug,parent,false)
        return MyViewHolder(itemView, listener)
    }

    override fun getItemCount(): Int {
        return drug.size
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentitem=drug[position]
        holder.tv_Name.setText(currentitem.getName())
        holder.tv_DT.setText(currentitem.getDactri())
        //-------------------------------------------------------
        val firestore = FirebaseFirestore.getInstance()

        if (currentitem.getID()!=""){
            val userRef = firestore.collection("Drug").document(currentitem.getID())
            userRef.addSnapshotListener { snapshot, _ ->
                if (snapshot != null && snapshot.exists()) {
                    val avatarUrl = snapshot.getString("imageUrl")
                    if (!avatarUrl.isNullOrEmpty()) {
                        Glide.with(holder.itemView.context)
                            .load(avatarUrl)
                            .thumbnail(0.5f)
                            .into(holder.iv_Avatar)
                    }
                }
            }
        }
    }
}