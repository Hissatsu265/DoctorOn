package com.example.doctoron.Activities


import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.example.doctoron.Objects.SessionManager
import com.example.doctoron.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class Lognin : AppCompatActivity() {
    lateinit var btnlogin: AppCompatButton
    lateinit var txt_forgotpass: TextView
    lateinit var txt_nothaveacc: TextView
    lateinit var edt_mail: EditText
    lateinit var edt_pass: EditText
    lateinit var cb_showpass: CheckBox
    lateinit var sessionManager:SessionManager

    private lateinit var progressDialog: ProgressDialog
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lognin)
        //-----------------lay collection------------------------

        //----------------init------------------
        btnlogin=findViewById<AppCompatButton>(R.id.btnSignIn)
        txt_nothaveacc=findViewById<TextView>(R.id.txtConfirmation)
        txt_forgotpass=findViewById<TextView>(R.id.txtForgotPassword)

        edt_mail=findViewById(R.id.etEmail)
        edt_pass=findViewById(R.id.etPassword)
        firebaseAuth = FirebaseAuth.getInstance()

        cb_showpass=findViewById<CheckBox>(R.id.showpass)
        edt_pass.inputType= InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        //------------------------------Luu dang nhap-------------------------------------------
        sessionManager = SessionManager(this)
        if (sessionManager.getSavedPassword()!=null)
        {
            edt_pass.setText(sessionManager.getSavedPassword())
            edt_mail.setText(sessionManager.getSavedUsername())
        }
        //--------------------------------------------------------------------------
        txt_nothaveacc.setOnClickListener {
            val intent= Intent (applicationContext,Signup::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left)
        }
        btnlogin.setOnClickListener {
            SignIN()
        }
        // ẩn hiện mật khẩu
        cb_showpass.setOnCheckedChangeListener{ buttonView, isChecked ->
            if (isChecked) {
                edt_pass.inputType=  InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                edt_pass.inputType= InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
        }
        // quên mật khẩu
        txt_forgotpass.setOnClickListener {
            ForgotPass()
        }
    }
    fun SignIN(){

        val gmail: String =  edt_mail.text.toString()
        val pass: String = edt_pass.text.toString()

        if (gmail==""||pass==""){
            Toast.makeText(applicationContext, "Vui lòng nhập email và mật khẩu", Toast.LENGTH_SHORT).show()
        }
        else{
            progressDialog = ProgressDialog(this)
            progressDialog.setCancelable(false)
            progressDialog.setMessage("Đang đăng nhập...")
            progressDialog.show()
            // đăng nhập
            firebaseAuth.signInWithEmailAndPassword(gmail, pass)
                .addOnCompleteListener { task ->
                    progressDialog.dismiss()
                    if (task.isSuccessful) {
                        val firebaseUser = firebaseAuth.currentUser
                        try{
                            if (firebaseUser != null) {
                                if(firebaseUser.isEmailVerified) {
                                    Toast.makeText(applicationContext, "Đăng nhập thành công", Toast.LENGTH_SHORT).show()
                                    //Luu dăng nhap
                                    sessionManager.saveLoginDetails(gmail, pass)
                                    // Cập nhap mk đề phòng đổi mk, quên mk
                                    val user = FirebaseAuth.getInstance().currentUser
                                    val userId = user?.uid
                                    Log.d("kkk", userId.toString())
                                    Capnhapmatkhau(gmail,pass,userId.toString())
                                    // chuyển hướng đến app
                                    val intent = Intent(applicationContext, MainActivity::class.java)
                                    intent.putExtra("user_ID",userId.toString())

                                    startActivity(intent)
                                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left)
                                }else{
                                    Toast.makeText(applicationContext, "Bạn chưa xác thực tài khoản hãy check lại mail", Toast.LENGTH_SHORT).show()
                                    firebaseUser.sendEmailVerification()
                                }
                            }
                        } catch (e: Exception)
                        {
                            Toast.makeText(applicationContext, e.message.toString(), Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        edt_pass.setText("")
                        Toast.makeText(applicationContext, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
    fun ForgotPass(){
        val builder: AlertDialog.Builder = AlertDialog.Builder(this@Lognin)
        builder.setTitle("Đặt lại mật khẩu")

        // Inflate layout
        val view: View =
            LayoutInflater.from(applicationContext).inflate(R.layout.dialog_forgotpass, null)
        builder.setView(view)

        val Emailresetpass: EditText = view.findViewById(R.id.etEmail)

        builder.setNegativeButton("Reset",
            DialogInterface.OnClickListener { dialog, which ->
                FirebaseAuth.getInstance().sendPasswordResetEmail(Emailresetpass.text.toString())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Email đã được gửi đi, thông báo cho người dùng.
                            Toast.makeText(
                                applicationContext,
                                "Email đã được gửi để đặt lại mật khẩu",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            // Có lỗi xảy ra, thông báo cho người dùng.
                            Log.e("ResetPassword", "Failed")
                        }
                    }
            })

        builder.create().show()
    }
    fun Capnhapmatkhau(gmail:String,pass:String,id:String){
        val db = FirebaseFirestore.getInstance()
        val data = hashMapOf(
            "gmail" to gmail,
            "pass" to pass
        )
        if (id != null) {
            db.collection("users").document(id)
                .update(data as Map<String, Any>)
                .addOnSuccessListener {
                }
                .addOnFailureListener { e ->
                }
        }
    }

}