package com.example.drivex.presentation.ui.activity

import android.app.Application
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import com.example.drivex.R
import com.example.drivex.data.model.Refuel
import com.example.drivex.presentation.ui.activity.viewModels.FuelViewModel

class FuelActivity : AbstractActivity() {

    private lateinit var textViewDate: TextView
    private lateinit var editTextMileage: EditText
    private lateinit var editTextCost: EditText
    private lateinit var editTextVolume: EditText
    private lateinit var buttonPhoto: ImageView
    private lateinit var buttonSave: ImageView
    private lateinit var containerPhoto: ImageView
    lateinit var fuelViewModel: FuelViewModel
    private val CAMERA_PERMISSION_CODE = 1
    private val CAMERA_PIC_REQUEST = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_refuel)
        Toast.makeText(this, "Пожалуйста, добавьте данные текущей заправки.", 500.toInt()).show()
        textViewDate = findViewById(R.id.textView_date)
        editTextMileage = findViewById(R.id.text_mileage)
        editTextCost = findViewById(R.id.cost_fuell)
        editTextVolume = findViewById(R.id.edir_text_volume)
        buttonPhoto = findViewById(R.id.button_photo)
        buttonSave = findViewById(R.id.button_save)
        containerPhoto = findViewById(R.id.fuel_photo_container)
        val viewModelFactory = ViewModelFactory(application)
        fuelViewModel = ViewModelProvider(this, viewModelFactory).get(FuelViewModel::class.java)
        initCalendar(textViewDate)
        initPhotoButton(buttonPhoto)
        initSaveButton(buttonSave)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_PIC_REQUEST) {
            val image: Bitmap? = data?.getParcelableExtra("data")
            containerPhoto.setImageBitmap(image)
        }
    }

    private suspend fun insertTodo() {
        val mileage: String = editTextMileage.text.toString()
        val volume: String = editTextVolume.text.toString()
        val cost: String = editTextCost.text.toString()
        val refuel = Refuel(
            id = 0,
            mileage = mileage.toInt(),
            volume = volume.toInt(),
            totalSum = cost.toDouble(),
            date = textViewDate.text.toString()
        )
        fuelViewModel.readAllDataByDate()
    }

     override fun putData() {
        val mileage: String = editTextMileage.text.toString()
        val volume: String = editTextVolume.text.toString()
        val cost: String = editTextCost.text.toString()
        val intent = Intent(this, MainActivity::class.java)
        if (mileage.isNotEmpty() && volume.isNotEmpty() && cost.isNotEmpty()) {
            val refuel = Refuel(
                id = 0,
                mileage = mileage.toInt(),
                volume = volume.toInt(),
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
                    "Пожалуйста, укажите стоимость текущей заправки",
                    Toast.LENGTH_SHORT
                ).show()
            }
            if (volume.isEmpty()) {
                editTextVolume.setBackgroundColor(Color.RED)
                Toast.makeText(
                    this,
                    "Пожалуйста, добавьте обьем заправленного топлива",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    class ViewModelFactory(
        private val aplication: Application
    ) : ViewModelProvider.Factory {
        override fun <T : androidx.lifecycle.ViewModel?> create(modelClass: Class<T>): T {
            return FuelViewModel(aplication) as T
        }
    }
}