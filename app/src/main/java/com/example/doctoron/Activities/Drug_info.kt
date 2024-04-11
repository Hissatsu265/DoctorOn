package com.example.doctoron.Activities

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.doctoron.Adapters.Drug_Adapter
import com.example.doctoron.Objects.Drug
import com.example.doctoron.R
import com.google.firebase.firestore.FirebaseFirestore

class Drug_info : AppCompatActivity() {
    lateinit var id_drug:String
    lateinit var tv_name:TextView
    lateinit var tv_dactri:TextView
    lateinit var tv_tp:TextView
    lateinit var tv_tdphu:TextView
    lateinit var tv_price:TextView
    lateinit var tv_use:TextView
    lateinit var tv_ccd:TextView
    lateinit var iv_anh:ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContentView(R.layout.activity_drug_info)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
        val btn_back=findViewById<ImageButton>(R.id.back_btn_1)
        btn_back.setOnClickListener {
            finish()
        }
        Initvalue()
//        ----------------------------------------------------------------------
//        id_drug=intent.getStringExtra("ID_drug").toString()
//        val db1= FirebaseFirestore.getInstance()
//        db1.collection("Drug")
//            .document(id_drug)
//            .get().addOnSuccessListener {document-> run{
//                if(document!=null){
//                    tv_name.setText(document.get("name").toString())
//                    tv_ccd.setText(document.get("chongchidinh").toString())
//                    tv_use.setText(document.get("use").toString())
//                    tv_price.setText(document.get("price").toString())
//                    tv_tp.setText(document.get("tp").toString())
//                    tv_dactri.setText(document.get("dactri").toString())
//                    tv_tdphu.setText(document.get("tdphu").toString())
//                    val url=document.get("imageUrl").toString()
//                    if (!url.isNullOrEmpty()) {
//                        Glide.with(this)
//                            .load(url)
//                            .centerCrop()
//                            .thumbnail(0.5f)
//                            .into(iv_anh)
//                    }
//                }
//            }
//            }
//        -------------------------------------------------------------------------
        try{
            val bundle: Bundle? = intent.getExtras()
            if (bundle != null) {
                val drug_f: Drug? = bundle.getSerializable("drugtt") as? Drug
                if(drug_f!=null){
                    tv_name.setText(drug_f.getName())
                    tv_ccd.setText(drug_f.getChongchi())
                    tv_use.setText(drug_f.getUse())
                    tv_price.setText(drug_f.getPrice())
                    tv_tp.setText(drug_f.getTP())
                    tv_dactri.setText(drug_f.getDactri())
                    tv_tdphu.setText(drug_f.getTdphu())
                    val url=drug_f.getUrl()
                    if (!url.isNullOrEmpty()) {
                        Glide.with(this)
                            .load(url)
                            .centerCrop()
                            .thumbnail(0.5f)
                            .into(iv_anh)
                    }
                }
            }
        }
        catch (e:Exception){
            Log.d("loiroibay", "onCreate: "+e.message.toString())
        }
    }
    fun Initvalue(){
        iv_anh=findViewById(R.id.image_drug)
        tv_tp=findViewById(R.id.tp_drug)
        tv_name=findViewById(R.id.name_drug)
        tv_tdphu=findViewById(R.id.tdphu_drug)
        tv_price=findViewById(R.id.price_drug)
        tv_use=findViewById(R.id.use_drug)
        tv_dactri=findViewById(R.id.dactri_drug)
        tv_ccd=findViewById(R.id.chongchidinh_drug)
    }
}