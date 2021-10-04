package com.example.fitnessbuddy.forgotpassword

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import com.example.fitnessbuddy.R
import com.example.fitnessbuddy.ui.Login
import com.example.fitnessbuddy.api.ServiceBuilder
import com.example.fitnessbuddy.repository.OTPRepository
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*

class ResetPassword : AppCompatActivity() {
    private lateinit var changepass: EditText
    private lateinit var changepass1: EditText
    private lateinit var confirm: MaterialButton
    private lateinit var rlayout: LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        changepass=findViewById(R.id.changepass)
        changepass1=findViewById(R.id.changepass1)
        confirm=findViewById(R.id.confirm)
        rlayout=findViewById(R.id.rlayout)

        confirm.setOnClickListener {
            val email= ServiceBuilder.email
            val pass=changepass.text.toString()
            val pass1=changepass1.text.toString()
            if (validateInput()) {
                if (pass != pass1) {
                    changepass1.error = "Password wrong"
                    changepass1.requestFocus()
                    return@setOnClickListener
                } else {

                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val repository = OTPRepository()
                            val response = repository.updatePass(email!!,pass)
                            if (response.message == "success") {
                                confirm.isClickable=false
                                val repository1 = OTPRepository()
                                val response1 = repository1.removeOTP(email!!)
                                if(response1.message=="success") {
                                    withContext(Dispatchers.Main) {
                                        val snack =
                                            Snackbar.make(
                                                rlayout,
                                                "Password Changed. Go to Login",
                                                Snackbar.LENGTH_LONG
                                            )
                                        snack.setAction("OK", View.OnClickListener {
                                            startActivity(
                                                Intent(
                                                    this@ResetPassword,
                                                    Login::class.java
                                                )
                                            )
                                            finish()
                                        })
                                        snack.show()
                                        delay(2000)
                                    }
                                }
                                startActivity(
                                    Intent(this@ResetPassword,
                                        Login::class.java)
                                )
                                finish()

                            } else {
                                withContext(Dispatchers.Main) {
                                    val snack =
                                        Snackbar.make(
                                            rlayout,
                                            "Invalid email",
                                            Snackbar.LENGTH_LONG
                                        )
                                    snack.setAction("OK", View.OnClickListener {
                                        snack.dismiss()
                                    })
                                    snack.show()
                                }
                            }
                        } catch (ex: Exception) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    this@ResetPassword,
                                    ex.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }


                }
            }
        }
    }
    private fun validateInput(): Boolean {
        var res = true
        when {
            (TextUtils.isEmpty(changepass.text)) -> {
                changepass.error = "This field should not be empty"
                changepass.requestFocus()
                res = false
            }
            (TextUtils.isEmpty(changepass1.text)) -> {
                changepass1.error = "This field should not be empty"
                changepass1.requestFocus()
                res = false
            }

        }
        return res
    }
}