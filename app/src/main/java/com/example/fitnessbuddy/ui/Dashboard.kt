package com.example.fitnessbuddy.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.edit
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.fitnessbuddy.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class Dashboard : AppCompatActivity() , NavigationView.OnNavigationItemSelectedListener{
    lateinit var toolbar: Toolbar
    lateinit var drawerLayout: DrawerLayout
    private lateinit var botNav: BottomNavigationView
    private lateinit var testTv: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        toolbar =findViewById(R.id.title)
        setSupportActionBar(toolbar)
//        drawerLayout=findViewById(R.id.drawerLayout)
//
//        val toggle = ActionBarDrawerToggle(
//            this, drawerLayout, toolbar, 0, 0
//        )
//        drawerLayout.addDrawerListener(toggle)
//        toggle.syncState()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean{
        when (item.itemId) {
            R.id.nav_home->{
                startActivity(
                    Intent(this@Dashboard,
                        HomeActivity::class.java)
                )
            }
            R.id.nav_Profile->{
                startActivity(
                    Intent(this,
                        MainActivity::class.java)
                )
            }




            R.id.nav_logout->{
                val prefs: SharedPreferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE)
                prefs.edit(commit = true) {
                    clear()
                }
                startActivity(
                    Intent(this@Dashboard,
                        Login::class.java)
                )
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
