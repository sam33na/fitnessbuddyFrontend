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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OTPVerify : AppCompatActivity() {
    private lateinit var otpverify: EditText
    private lateinit var sendotp: MaterialButton
    private lateinit var olayout: LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otpverify)

        otpverify=findViewById(R.id.otpverify)
        sendotp=findViewById(R.id.sendotp)
        olayout=findViewById(R.id.olayout)

        sendotp.setOnClickListener {
            val email= ServiceBuilder.email
            val code=otpverify.text.toString()
            if (validateInput()){
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val repository = OTPRepository()
                        val response = repository.checkOtp(email!!,code)
                        if (response.message == "success") {
                            startActivity(
                                Intent(this@OTPVerify,
                                    ResetPassword::class.java)
                            )
                            finish()
                        } else {
                            withContext(Dispatchers.Main) {
                                val snack =
                                    Snackbar.make(
                                        olayout,
                                        "Invalid OTP Code",
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
                                this@OTPVerify,
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
            (TextUtils.isEmpty(otpverify.text)) -> {
                otpverify.error = "This field should not be empty"
                otpverify.requestFocus()
                res = false
            }


        }
        return res
    }
}