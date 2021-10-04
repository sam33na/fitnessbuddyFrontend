package com.example.fitnessbuddy.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.fitnessbuddy.R
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        CoroutineScope(Dispatchers.IO).launch {
            delay(1000)


            val sharedPref= getSharedPreferences("LoginPref", MODE_PRIVATE)


            withContext(Dispatchers.Main){

                if (sharedPref.contains("IsLogin")){

                    Toast.makeText(this@MainActivity, "Logged in successful", Toast.LENGTH_SHORT).show()

                    startActivity(Intent(this@MainActivity, Dashboard::class.java))
                    finish()

                }

                else
                {
                    //
                    startActivity(Intent(this@MainActivity, Login::class.java))
                    finish()
                }
            }

        }
    }
}