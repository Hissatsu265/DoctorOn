package com.example.doctoron.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.doctoron.Interface.OnItemClickListener
import com.example.doctoron.Objects.Doctor
import com.example.doctoron.R

class History_predict_Adapter(private val rate:ArrayList<String>,private val time:ArrayList<String>,
                              private val type:ArrayList<String>): RecyclerView.Adapter<History_predict_Adapter.MyViewHolder>()
{
    class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)
    {
        var tv_time: TextView =itemView.findViewById(R.id.time)
        var tv_rate: TextView =itemView.findViewById(R.id.rate)
        var tv_title: TextView =itemView.findViewById(R.id.title)

        //-------------------------------------------------------------------------
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView=
            LayoutInflater.from(parent.context).inflate(R.layout.item_historypredict,parent,false)
        return History_predict_Adapter.MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return time.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val position1=time.size-1-position

        val rate1=rate[position1]
        var title1="Kết quả dự đoán cho bênh tim mạch"
        if(type[position1]=="1"){
            title1="Kết quả dự đoán cho bênh tiểu đường"
        }else if (type[position1]=="2"){
            title1="Kết quả dự đoán cho bênh viêm phổi"
        }
        holder.tv_rate.setText("Tỉ lệ mắc bệnh: $rate1%")
        holder.tv_time.setText(time[position1])
        holder.tv_title.setText(title1)
    }
}