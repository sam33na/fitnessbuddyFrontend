package com.example.fitnessbuddy.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.fitnessbuddy.R
import com.example.fitnessbuddy.api.ServiceBuilder
import com.example.fitnessbuddy.entity.ItemsEnt
import com.example.fitnessbuddy.repository.AddItemRepository
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class AddActivity : AppCompatActivity() {

    private lateinit var oneway: TextView
    private lateinit var twoway: TextView
    private lateinit var route: Spinner



    private lateinit var imgdeparture: ImageButton
    private lateinit var imgdepdate: ImageButton
    private lateinit var tvdeparture: TextView
    private lateinit var tvdate: TextView


    private lateinit var imgarrival: ImageButton
    private lateinit var imgarrdate: ImageButton
    private lateinit var tvarrival: TextView
    private lateinit var edtbussno: EditText
    private lateinit var description: EditText
    private lateinit var alayout: ConstraintLayout

    private lateinit var btnAdd: MaterialButton
    private lateinit var toolbar: Toolbar
    private val des = arrayOf("Fare", "15", "20", "25", "30")
    private val location = arrayOf(
        "Choose Activity",
        "Cycling",
        "Sleeping",
        "Running",
        "Work",
        "Driving",
        "Reading"

    )
    private var selectedTimeD: String = ""
    private var selectedTimeA: String = ""
    private var currentl: String = ""


    var date = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        binding()
        val adapter1 = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            location
        )

        route.adapter = adapter1
        route.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                currentl = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }




        tvdate.setOnClickListener {
            loadCalendar()
        }
        btnAdd.setOnClickListener {
            //addTicket()
        }

        tvdeparture.setOnClickListener {
            OnClickTime()
        }
        tvarrival.setOnClickListener {
            OnClickTime1()
        }
        toolbar = findViewById(R.id.title)

        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }
        toolbar.setNavigationOnClickListener() {
            finish()
        }
    }

    private fun loadCalendar() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener() { view, yearNew, monthOfYear, dayOfMonth ->
                date = "$dayOfMonth/${monthOfYear + 1}/$yearNew"
                tvdate.setText(date)
            },
            year,
            month,
            day
        )
        datePickerDialog.datePicker.minDate = c.timeInMillis;
        datePickerDialog.datePicker.maxDate = (c.timeInMillis) + (1000 * 60 * 60 * 24 * 1);

        datePickerDialog.show()
    }


    private fun binding() {
        oneway = findViewById(R.id.oneway)
        route = findViewById(R.id.route)
        imgdeparture = findViewById(R.id.imgdep)
        imgdepdate = findViewById(R.id.imgdepdate)
        tvdate = findViewById(R.id.depdate)
        tvdeparture = findViewById(R.id.departure)
        imgarrival = findViewById(R.id.imgarr)
        tvarrival = findViewById(R.id.arrival)
        imgarrdate = findViewById(R.id.imgarrdate)
        edtbussno = findViewById(R.id.edtbussno)
        btnAdd = findViewById(R.id.btnadd)
        alayout = findViewById(R.id.alayout)
        description = findViewById(R.id.description)
    }

    private fun validateInput(): Boolean {
//        val sdf = SimpleDateFormat("d/M/yyyy")
//        val currentDate = sdf.format(Date())
//        val c = Calendar.getInstance()
//        val hour = c.get(Calendar.HOUR_OF_DAY)
//        val h=selectedTimeD.substring(0,2).toInt()
        var res = true
        when {
            (TextUtils.isEmpty(edtbussno.text)) -> {
                edtbussno.error = "This field should not be empty"
                edtbussno.requestFocus()
                res = false
            }
            (TextUtils.isEmpty(description.text)) -> {
                description.error = "This field should not be empty"
                description.requestFocus()
                res = false
            }
            (date == "") -> {
                Toast.makeText(
                    this@AddActivity,
                    "Please select date",
                    Toast.LENGTH_SHORT
                ).show()
                res = false
            }
            (selectedTimeD == "") -> {
                Toast.makeText(
                    this@AddActivity,
                    "Please select departure time",
                    Toast.LENGTH_SHORT
                ).show()
                res = false
            }
            (selectedTimeD == "") -> {
                Toast.makeText(
                    this@AddActivity,
                    "Please select arrival time ",
                    Toast.LENGTH_SHORT
                ).show()
                res = false
            }
            (currentl == "Choose route") -> {
                Toast.makeText(
                    this@AddActivity,
                    "Please select departure location ",
                    Toast.LENGTH_SHORT
                ).show()
                res = false
            }
          }
        return res
    }

    private fun OnClickTime() {
        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            selectedTimeD = SimpleDateFormat("HH:mm").format(cal.time)
            tvdeparture.setText(selectedTimeD)
        }
        TimePickerDialog(
            this,
            timeSetListener,
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE),
            true
        ).show()

    }

    private fun OnClickTime1() {
        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            selectedTimeA = SimpleDateFormat("HH:mm").format(cal.time)
            tvarrival.setText(selectedTimeA)
        }
        TimePickerDialog(
            this,
            timeSetListener,
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE),
            true
        ).show()
    }

    private fun addTicket() {
        val act = currentl
        val start = selectedTimeA
        val end = selectedTimeD
        val date = date
        val priority=edtbussno.text.toString()
        val description=description.text.toString()

        val addItem = ItemsEnt(
            act=act,
            start = start,
            end = end,
            date = date,
            priority=priority,
            description = description,
        )
        if (validateInput()) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val repository = AddItemRepository()
                    val response = repository.Addplan(addItem)
                    if (response.success == true) {
                        withContext(Dispatchers.Main) {
                            val snack =
                                Snackbar.make(
                                    alayout,
                                    "Ticket Added",
                                    Snackbar.LENGTH_LONG
                                )
                            snack.setAction("OK", View.OnClickListener {
                                finish()
                            })
                            snack.show()
                            delay(2000)
                        }
                        startActivity(
                            Intent(
                                this@AddActivity,
                                HomeActivity::class.java
                            )
                        )
                    }
                }
                catch (ex: Exception) {
                    withContext(Dispatchers.Main) {
                        Log.d("Error ", ex.toString())
                        Toast.makeText(
                            this@AddActivity,
                            ex.toString(), Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}