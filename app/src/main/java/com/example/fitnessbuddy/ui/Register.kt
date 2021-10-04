package com.example.fitnessbuddy.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.fitnessbuddy.R
import com.example.fitnessbuddy.entity.User
import com.example.fitnessbuddy.repository.UserRepository
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class Register : AppCompatActivity() {
    private lateinit var name: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var btnsignup: MaterialButton
    private lateinit var loginBack: TextView
    private lateinit var rlayout: ConstraintLayout

    private fun binding(
    ) {
        name = findViewById(R.id.name)
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        btnsignup = findViewById(R.id.btnsignup)
        loginBack=findViewById(R.id.dont)
        rlayout=findViewById(R.id.clayout)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        binding()
        loginBack.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    Login::class.java
                )
            )
        }
        btnsignup.setOnClickListener {
            val username = name.text.toString()
            val email = email.text.toString()
            val password = password.text.toString()
            val register = User(username = username, email = email, password = password)
            if(validateInput()) {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                       
                        val repository = UserRepository()
                        val response = repository.registerUser(register)
                        if (response.success == true) {

                            withContext(Dispatchers.Main) {
                                val snack =
                                    Snackbar.make(
                                        rlayout,
                                        "registered",
                                        Snackbar.LENGTH_LONG
                                    )
                                snack.setAction("OK", View.OnClickListener {
                                    startActivity(
                                        Intent(
                                            this@Register,
                                            Login::class.java
                                        )
                                    )
                                })
                                snack.show()
                            }

                        }

                    } catch (ex: Exception) {
                        withContext(Dispatchers.Main) {
                            Log.d("Error ", ex.toString())
                            Toast.makeText(
                                this@Register,
                                ex.toString(), Toast.LENGTH_SHORT
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
            (TextUtils.isEmpty(name.text)) -> {
                name.error = "This field should not be empty"
                name.requestFocus()
                res = false
            }

            (TextUtils.isEmpty(email.text)) -> {
                email.error = "This field should not be empty"
                email.requestFocus()
                res = false
            }
            (TextUtils.isEmpty(password.text)) -> {
                password.error = "This field should not be empty"
                password.requestFocus()
                res = false
            }
        }
        return res
    }
}

