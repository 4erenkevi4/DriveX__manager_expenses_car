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
import com.example.drivex.utils.Constans.PAYMENT
import com.example.drivex.utils.Constans.PAYMENT_TYPE
import com.example.drivex.utils.Constans.REFUEL
import com.example.drivex.utils.Constans.SHOPPING
import java.net.URI


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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        uriPhoto = data?.getStringExtra(URI_PHOTO)
        containerPhoto.isVisible = true
        containerPhoto.setImageURI(uriPhoto!!.toUri())
    }

    override fun initCalendar(textViewDate: TextView) {
        initCalendar(textViewDate, this)
    }

    private fun initTypePayment() {

        when (paymentType) {
            IS_REFUEL -> {
                titte = REFUEL
                desc = getString(R.string.info_about_refuel)
                icon = R.drawable.fuel_icon;
                startToast =
                    getString(R.string.please_add_refuel_data)
            }
            IS_SHOPPING -> {
                titte = SHOPPING
                desc = getString(R.string.info_of_your_buy)
                icon = R.drawable.shoping_icon
                startToast = getString(R.string.please_add_info_of_your_buy)
                editTextVolume.isVisible = false
                selectionType.isVisible = false
            }
            IS_PAYMENT -> {
                titte = PAYMENT
                desc = getString(R.string.payment_info)
                icon = R.drawable.pay_icon; startToast =
                    getString(R.string.please_add_payment_info)
                editTextVolume.isVisible = false
                selectionType.isVisible = false
            }

        }
    }

    private fun updateMode(id: Long) {
        val intent = Intent(this, MainActivity::class.java)
        fuelViewModel.readRefuelById(id).observe(this, { desc ->
            desc?.let {
                desButtonPhoto.isVisible = false
                buttonPhoto.isVisible = false
                selectionType.text = getText(R.string.category)
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
        var volume: String = editTextVolume.text.toString()
        val cost: String = editTextCost.text.toString()
        var descriptionValue: String = description.text.toString()
        if (descriptionValue.isEmpty())
            descriptionValue = desc

        val intent = Intent(this, MainActivity::class.java)
        if (mileage.isNotEmpty() && cost.isNotEmpty()) {
            if (volume.isNullOrEmpty())
                volume = "0"
            val expenses = Expenses(
                id = 0,
                title = titte,
                mileage = mileage.toInt(),
                volume = volume.toInt(),
                totalSum = cost.toDouble(),
                date = textViewDate.text.toString(),
                icon = icon,
                description = descriptionValue,
                photoURI = uriPhoto?.toString()
            )
            fuelViewModel.addRefuel(expenses)
            startActivity(intent)

        } else {
            if (mileage.isEmpty() && paymentType == IS_REFUEL) {
                showToast(getString(R.string.please_add_mileage), editTextMileage)
            }
            if (volume.isEmpty() && paymentType == IS_REFUEL) {
                showToast(getString(R.string.please_add_volume_fuel), editTextVolume)
            }
            if (cost.isEmpty()) {
                showToast(getString(R.string.please_add_cost_fuel), editTextCost)
            }

        }
    }

    class ViewModelFactory(
        private val aplication: Application,
    ) : ViewModelProvider.Factory {
        override fun <T : androidx.lifecycle.ViewModel?> create(modelClass: Class<T>): T {
            return AbstractViewModel(aplication) as T
        }
    }
}

