package com.example.doctoron.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.Toast
import android.widget.Toolbar
import android.window.OnBackInvokedDispatcher
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.doctoron.Fragments.AccountFragment
import com.example.doctoron.Fragments.CalenderFragment
import com.example.doctoron.Fragments.DashboardFragment
import com.example.doctoron.Fragments.MessagesFragment
import com.example.doctoron.R
import com.example.doctoron.databinding.ActivityMainBinding
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var fragmentManager: FragmentManager
    private lateinit var drawerLayout: androidx.drawerlayout.widget.DrawerLayout
    var user_ID:String="gTt4CW1WkAVfIWxVcTJEyQ1Akwi2"
    override fun onCreate(savedInstanceState: Bundle?) {
        try {

            //---------------truyen user id ------------------------
            user_ID=intent.getStringExtra("user_ID").toString()
            //----------------------------------------------------
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main)
            //------------------- tool bar -----------------------------------------------
            val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
            setSupportActionBar(toolbar)
            //----------------------------Notification --------------------------------------
            val btn_notification=findViewById<ImageButton>(R.id.btn_notification)
            btn_notification.setOnClickListener{
                val intent=Intent(this, Notification::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left)
            }
            //---------------------------------------------------------------------------
            // Tìm các thành phần còn lại bằng findViewById nếu cần
            val drawerLayout = findViewById<androidx.drawerlayout.widget.DrawerLayout>(R.id.drawer_layout)
            val bottomNavigationView = findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottom_navigation)
//            val fab = findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.fab)
            val navigationDrawer=findViewById<com.google.android.material.navigation.NavigationView>(R.id.navigation_drawer)

            //-------thiết lập-----------------------------------------------------------
            val toggle=ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.Nav_open,R.string.Nav_close)
            drawerLayout.addDrawerListener(toggle)
            toggle.syncState()
            navigationDrawer.setNavigationItemSelectedListener(this)

            bottomNavigationView.background=null
            bottomNavigationView.setOnItemSelectedListener { item->
                when(item.itemId){
                    R.id.Home->openFragment(DashboardFragment())
                    R.id.Message->openFragment(MessagesFragment())
                    R.id.Account->openFragment(AccountFragment())
                    R.id.Calender->openFragment(CalenderFragment())
                }
                true
            }
            fragmentManager=supportFragmentManager
            openFragment(DashboardFragment())
//            fab.setOnClickListener{
//                Toast.makeText(this,"hus",Toast.LENGTH_SHORT).show()
//            }
        }
        catch (e:Exception){
            Log.d("huhuu", e.message.toString())
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.nav_prime->openFragment(AccountFragment())
            R.id.nav_prime1->{
                val intent = Intent(this, Setting_account::class.java)
                startActivity(intent)
            }
            R.id.nav_prime2->{
                val intent = Intent(this, Lognin::class.java)
                startActivity(intent)
                this?.finish()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
    override fun onBackPressed(){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START)
        }else{
            super.onBackPressedDispatcher.onBackPressed()
            super.onBackPressed()
        }
    }
    private fun openFragment(fragment: Fragment){
        val bundle = Bundle()
        bundle.putString("user_ID", user_ID)
        fragment.arguments=bundle
        val fragmentTransaction:FragmentTransaction= fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container,fragment)
        fragmentTransaction.commit()
    }
}