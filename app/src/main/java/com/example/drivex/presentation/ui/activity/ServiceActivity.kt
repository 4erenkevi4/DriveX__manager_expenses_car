package com.example.drivex.presentation.ui.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.drivex.R
import com.example.drivex.data.model.Refuel
import com.example.drivex.presentation.ui.activity.viewModels.FuelViewModel

class ServiceActivity : AbstractActivity() {

    private lateinit var textViewDate: TextView
    private lateinit var editTextMileage: EditText
    private lateinit var editTextCost: EditText
    private lateinit var description: TextView
    private lateinit var buttonPhoto: ImageView
    private lateinit var buttonSave: ImageView
    lateinit var fuelViewModel: FuelViewModel
    private lateinit var recyclerView: RecyclerView
    private val CAMERA_PERMISSION_CODE = 1
    private val CAMERA_PIC_REQUEST = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service)
        Toast.makeText(this, "Пожалуйста, добавьте данные текущего ремонта.", Toast.LENGTH_SHORT).show()
        textViewDate = findViewById(R.id.textView_data)
        editTextMileage = findViewById(R.id.text_mileag)
        editTextCost = findViewById(R.id.cost_fuel)
        description = findViewById(R.id.edit_text_desc)
        buttonPhoto = findViewById(R.id.button_photo_)
        buttonSave = findViewById(R.id.button_save_)
        val viewModelFactory = FuelActivity.ViewModelFactory(application)
        fuelViewModel = ViewModelProvider(this, viewModelFactory).get(FuelViewModel::class.java)
        initCalendar(textViewDate)
        initPhotoButton(buttonPhoto)
        initSaveButton(buttonSave)
    }

    override fun putData() {
        val mileage: String = editTextMileage.text.toString()
        val desc: String = description.text.toString()
        val cost: String = editTextCost.text.toString()
        val intent = Intent(this, MainActivity::class.java)
        if (mileage.isNotEmpty() && cost.isNotEmpty()) {
            val refuel = Refuel(
                id = 1,
                mileage = mileage.toInt(),
                totalSum = cost.toDouble(),
                date = textViewDate.text.toString()
            )
            fuelViewModel.addRefuel(refuel)
            startActivity(intent)
        } else {
            if (mileage.isEmpty()) {
                editTextMileage.setBackgroundColor(Color.RED)
                Toast.makeText(
                    this,
                    "Пожалуйста, добавьте текущее значение пробега",
                    Toast.LENGTH_SHORT
                ).show()
            }
            if (cost.isEmpty()) {
                editTextCost.setBackgroundColor(Color.RED)
                Toast.makeText(
                    this,
                    "Пожалуйста, укажите стоимость текущего ремонта",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}