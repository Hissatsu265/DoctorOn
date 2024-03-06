package com.example.doctoron.Activities


import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.text.InputType
import android.util.Log

import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.example.doctoron.Objects.Doctor
import com.example.doctoron.Objects.User
import com.example.doctoron.R
import com.google.firebase.auth.FirebaseAuth
import kotlin.math.log


class Signup : AppCompatActivity() {

    lateinit var btnsign: AppCompatButton
    lateinit var edt_pass: EditText
    lateinit var edt_pass_again: EditText
    lateinit var edt_email: EditText
    lateinit var edt_name: EditText
    lateinit var cb_showpass: CheckBox
    lateinit var tv_confirm: TextView

    var Collection:String=""

    private lateinit var progressDialog: ProgressDialog
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        //--------------lay collection------------------------------
        Collection=intent.getStringExtra("Collection").toString()
        //----------------------init---------------------------------
        firebaseAuth = FirebaseAuth.getInstance()

        btnsign = findViewById<AppCompatButton>(R.id.btnSignUp)
        edt_pass= findViewById<EditText>(R.id.etPassword)
        edt_pass_again= findViewById<EditText>(R.id.etPassword1)
        edt_name= findViewById<EditText>(R.id.etFullName)
        edt_email= findViewById<EditText>(R.id.etEmail)
        cb_showpass=findViewById<CheckBox>(R.id.showpass)
        tv_confirm=findViewById(R.id.txtConfirmation)
        // ẩn mật khẩu ban đầu
        edt_pass.inputType= InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        edt_pass_again.inputType= InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        //---------------------------Handle Click-----------------------------
        // đã có tài khoản
        tv_confirm.setOnClickListener {
            val intent= Intent (applicationContext,Lognin::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left)
        }
        // chỉnh lại các thông tin khi lỗi
        edt_pass.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && edt_pass.text.toString().equals("Mật khẩu không đạt chuẩn")) {
                edt_pass.setText("")
                edt_pass.setTextColor(Color.parseColor("#9098b1"))
            }
        }
        edt_pass_again.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && (edt_pass_again.text.toString().equals("Mật khẩu không trùng khớp"))||edt_pass_again.text.toString().equals("")) {
                edt_pass_again.setText("")
                edt_pass_again.setTextColor(Color.parseColor("#9098b1"))
            }
        }
        edt_email.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && edt_email.text.toString().equals("Gmail không hợp lệ")) {
                edt_email.setText("")
                edt_email.setTextColor(Color.parseColor("#9098b1"))
            }
        }
       //------------------------------------------------------------------
        // nhấn nút đăng kí
        btnsign.setOnClickListener {
            HandsignCLick()
        }
        // ẩn hiện mật khẩu
        cb_showpass.setOnCheckedChangeListener{ buttonView, isChecked ->
            if (isChecked) {
                edt_pass.inputType=  InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                edt_pass_again.inputType=  InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                edt_pass.inputType= InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                edt_pass_again.inputType= InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
        }
        //-----------------------------------------------------------
    }
    fun isValidEmail(email: String): Boolean {
        val emailPattern = Regex("[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
        return email.matches(emailPattern)
    }
    fun isValidPassword(password: String): Boolean {
        val passwordPattern = Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+\$).{8,}\$")
        return password.matches(passwordPattern)
    }
    fun HandsignCLick(){
        val gmail:String =edt_email.text.toString()
        val pass :String = edt_pass.text.toString()
        val pass_again:String =edt_pass_again.text.toString()
        var kt: Boolean = true
        // kiểm tra mật khẩu
        if(!pass.equals(pass_again)){
            edt_pass_again.setText("Mật khẩu không trùng khớp")
            edt_pass_again.setTextColor(Color.RED)
            kt=false
        }
        // check mật khẩu
        if (!isValidPassword(pass)){
            edt_pass.setText("Mật khẩu không đạt chuẩn")
            edt_pass.setTextColor(Color.RED)
            edt_pass_again.setText("")
            kt=false
        }

        if(!isValidEmail(gmail)){
            edt_email.setText("Gmail không hợp lệ")
            edt_email.setTextColor(Color.RED)
            kt=false
        }
        if(edt_name.text.toString().equals("")){
            Toast.makeText(applicationContext,"Không được để trống thông tin",Toast.LENGTH_SHORT).show()
            kt=false
        }
        if(kt){
            progressDialog = ProgressDialog(this)
            progressDialog.setCancelable(false)
            progressDialog.setMessage("Loading...")
            progressDialog.show()
            // đăng kí tài khoản
            firebaseAuth.createUserWithEmailAndPassword(gmail, pass)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // gửi mail xác thực
                        val user = firebaseAuth.currentUser
                        user?.sendEmailVerification()
                            ?.addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    // lưu thông tin đăng kí
                                    SaveInfo(gmail,pass,edt_name.text.toString(),user?.uid.toString())
                                    //-----------------------------------------------------------------------------
                                    progressDialog.dismiss()
                                    Toast.makeText(applicationContext,"Đã gửi email xác thực",Toast.LENGTH_SHORT).show()
                                    val intent=Intent(applicationContext,Confirm_email::class.java)

                                    intent.putExtra("user_ID", user?.uid.toString())

                                    intent.putExtra("gmail",gmail)
                                    intent.putExtra("pass",pass)
                                    //----------------------
                                    startActivity(intent)
                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left)
                                } else {
                                    Toast.makeText(applicationContext,"Chưa thể gửi email xác thực",Toast.LENGTH_SHORT).show()
                                }
                            }
                    } else {
                        Toast.makeText(applicationContext,"Đăng kí thất bại",Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
    fun SaveInfo(gmail:String,pass:String,name:String,id:String){
        if (Collection=="users"){
            val new_user = User(name,pass,gmail)
            new_user.SendtoFirebase(id)
        }
        else{
            val new_doctor = Doctor(name,pass,gmail)
            new_doctor.SendtoFirebase(id)
        }
    }
    //------------------------------------------------------------------------------------
}