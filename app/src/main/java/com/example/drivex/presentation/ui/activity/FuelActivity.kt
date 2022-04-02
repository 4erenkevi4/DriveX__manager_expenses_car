package com.example.drivex.presentation.ui.activity

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.appcompat.widget.Toolbar
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import com.example.drivex.R
import com.example.drivex.data.model.Expenses
import com.example.drivex.presentation.ui.activity.viewModels.AbstractViewModel
import com.example.drivex.utils.Constans.PAYMENT
import com.example.drivex.utils.Constans.REFUEL
import com.example.drivex.utils.Constans.SHOPPING
import com.example.drivex.utils.Constans.PAYMENT_TYPE
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


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
    private lateinit var abstractViewModel: AbstractViewModel
    private lateinit var selectionType: TextView
    private lateinit var toolbar: Toolbar
    private lateinit var root: View
    private lateinit var typeOfExpenses: TextView
    private lateinit var descriptionBtnSave: TextView
    private lateinit var liveDataMileage: LiveData<String>

    private var paymentType: String? = ""
    private var titte: String? = null
    private var startToast = ""
    private var desc = ""
    private var icon: Int? = null
    private var localExpenses: Expenses? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModelFactory = ViewModelFactory(application)
        abstractViewModel =
            ViewModelProvider(this, viewModelFactory).get(AbstractViewModel::class.java)
        paymentType = intent.getStringExtra(PAYMENT_TYPE)
        setContentView(R.layout.activity_refuel)
        root = findViewById(R.id.refuel_root)
        textViewDate = findViewById(R.id.textView_date)
        editTextMileage = findViewById(R.id.text_mileage)
        editTextCost = findViewById(R.id.cost_fuell)
        editTextVolume = findViewById(R.id.edit_text_volume)
        buttonPhoto = findViewById(R.id.button_photo)
        buttonSave = findViewById(R.id.button_save)
        descriptionBtnSave = findViewById(R.id.description_button_save)
        containerPhoto = findViewById(R.id.fuel_photo_container)
        description = findViewById(R.id.textView_description)
        selectionType = findViewById(R.id.selection_type)
        desButtonPhoto = findViewById(R.id.description_button_photo)
        toolbar = findViewById(R.id.back_toolbar)
        typeOfExpenses = findViewById(R.id.type)
        liveDataMileage = abstractViewModel.lastMileageStr
        liveDataMileage.observe(this) {
            editTextMileage.setText(it)
        }
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
        setToolbar(toolbar, R.string.refuel, true)
        containerPhoto.setOnClickListener {
            initCamera(it as ImageView, it)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        uriPhoto = data?.getStringExtra(URI_PHOTO)
        uriPhoto?.let {
            containerPhoto.isVisible = true
            containerPhoto.setImageURI(it.toUri())
        }
    }

    override fun initCalendar(textViewDate: TextView) {
        initCalendar(textViewDate, this)
    }

    private fun initTypePayment() {

        when (paymentType) {
            REFUEL -> {
                titte = REFUEL
                desc = getString(R.string.info_about_refuel)
                icon = R.drawable.fuel_icon;
                startToast =
                    getString(R.string.please_add_refuel_data)
            }
            SHOPPING -> {
                root.setBackgroundResource(R.drawable.blue_backg_gradient3)
                titte = SHOPPING
                desc = getString(R.string.info_of_your_buy)
                icon = R.drawable.shoping_icon
                startToast = getString(R.string.please_add_info_of_your_buy)
                editTextVolume.isVisible = false
                selectionType.isVisible = false
            }
            PAYMENT -> {
                root.setBackgroundResource(R.drawable.blue_backg_gradient3)
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

        abstractViewModel.readRefuelById(id).observe(this) { expenses ->
            expenses?.let {
                descriptionBtnSave.text = getString(R.string.resave_expenses)
                desButtonPhoto.text = getString(R.string.make_new_photo)
                selectionType.text = getText(R.string.category)
                editTextMileage.setText(expenses.mileage.toString())
                editTextVolume.setText(expenses.volume.toString())
                editTextCost.setText(expenses.totalSum.toString())
                textViewDate.text = expenses.date.toString()
                description.hint = expenses.description
                typeOfExpenses.run {
                    this.isVisible = true
                    this.text = expenses.title
                }
                containerPhoto.setImageURI(expenses.photoURI?.toUri())
            }
            localExpenses = expenses
        }
        buttonSave.setOnClickListener { putData(isUpdate = true) }
    }

    override fun putData(isUpdate: Boolean) {

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
                id = localExpenses?.id ?: 0,
                title = titte ?: localExpenses?.title ?: typeOfExpenses.text.toString(),
                mileage = mileage.toInt(),
                volume = volume.toInt(),
                totalSum = cost.toDouble(),
                date = localExpenses?.date ?: textViewDate.text.toString(),
                icon = localExpenses?.icon ?: getIconByType(
                    titte ?: typeOfExpenses.text.toString()
                ),
                description = localExpenses?.description ?: descriptionValue,
                photoURI = uriPhoto ?: localExpenses?.photoURI,
                timeForMillis = localExpenses?.timeForMillis ?: getTimeForMillis(),
                month = abstractViewModel.getMonthOrYear(textViewDate.text.toString(), isMonth = true),
                year = abstractViewModel.getMonthOrYear(textViewDate.text.toString(), isMonth = false)
            )
            if (isUpdate)
                abstractViewModel.insert(expenses)
            else
                abstractViewModel.addRefuel(expenses)
            startActivity(intent)

        } else {
            if (mileage.isEmpty() && paymentType == REFUEL) {
                showToast(getString(R.string.please_add_mileage), editTextMileage)
            }
            if (volume.isEmpty() && paymentType == REFUEL) {
                showToast(getString(R.string.please_add_volume_fuel), editTextVolume)
            }
            if (cost.isEmpty()) {
                showToast(getString(R.string.please_add_cost_fuel), editTextCost)
            }

        }
    }

    private fun getTimeForMillis(): Long {
        val test = textViewDate.text.toString()
        val format: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.ENGLISH)
        return format.parse(test).time
    }

    private fun getIconByType(type: String): Int {
        return when (type) {
            REFUEL -> return R.drawable.fuel_icon;

            SHOPPING -> return R.drawable.shoping_icon

            PAYMENT -> return R.drawable.pay_icon
            else -> {
                R.drawable.fuel_icon
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

