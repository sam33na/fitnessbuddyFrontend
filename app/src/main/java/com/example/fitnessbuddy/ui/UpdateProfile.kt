package com.example.fitnessbuddy.ui

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.*
import com.bumptech.glide.Glide
import com.example.fitnessbuddy.R
import com.example.fitnessbuddy.api.ServiceBuilder
import com.example.fitnessbuddy.repository.UserRepository
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class UpdateProfile : AppCompatActivity() {
    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPhone: EditText
    private lateinit var etaddress: EditText
    private lateinit var etPassword: TextView
    private lateinit var txtusrnm: TextView
    private lateinit var btnUpdate: Button
    private lateinit var imgprofile: CircleImageView
    private lateinit var imageView9: ImageView
    private var imageUrl: String? = null

    private var REQUEST_GALLERY_CODE = 0
    private var REQUEST_CAMERA_CODE = 1
    var imagepath: String = ""
    var password: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_profile)

        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etPhone = findViewById(R.id.etPhone)
        etaddress = findViewById(R.id.etaddress)
        etPassword = findViewById(R.id.etPw)
        btnUpdate = findViewById(R.id.btnUpdate)
        imgprofile = findViewById(R.id.imageView8)
        imageView9 = findViewById(R.id.imageView9)
        txtusrnm=findViewById(R.id.txtusrnm)
        getUser()
        imgprofile.setOnClickListener {
            loadPopUpMenu()
        }
        imageView9.setOnClickListener {
            finish()
        }

        btnUpdate.setOnClickListener {

            update()


        }

    }

    private fun getUser() {
        val userid = ServiceBuilder.userID!!

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val userRepository = UserRepository()
                val response = userRepository.getUser(userid)
                if (response.message == "success") {
                    password = response.data?.password
                    etName.setText(response.data?.username)
                    txtusrnm.text = response.data?.username
                    etEmail.setText(response.data?.email)
                    etPhone.setText(response.data?.phone)
                    withContext(Dispatchers.Main) {
                        var imagePath = ServiceBuilder.loadImagePath() + response.data?.dp
                        imagePath = imagePath.replace("\\", "/")
                        if (!response.data?.dp.equals("no-img.jpg")) {
                            Glide.with(this@UpdateProfile)
                                .load(imagePath)
                                .into(imgprofile)
                        }
                    }
                }
            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@UpdateProfile,
                        "Error : ${ex.toString()}", Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }


    // Load pop up menu
    private fun loadPopUpMenu() {
        val popupMenu = PopupMenu(this, imgprofile)
        popupMenu.menuInflater.inflate(R.menu.camera, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menuCamera ->
                    camera()
                R.id.menuGallery ->
                    gallery()
            }
            true
        }
        popupMenu.show()
    }


    private fun gallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_GALLERY_CODE)
    }

    private fun camera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, REQUEST_CAMERA_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_GALLERY_CODE && data != null) {
                val selectedImage = data.data
                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                val contentResolver = contentResolver
                val cursor =
                    contentResolver.query(selectedImage!!, filePathColumn, null, null, null)
                cursor!!.moveToFirst()
                val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                imageUrl = cursor.getString(columnIndex)
                imgprofile.setImageBitmap(BitmapFactory.decodeFile(imageUrl))
                cursor.close()
            } else if (requestCode == REQUEST_CAMERA_CODE && data != null) {
                val imageBitmap = data.extras?.get("data") as Bitmap
                val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                val file = bitmapToFile(imageBitmap, "$timeStamp.jpg")
                imageUrl = file!!.absolutePath
                imgprofile.setImageBitmap(BitmapFactory.decodeFile(imageUrl))
            }
        }

    }

    private fun bitmapToFile(
        bitmap: Bitmap,
        fileNameToSave: String
    ): File? {
        var file: File? = null
        return try {
            file = File(
                getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                    .toString() + File.separator + fileNameToSave
            )
            file!!.createNewFile()

            //Convert bitmap to byte array
            val bos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos) // YOU can also save it in JPEG
            val bitMapData = bos.toByteArray()
            //write the bytes in file
            val fos = FileOutputStream(file)
            fos.write(bitMapData)
            fos.flush()
            fos.close()
            file

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            file // it will return null
        }
    }

    fun getMimeType(file: File): String? {
        var type: String? = null
        val extension = MimeTypeMap.getFileExtensionFromUrl(file.path)
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        }
        return type
    }


    private fun uploadImage(id: String) {
        if (imageUrl != null) {
            val file = File(imageUrl!!)
            val mimeType = getMimeType(file)
            val reqFile =
                RequestBody.create(MediaType.parse(mimeType!!), file)
            val body =
                MultipartBody.Part.createFormData("dp", file.name, reqFile)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val userRepository = UserRepository()
                    val response = userRepository.uploadImage(id, body)
                    if (response.message == "updated Image") {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                this@UpdateProfile,
                                "Uploaded",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }
                } catch (ex: java.lang.Exception) {
                    withContext(Dispatchers.Main) {
                        Log.d("Mero Error ", ex.localizedMessage)
                        Toast.makeText(
                            this@UpdateProfile,
                            ex.localizedMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }


    private fun update() {
        if(validateInput()) {
            val id = ServiceBuilder.userID!!
            val username = etName.text.toString()
            val email = etEmail.text.toString()
            val address = etaddress.text.toString()
            val phone = etPhone.text.toString()
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val userRepository = UserRepository()
                    val response =
                        userRepository.updateUser(id, username, email, address, password, phone)

                    if (response.message == "updated profile") {
                        if (imageUrl != null) {
                            uploadImage(id)
                        }
                        startActivity(
                            Intent(
                                this@UpdateProfile,
                                ViewProfile::class.java
                            )
                        )
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                this@UpdateProfile,
                                "updated",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        finish()
                    }
                } catch (ex: java.lang.Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@UpdateProfile,
                            ex.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            }
        }

    }
    private fun validateInput(): Boolean {
        var res = true
        when {
            (TextUtils.isEmpty(etName.text)) -> {
                etName.error = "This field should not be empty"
                etName.requestFocus()
                res = false
            }
            (TextUtils.isEmpty(etEmail.text)) -> {
                etEmail.error = "This field should not be empty"
                etEmail.requestFocus()
                res = false
            }
        }
        return res
    }


}