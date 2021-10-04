package com.example.fitnessbuddy.ui

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.content.edit
import androidx.drawerlayout.widget.DrawerLayout
import com.example.fitnessbuddy.R
import com.example.fitnessbuddy.api.ServiceBuilder
import com.google.android.material.card.MaterialCardView
import com.google.android.material.navigation.NavigationView
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ImageListener
import java.util.*

class HomeActivity : AppCompatActivity() {
    private lateinit var userName: TextView
    private lateinit var username: TextView
    private lateinit var txtgood: TextView
    private lateinit var myBooking: MaterialCardView
    private lateinit var showTicket: MaterialCardView
    private lateinit var pastbooking: MaterialCardView
    private lateinit var navView: NavigationView
    public lateinit var toogle: ActionBarDrawerToggle
    private lateinit var mapView: ImageView

    private var sampleImage = intArrayOf(R.drawable.wearmask, R.drawable.distance, R.drawable.payment)
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        myBooking = findViewById(R.id.addTicket)
        drawerLayout = findViewById(R.id.drawerLayout)
        toolbar = findViewById(R.id.title)
        showTicket = findViewById(R.id.showTicket)
        pastbooking = findViewById(R.id.past)
        userName = findViewById(R.id.userName)
        txtgood = findViewById(R.id.txtgood)
        navView=findViewById(R.id.navView)
        username = navView.getHeaderView(0).findViewById(R.id.txt)
        userName.setText(ServiceBuilder.name)
        username.setText(ServiceBuilder.name)
        txtgood.setText(greetingMessage())
        showTicket.setOnClickListener {
            startActivity(
                Intent(
                    this@HomeActivity,
                    AddActivity::class.java
                )
            )
        }
        pastbooking.setOnClickListener {
            startActivity(
                Intent(
                    this@HomeActivity,
                    BMIActivity::class.java
                )
            )
        }

        myBooking.setOnClickListener {
            startActivity(
                Intent(
                    this@HomeActivity,
                    Settings::class.java
                )
            )
        }



        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, 0, 0
        )

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navView.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.nav_home->{
                    startActivity(
                        Intent(this@HomeActivity,
                            HomeActivity::class.java)
                    )
                }
                R.id.nav_Profile->{
                    startActivity(
                        Intent(this,
                            ViewProfile::class.java)
                    )
                }

                R.id.nav_logout->{
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Logout")
                    builder.setMessage("Are you sure you want to logout??")
                    builder.setIcon(android.R.drawable.ic_delete)
                    builder.setPositiveButton("Yes") { _, _ ->
                        val prefs: SharedPreferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE)
                        prefs.edit(commit = true) {
                            clear()
                        }
                        startActivity(
                            Intent(this@HomeActivity,
                                Login::class.java)
                        )
                        finish()
                    }
                    builder.setNegativeButton("No") { _, _ ->
                    }
                    val alertDialog: AlertDialog = builder.create()
                    alertDialog.setCancelable(false)
                    alertDialog.show()
                }
            }
            true
        }
        val carouselView = findViewById<CarouselView>(R.id.carouselView);
        carouselView.pageCount = sampleImage.size;
        carouselView.setImageListener(imageListener);
    }

    var imageListener: ImageListener =
        ImageListener { position, imageView ->
            imageView.setImageResource(sampleImage[position])
        }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toogle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }


    fun greetingMessage(): String {
        val c = Calendar.getInstance()
        val timeOfDay = c.get(Calendar.HOUR_OF_DAY)

        return when (timeOfDay) {
            in 0..11 -> "Good Morning"
            in 12..15 -> "Good Afternoon"
            in 16..20 -> "Good Evening"
            in 21..23 -> "Good Night"
            else -> "Hello"
        }
    }
}
