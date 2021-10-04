package com.example.fitnessbuddy.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.fitnessbuddy.R
import com.google.android.material.card.MaterialCardView
import com.kiran.student.notification.NotificationChannels

class Settings : AppCompatActivity() {
    private lateinit var logout: MaterialCardView
    private lateinit var account: MaterialCardView
    private lateinit var aboutus: MaterialCardView
    private lateinit var sensorManager: SensorManager
    private var sensor: Sensor? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        logout = findViewById(R.id.logout)
        aboutus = findViewById(R.id.aboutus)
        account = findViewById(R.id.account)
        sensorManager = this.getSystemService(Context.SENSOR_SERVICE) as SensorManager

        if (!checkSensor())
            return
        else {
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
            // sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }


        account.setOnClickListener {
            startActivity(Intent(this, ViewProfile::class.java))
        }


        logout.setOnClickListener() {
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)

            builder.setTitle("Logout")
            builder.setIcon(R.drawable.ic_baseline_logout)
            builder.setMessage("Are you sure you want to logout?")
            builder.setPositiveButton("Yes") { dialogInterface, which ->
                logOutUser()
            }

            builder.setNegativeButton("No") { dialogInterface, which ->
                dialogInterface.dismiss()
            }

            builder.create().show()
        }
    }



    private fun logOutUser() {
        val logOutPref: SharedPreferences = this.getSharedPreferences("LoginPref", Context.MODE_PRIVATE)
        val logOutEditor: SharedPreferences.Editor = logOutPref.edit()
        logOutEditor.remove("IsLogin")
        logOutEditor.apply()
        logOutEditor.commit()

        startActivity(Intent(this, Login::class.java))
        showNotification()
    }
    private fun alert()
    {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Logout")
        builder.setIcon(R.drawable.ic_baseline_logout)
        builder.setMessage("Are you sure you want to logout?")
        builder.setPositiveButton("Yes") { dialogInterface, which ->
            logOutUser()
        }

        builder.setNegativeButton("No") { dialogInterface, which ->
            dialogInterface.dismiss()
        }

        builder.create().show()
    }
    private fun showNotification() {
        val notificationManager = NotificationManagerCompat.from(this)
        val notificationChannels = NotificationChannels(this)
        notificationChannels.createNotificationChannels()

        val notification = NotificationCompat.Builder(this, notificationChannels.CHANNEL_2)
            .setSmallIcon(R.drawable.notification)
            .setContentTitle("User logged out")
            .setContentText("Thank you for using the application")
            .setColor(Color.BLUE)
            .build()

        notificationManager.notify(1, notification)
    }

    private fun checkSensor(): Boolean {
        var flag = true
        if (sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE) == null) {
            flag = false
        }
        return flag
    }
}
