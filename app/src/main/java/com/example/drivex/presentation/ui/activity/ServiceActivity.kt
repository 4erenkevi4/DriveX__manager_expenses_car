package com.example.drivex.presentation.ui.activity

import android.annotation.SuppressLint
import android.app.Application
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.drivex.R
import com.example.drivex.data.model.Expenses
import com.example.drivex.presentation.adapters.ServiceAdapter
import com.example.drivex.presentation.ui.activity.viewModels.AbstractViewModel


class ServiceActivity : AbstractActivity() {

    private lateinit var textViewDate: TextView
    private lateinit var editTextMileage: EditText
    private lateinit var editTextCost: EditText
    private lateinit var description: TextView
    private lateinit var buttonPhoto: ImageView
    private lateinit var buttonSave: ImageView
    private lateinit var photoPreview: ImageView
    lateinit var fuelViewModel: AbstractViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var descRecyclerView: View
    private lateinit var constraintLayout: ConstraintLayout

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service)
        Toast.makeText(this, "Пожалуйста, добавьте данные текущего ремонта.", Toast.LENGTH_SHORT)
            .show()
        textViewDate = findViewById(R.id.textView_data)
        editTextMileage = findViewById(R.id.text_mileag)
        editTextCost = findViewById(R.id.cost_fuel)
        description = findViewById(R.id.edit_text_desc)
        buttonPhoto = findViewById(R.id.button_photo_)
        buttonSave = findViewById(R.id.button_save_)
        photoPreview = findViewById(R.id.fuel_photo_container)
        descRecyclerView = findViewById(R.id.desc_recyclerview)
        constraintLayout = findViewById(R.id.constraintLayout)
        recyclerView = findViewById(R.id.recycler_view_service)
        val viewModelFactory = ViewModelFactory(application)
        fuelViewModel = ViewModelProvider(this, viewModelFactory).get(AbstractViewModel::class.java)
        initCalendar(textViewDate)
        initSaveButton(buttonSave)
        initCamera(photoPreview, buttonPhoto, recyclerView, descRecyclerView)
        setRecyclerview()
        description.setOnClickListener {
            recyclerView.isVisible = true
            descRecyclerView.isVisible = true
            photoPreview.isVisible = false
        }
        if (intent.getBooleanExtra("RESTART_AFTER_CAMERA", false)) {
            recyclerView.isVisible = false
            descRecyclerView.isVisible = false
            photoPreview.isVisible = true
        }
    }

    override fun initCalendar(textViewDate: TextView) {
        initCalendar(textViewDate, this)
    }


    override fun putData() {
        val mileage: String = editTextMileage.text.toString()
        val desc: String = description.text.toString()
        val cost: String = editTextCost.text.toString()
        val intent = Intent(this, MainActivity::class.java)
        if (mileage.isNotEmpty() && cost.isNotEmpty()) {

            val refuel = Expenses(
                id = 0,
                title = "Сервис",
                mileage = mileage.toInt(),
                description = desc,
                totalSum = cost.toDouble(),
                date = textViewDate.text.toString(),
                icon = R.drawable.servicel_icon,
                photoURI = uriPhoto?.toString()
            )
            fuelViewModel.addRefuel(refuel)
            startActivity(intent)

        } else {
            if (mileage.isEmpty()) {
                showToast("Пожалуйста, добавьте текущее значение пробега", editTextMileage)
            }
            if (cost.isEmpty()) {
                showToast("Пожалуйста, укажите стоимость текущего ремонта", editTextCost)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setRecyclerview() {
        val adapter = ServiceAdapter { item, view ->
            description.text = description.text.toString() + " " + item + ";"
            view.setBackgroundColor(Color.GRAY)
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

    }

}

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
    private val aplication: Application
) : ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel?> create(modelClass: Class<T>): T {
        return AbstractViewModel(aplication) as T
    }
}


