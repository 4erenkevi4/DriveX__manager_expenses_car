package com.example.drivex.presentation.ui.activity

import android.app.Application
import android.content.Intent
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
import com.example.drivex.utils.Constans

class ServiceActivity : AbstractActivity() {

    private lateinit var textViewDate: TextView
    private lateinit var editTextMileage: EditText
    private lateinit var editTextCost: EditText
    private lateinit var description: TextView
    private lateinit var buttonPhoto: ImageView
    private lateinit var buttonSave: ImageView
    lateinit var fuelViewModel: FuelViewModel
    private lateinit var recyclerView: RecyclerView

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
        val viewModelFactory = ViewModelFactory(application)
        fuelViewModel = ViewModelProvider(this, viewModelFactory).get(FuelViewModel::class.java)
        initPhotoButton(buttonPhoto)
        initCalendar(textViewDate)
        initSaveButton(buttonSave)
    }

    private suspend fun insertTodo() {
        val mileage: String = editTextMileage.text.toString()
        val desc: String = description.text.toString()
        val cost: String = editTextCost.text.toString()

        val refuel = Refuel(
            id = Constans.ACTIVITY_SERVICE,
            mileage = mileage.toInt(),
            //volume = volume.toInt(),
            totalSum = cost.toDouble(),
            date = textViewDate.text.toString()
        )
        fuelViewModel.readAllDataByDate()
    }


    override fun putData() {
        val mileage: String = editTextMileage.text.toString()
        val desc: String = description.text.toString()
        val cost: String = editTextCost.text.toString()
        val intent = Intent(this, MainActivity::class.java)
        if (mileage.isNotEmpty() && cost.isNotEmpty()) {

            val refuel = Refuel(
                id = 0,
                mileage = mileage.toInt(),
                //desc = description.toInt(),
                totalSum = cost.toDouble(),
                date = textViewDate.text.toString(),
                icon = R.drawable.servicel_icon
            )
            fuelViewModel.addRefuel(refuel)
            startActivity(intent)

        } else {
            if (mileage.isEmpty()) {
                showToast("Пожалуйста, добавьте текущее значение пробега",editTextMileage)
            }
            if (cost.isEmpty()) {
                showToast("Пожалуйста, укажите стоимость текущей заправки",editTextCost)
            }
        }
    }


    }
    @Suppress("UNCHECKED_CAST")
    class ViewModelFactory(
        private val aplication: Application
    ) : ViewModelProvider.Factory {
        override fun <T : androidx.lifecycle.ViewModel?> create(modelClass: Class<T>): T {
            return FuelViewModel(aplication) as T
        }
    }


