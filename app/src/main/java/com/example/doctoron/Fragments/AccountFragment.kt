package com.example.doctoron.Fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import com.example.doctoron.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.doctoron.Activities.Chinh_sua_thong_tin_ca_nhan
import com.example.doctoron.Activities.Chiso_bmi_hr_calo
import com.example.doctoron.Activities.Lognin
import com.example.doctoron.Activities.Predict_health
import com.example.doctoron.Activities.Setting_account

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AccountFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AccountFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    //---------------------sau dùng phương thức khác để truyển vào--------------------

    private var userId:String= "vtnB0tljUVnbTZLCqgPD"

    //------------------------------------------------------------------------------
    private val PICK_IMAGE_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userId = arguments?.getString("user_ID").toString()
        Log.d("id4", userId)
        val view= inflater.inflate(R.layout.fragment_account, container, false)
        //-------------------------------------------------------------------------------
        var ll_help:LinearLayout=view.findViewById(R.id.help_account)
        var ll_help1:LinearLayout=view.findViewById(R.id.help_account1)
        var ll_chiso:LinearLayout=view.findViewById(R.id.ll_chiso)

        var ll_changepass:LinearLayout=view.findViewById(R.id.changepass)
        var ll_chinhtt:LinearLayout=view.findViewById(R.id.chinhsuatt)
        var ll_logout:LinearLayout=view.findViewById(R.id.logout)
        var ll_predict: LinearLayout=view.findViewById(R.id.predict_health)
        var tv_name_user:TextView=view.findViewById(R.id.tv_name_account)
        var img_avatar:ImageView=view.findViewById(R.id.user_avatar)
        //-------------------------------------------------------------------------------
        img_avatar.setOnClickListener{
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }
        ll_predict.setOnClickListener{
            val intent = Intent(activity, Predict_health::class.java)
            startActivity(intent)
        }
        ll_help.setOnClickListener{
            val intent = Intent(activity, Setting_account::class.java)
            startActivity(intent)
        }
        ll_help1.setOnClickListener{
            val intent = Intent(activity, Setting_account::class.java)
            startActivity(intent)
        }
        ll_chiso.setOnClickListener {
            val intent = Intent(activity, Chiso_bmi_hr_calo::class.java)
            intent.putExtra("user_ID",userId)
            startActivity(intent)
        }
        ll_logout.setOnClickListener {
            val intent = Intent(activity, Lognin::class.java)
            startActivity(intent)
            activity?.finish()
        }
        ll_chinhtt.setOnClickListener {
            val intent = Intent(activity, Chinh_sua_thong_tin_ca_nhan::class.java)
            intent.putExtra("user_ID",userId)
            startActivity(intent)
        }
        ll_changepass.setOnClickListener{
            val db1=FirebaseFirestore.getInstance()
            try {

                db1.collection("users")
                    .document(userId)
                    .get()
                    .addOnSuccessListener { document ->
                        run {
                            if (document != null) {
                                val Emailresetpass=document.get("gmail").toString()
                                FirebaseAuth.getInstance().sendPasswordResetEmail(Emailresetpass)
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            // Email đã được gửi đi, thông báo cho người dùng.
                                            Toast.makeText(
                                                activity,
                                                "Email đã được gửi để đặt lại mật khẩu",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } else {
                                            Log.e("ResetPassword", "Failed")
                                        }
                                    }
                            }
                        }
                    }
            }
            catch (e:Exception){
                Log.e("loiiiii", e.message.toString() )
            }

        }
        //------------------------------Load avatar-------------------------------------

//        val userId:String= "vtnB0tljUVnbTZLCqgPD"
//        --------------------------------------------------------------
        val firestore = FirebaseFirestore.getInstance()
        val userRef = firestore.collection("users").document(userId!!)

        userRef.addSnapshotListener { snapshot, _ ->
            if (snapshot != null && snapshot.exists()) {
                val avatarUrl = snapshot.getString("avatarUrl")
                val name_user= snapshot.getString("name")
                tv_name_user.setText(name_user)
                if (!avatarUrl.isNullOrEmpty()) {
                    Glide.with(requireContext())
                        .load(avatarUrl)
                        .apply(RequestOptions.circleCropTransform())
                        .into(img_avatar)
                }
            }
        }
        //-------------------------------------------------------------------
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AccountFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AccountFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val selectedImageUri: Uri = data.data!!
            val path: String="avatars/"+userId+".jpg"
            val storageRef = FirebaseStorage.getInstance().reference.child(path)
            storageRef.putFile(selectedImageUri)
                .addOnSuccessListener { taskSnapshot ->
                    storageRef.downloadUrl.addOnSuccessListener { uri ->
                        val imageUrl = uri.toString()
//------------------------------để tạm sau này truyền vào sau-------------------------------------------------------------------------

//                    val user = FirebaseAuth.getInstance().currentUser
//                    val userId = user?.uid

//----------------------------------------------------------------------------------------------------------
                        val firestore = FirebaseFirestore.getInstance()
                        val userRef = firestore.collection("users").document(userId!!)
                        userRef.update("avatarUrl", imageUrl)
                            .addOnSuccessListener {
                                // Upload và lưu thành công
                            }
                            .addOnFailureListener { e ->
                                // Xử lý khi gặp lỗi
                            }
                }
                .addOnFailureListener { e ->

                }
        }
    }
}
}