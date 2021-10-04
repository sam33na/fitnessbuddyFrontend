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
import com.example.fitnessbuddy.api.ServiceBuilder
import com.example.fitnessbuddy.repository.OTPRepository
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*

class EmailVerification : AppCompatActivity() {
    private lateinit var emailverify: EditText
    private lateinit var send: MaterialButton
    private lateinit var elayout: LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_verification)
        emailverify=findViewById(R.id.emailverify)
        send=findViewById(R.id.send)
        elayout=findViewById(R.id.elayout)

        send.setOnClickListener {
            val email = emailverify.text.toString()
            if (validateInput()){
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val repository = OTPRepository()
                        val response = repository.checkEmail(email)
                        if (response.message == "Success") {
                            send.isClickable=false
                            ServiceBuilder.email = response.data!!.email
                            withContext(Dispatchers.Main) {
                                val snack =
                                    Snackbar.make(
                                        elayout,
                                        "OTP Code send to you email",
                                        Snackbar.LENGTH_LONG
                                    )
                                snack.setAction("Ok", View.OnClickListener {
                                    snack.dismiss()
                                })
                                snack.show()
                                delay(2000)
                            }
                            startActivity(
                                Intent(this@EmailVerification,
                                    OTPVerify::class.java)
                            )
                            finish()

                        } else {
                            withContext(Dispatchers.Main) {
                                val snack =
                                    Snackbar.make(
                                        elayout,
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
                                this@EmailVerification,
                                ex.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }

        }

    }

    private fun validateInput(): Boolean {
        var res = true
        when {
            (TextUtils.isEmpty(emailverify.text))-> {
                emailverify.error = "This field should not be empty"
                emailverify.requestFocus()
                res = false
            }
        }
        return res
    }
}