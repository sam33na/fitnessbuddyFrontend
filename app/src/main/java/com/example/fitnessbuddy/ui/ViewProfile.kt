package com.example.fitnessbuddy.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.fitnessbuddy.R
import com.example.fitnessbuddy.api.ServiceBuilder
import com.example.fitnessbuddy.repository.UserRepository
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewProfile : AppCompatActivity() {
    private lateinit var txtusrnm: TextView
    private lateinit var txtName: TextView
    private lateinit var txtEmail: TextView
    private lateinit var txtPhone: TextView
    private lateinit var txtaddress: TextView
    private lateinit var btnEdit: Button
    private lateinit var imageView8: CircleImageView
    private lateinit var imageView9: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_profile)

        txtusrnm=findViewById(R.id.txtusrnm)
        txtName=findViewById(R.id.txtName)
        txtEmail=findViewById(R.id.txtEmail)
        txtPhone=findViewById(R.id.txtPhone)
        btnEdit=findViewById(R.id.btnEdit)
        imageView8=findViewById(R.id.imageView8)
        imageView9=findViewById(R.id.imageView9)
        imageView9.setOnClickListener {
            finish()
        }

        val userid= ServiceBuilder.userID!!
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val repository = UserRepository()
                val response = repository.getUser(userid)
                if (response.message == "success") {
                    txtusrnm.setText(response.data?.username)
                    txtName.setText(response.data?.username)
                    txtEmail.setText(response.data?.email)
                    txtPhone.setText(response.data?.phone)
                    withContext(Dispatchers.Main) {
                        var imagePath = ServiceBuilder.loadImagePath() + response.data?.dp
                        imagePath = imagePath.replace("\\", "/")
                        if (!response.data?.dp.equals("no-img.jpg")) {
                            Glide.with(this@ViewProfile)
                                .load(imagePath)
                                .into(imageView8)
                        }
                    }
                }
            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@ViewProfile,
                        ex.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        btnEdit.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    UpdateProfile::class.java
                )
            )
        }


    }
}