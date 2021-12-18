package com.example.drivex.presentation.ui.activity

import android.annotation.SuppressLint
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.drivex.R
import com.example.drivex.data.model.Expenses
import com.example.drivex.presentation.ui.activity.viewModels.AbstractViewModel
import com.example.drivex.utils.Constans.IS_PAYMENT
import com.example.drivex.utils.Constans.IS_REFUEL
import com.example.drivex.utils.Constans.IS_SHOPPING
import com.example.drivex.utils.Constans.PAYMENT_TYPE


@Suppress("UNCHECKED_CAST")
class FuelActivity : AbstractActivity() {

    private lateinit var textViewDate: TextView
    private lateinit var editTextMileage: EditText
    private lateinit var editTextCost: EditText
    private lateinit var editTextVolume: EditText
    private lateinit var description: EditText
    private lateinit var buttonPhoto: ImageView
    private lateinit var desButtonPhoto: TextView
    private lateinit var buttonSave: ImageView
    private lateinit var containerPhoto: ImageView
    private lateinit var fuelViewModel: AbstractViewModel
    private lateinit var selectionType: TextView
    private var paymentType: String? = ""
    var titte: String = ""
    var startToast = ""
    var desc = ""
    var icon: Int? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        paymentType = intent.getStringExtra(PAYMENT_TYPE)
        setContentView(R.layout.activity_refuel)
        textViewDate = findViewById(R.id.textView_date)
        editTextMileage = findViewById(R.id.text_mileage)
        editTextCost = findViewById(R.id.cost_fuell)
        editTextVolume = findViewById(R.id.edit_text_volume)
        buttonPhoto = findViewById(R.id.button_photo)
        buttonSave = findViewById(R.id.button_save)
        containerPhoto = findViewById(R.id.fuel_photo_container)
        description = findViewById(R.id.textView_description)
        selectionType = findViewById(R.id.selection_type)
        desButtonPhoto = findViewById(R.id.description_button_photo)
        val viewModelFactory = ViewModelFactory(application)
        fuelViewModel = ViewModelProvider(this, viewModelFactory).get(AbstractViewModel::class.java)
        val id = intent.getLongExtra("id", -1L)
        if (id == -1L) {
            initSaveButton(buttonSave)
        } else {
            updateMode(id)
        }
        initCalendar(textViewDate)
        initCamera(containerPhoto, buttonPhoto)
        initTypePayment()
        makeText(this, startToast, Toast.LENGTH_SHORT).show()
    }

    override fun initCalendar(textViewDate: TextView) {
        initCalendar(textViewDate, this)
    }

    private fun initTypePayment(){

        when (paymentType) {
            IS_REFUEL -> {
                titte = "Заправка"
                desc = "Информация о выбранной заправке:"
                icon = R.drawable.fuel_icon; startToast = "Пожалуйста, добавьте данные текущей заправки"
            }
            IS_SHOPPING -> {
                titte = "Покупка"
                desc = "Информация о выбранной покупке:"
                icon = R.drawable.shoping_icon
                startToast = "Пожалуйста, добавьте информацию о вашей покупке"
                editTextVolume.isVisible = false
                selectionType.isVisible = false
            }
            IS_PAYMENT -> {
                titte = "Платеж"
                desc = "Информация о выбранном платеже:"
                icon = R.drawable.pay_icon; startToast = "Пожалуйста, добавьте информацию о вашем платеже"
                editTextVolume.isVisible = false
                selectionType.isVisible = false
            }

        }
    }


    @SuppressLint("SetTextI18n")
    fun updateMode(id: Long) {
        val intent = Intent(this, MainActivity::class.java)
        fuelViewModel.readRefuelById(id).observe(this, { desc ->
            desc?.let {
                desButtonPhoto.isVisible = false
                buttonPhoto.isVisible = false
                selectionType.text = "Категория"
                editTextMileage.setText(desc.mileage.toString())
                editTextVolume.setText(desc.title)
                editTextCost.setText(desc.totalSum.toString())
                textViewDate.text = desc.date
                description.hint = desc.description
                containerPhoto.setImageURI(desc.photoURI?.toUri())
            }
        })
        buttonSave.setOnClickListener { startActivity(intent) }
    }

    override fun putData() {

        val mileage: String = editTextMileage.text.toString()
        val volume: String = editTextVolume.text.toString()
        val cost: String = editTextCost.text.toString()
        var descriptionValue: String = description.text.toString()
        if (descriptionValue.isEmpty())
            descriptionValue = desc

        val intent = Intent(this, MainActivity::class.java)
        if (mileage.isNotEmpty() && volume.isNotEmpty() && cost.isNotEmpty()) {
            val expenses = Expenses(
                id = 0,
                title = titte,
                mileage = mileage.toInt(),
                volume = volume.toInt(),
                totalSum = cost.toDouble(),
                date = textViewDate.text.toString(),
                icon = icon,
                description =descriptionValue,
                photoURI = uriPhoto?.toString()
            )
            fuelViewModel.addRefuel(expenses)
            startActivity(intent)

        } else {
            if (mileage.isEmpty()&& paymentType == IS_REFUEL) {
                showToast("Пожалуйста, добавьте текущее значение пробега", editTextMileage)
            }
            if (volume.isEmpty()&& paymentType == IS_REFUEL) {
                showToast("Пожалуйста, добавьте обьем заправленного топлива", editTextVolume)
            }
            if (cost.isEmpty()) {
                showToast("Пожалуйста, укажите стоимость", editTextCost)
            }

        }
    }

    class ViewModelFactory(
        private val aplication: Application
    ) : ViewModelProvider.Factory {
        override fun <T : androidx.lifecycle.ViewModel?> create(modelClass: Class<T>): T {
            return AbstractViewModel(aplication) as T
        }
    }
}

