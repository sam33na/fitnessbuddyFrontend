package com.example.fitnessbuddy.ui


import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CancellationSignal
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import com.example.fitnessbuddy.R
import com.example.fitnessbuddy.api.ServiceBuilder
import com.example.fitnessbuddy.entity.User
import com.example.fitnessbuddy.forgotpassword.EmailVerification
import com.example.fitnessbuddy.repository.UserRepository
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Login : AppCompatActivity() {
    //private lateinit var finger: ImageView

    private lateinit var etUsr: EditText
    private lateinit var etPw: EditText
    private lateinit var tvReg: TextView
    private lateinit var btnLogin: Button
    private lateinit var finger: ImageView
    private lateinit var constraint: ConstraintLayout
    private lateinit var forgotpass: TextView
    private val permissions = arrayOf(
        android.Manifest.permission.CAMERA,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        android.Manifest.permission.ACCESS_FINE_LOCATION
    )

    //to request the permission
    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            permissions, 1
        )
    }

    //to check the permissions
    private fun hasPermission(): Boolean {
        var hasPermission = true
        for (permission in permissions) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                hasPermission = false
            }
        }
        return hasPermission
    }
    //fingerprint
    private  var cancellationSignal: CancellationSignal?=null
    private lateinit var btn:Button
    private val authenticationcallback: BiometricPrompt.AuthenticationCallback
        get()=
            @RequiresApi(Build.VERSION_CODES.P)
            object : BiometricPrompt.AuthenticationCallback()
            {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                    super.onAuthenticationError(errorCode, errString)
                    notifyUser("Authentication error: $errString")
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
                    super.onAuthenticationSucceeded(result)
                    notifyUser("authentication success")
                    startActivity(Intent(this@Login, Dashboard::class.java))
                }
            }

    @RequiresApi(Build.VERSION_CODES.P)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        etUsr = findViewById(R.id.etUsr)
        etPw = findViewById(R.id.etPw)
        tvReg = findViewById(R.id.tvReg)
        btnLogin = findViewById(R.id.button)
        constraint = findViewById(R.id.layout)
        finger = findViewById(R.id.finger)

        if (!hasPermission()) {
            requestPermission()
        }
        btnLogin.setOnClickListener {
            startActivity(
                Intent(
                    this@Login,
                    HomeActivity::class.java
                )
            )

        }
//        forgotpass.setOnClickListener {
//            startActivity(
//                Intent(
//                    this@Login,
//                    EmailVerification::class.java
//                )
//            )
//        }

        finger.setOnClickListener {
            val biometricPrompt=BiometricPrompt.Builder(this)
                .setTitle("Authentication required")
                .setSubtitle("Please use fingerprint for login")
                .setDescription("Place your finger on the sensor")
                .setNegativeButton("Cancel",this.mainExecutor, DialogInterface.OnClickListener { dialog, which ->
                    notifyUser("Fingerprint sign-in cancelled")
                }).build()

            biometricPrompt.authenticate(getCancelSignal(),mainExecutor,authenticationcallback)

        }

        tvReg.setOnClickListener {
            startActivity(
                Intent(
                    this@Login,
                    Register::class.java
                )
            )
        }
    }


    private fun validate(): Boolean {
        var result = true
        when {
            TextUtils.isEmpty(etUsr.text) -> {
                etUsr.error = "This field should not be empty"
                etUsr.requestFocus()
                result = false
            }
            TextUtils.isEmpty(etPw.text) -> {
                etPw.error = "This field should not be empty"
                etPw.requestFocus()
                result = false
            }
        }
        return result
    }

//    private fun login()
//    {
//        val username = etUsr.text.toString()
//        val password = etPw.text.toString()
//        if(validate()) {
//            CoroutineScope(Dispatchers.IO).launch {
//                try {
//                    val repository = UserRepository()
//                    val response = repository.checkUser(username, password)
//                    if (response.message == "successfull authentication") {
//                        ServiceBuilder.token = "Bearer ${response.token}"
//                        ServiceBuilder.userID = response.data!!._id
//                        ServiceBuilder.name = response.data!!.username
//                        ServiceBuilder.role = response.role
//                        saveSharedPref()
//                        if (response.role == "Admin") {
//                            startActivity(
//                                Intent(
//                                    this@Login,
//                                    Dashboard::class.java
//                                )
//                            )
//                            finish()
//                        } else {
//                            startActivity(
//                                Intent(
//                                    this@Login,
//                                    HomeActivity::class.java
//                                )
//                            )
//                            finish()
//                        }
//                    } else {
//                        withContext(Dispatchers.Main) {
//                            val snack =
//                                Snackbar.make(
//                                    constraint,
//                                    "Invalid credentials",
//                                    Snackbar.LENGTH_LONG
//                                )
//                            snack.setAction("OK", View.OnClickListener {
//                                snack.dismiss()
//                            })
//                            snack.show()
//                            Toast.makeText(this@Login, "Bearer ${response.token}", Toast.LENGTH_SHORT).show()
//                        }
//                    }
//                } catch (ex: Exception) {
//                    withContext(Dispatchers.Main) {
//                        Toast.makeText(
//                            this@Login,
//                            ex.toString(),
//                            Toast.LENGTH_SHORT
//                        ).show()
//
//                    }
//                }
//            }
//        }
//    }


    private fun notifyUser(message: String)
    {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun getCancelSignal(): CancellationSignal
    {
        cancellationSignal = CancellationSignal()
        cancellationSignal?.setOnCancelListener {
            notifyUser("aithentication cancelled by user")
        }
        return cancellationSignal as CancellationSignal
    }

    private fun saveSharedPref() {
        val sharedPref = getSharedPreferences("LoginPref",
            MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("IsLogin", true)
        editor.apply()

    }
}
