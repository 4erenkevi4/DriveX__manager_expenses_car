package com.example.drivex.presentation.ui.activity

import android.annotation.SuppressLint
import android.app.Application
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Camera
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Size
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.view.PreviewView
import androidx.lifecycle.ViewModelProvider
import com.example.drivex.R
import com.example.drivex.data.model.Refuel
import com.example.drivex.presentation.ui.activity.viewModels.AbstractViewModel
import com.example.drivex.utils.CameraxHelper
import timber.log.Timber
import java.io.File

@Suppress("UNCHECKED_CAST")
class FuelActivity : AbstractActivity() {

    private lateinit var textViewDate: TextView
    private lateinit var editTextMileage: EditText
    private lateinit var editTextCost: EditText
    private lateinit var editTextVolume: EditText
    private lateinit var description: TextView
    private lateinit var buttonPhoto: ImageView
    private lateinit var buttonSave: ImageView
    private lateinit var containerPhoto: PreviewView
    private lateinit var fuelViewModel: AbstractViewModel
    private val CAMERA_PIC_REQUEST = 2

    private val cameraxHelper by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CameraxHelper(
                caller = this,
                previewView = containerPhoto,
                onPictureTaken = { file, uri ->
                                Timber.i("Picture taken ${file.absolutePath}, uri=$uri")
                            },
                onError = { Timber.e(it) },
                builderPreview = Preview.Builder().setTargetResolution(Size(200, 200)),
                builderImageCapture = ImageCapture.Builder().setTargetResolution(Size(200, 200)),
                filesDirectory = File(
                                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                                "camerax_sample"
                            )
            )
        } else {
            TODO("VERSION.SDK_INT < LOLLIPOP")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_refuel)
        makeText(this, "Пожалуйста, добавьте данные текущей заправки.", Toast.LENGTH_SHORT).show()
        textViewDate = findViewById(R.id.textView_date)
        editTextMileage = findViewById(R.id.text_mileage)
        editTextCost = findViewById(R.id.cost_fuell)
        editTextVolume = findViewById(R.id.edit_text_volume)
        buttonPhoto = findViewById(R.id.button_photo)
        buttonSave = findViewById(R.id.button_save)
        containerPhoto = findViewById(R.id.fuel_photo_container)
        description = findViewById(R.id.textView_description)
        val viewModelFactory = ViewModelFactory(application)
        fuelViewModel = ViewModelProvider(this, viewModelFactory).get(AbstractViewModel::class.java)
        val id = intent.getLongExtra("id", -1L)
        if (id == -1L) {
            initSaveButton(buttonSave)
        } else {
            updateMode(id)
        }
        initCalendar(textViewDate)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            cameraxHelper.start()
            cameraxHelper.changeCamera()
            buttonPhoto.setOnClickListener {
                cameraxHelper.takePicture() }

        }
    }

    override fun initCalendar(textViewDate: TextView) {
        initCalendar(textViewDate, this)
    }

    fun onAdctivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_PIC_REQUEST) {
            val image: Bitmap? = data?.getParcelableExtra("data")
            containerPhoto
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TAKE_PICTURE_REQUEST && resultCode == RESULT_OK) {

            }
        }

    @SuppressLint("SetTextI18n")
    fun updateMode(id: Long) {
        val intent = Intent(this, MainActivity::class.java)
        fuelViewModel.readRefuelById(id).observe(this, { desc ->
            desc?.let {
                editTextMileage.setText(desc.mileage.toString())
                editTextVolume.setText(desc.title)
                editTextCost.setText(desc.totalSum.toString())
                textViewDate.text = desc.date
                description.text = desc.description
            }
        })



        buttonSave.setOnClickListener { startActivity(intent) }
    }

    override fun putData() {
        val mileage: String = editTextMileage.text.toString()
        val volume: String = editTextVolume.text.toString()
        val cost: String = editTextCost.text.toString()
        val intent = Intent(this, MainActivity::class.java)
        if (mileage.isNotEmpty() && volume.isNotEmpty() && cost.isNotEmpty()) {

            val refuel = Refuel(
                id = 0,
                title = "Заправка",
                mileage = mileage.toInt(),
                volume = volume.toInt(),
                totalSum = cost.toDouble(),
                date = textViewDate.text.toString(),
                icon = R.drawable.fuel_icon,
                description = "Данные выбранной заправки:"
            )
            fuelViewModel.addRefuel(refuel)
            startActivity(intent)

        } else {
            if (mileage.isEmpty()) {
                showToast("Пожалуйста, добавьте текущее значение пробега", editTextMileage)
            }
            if (cost.isEmpty()) {
                showToast("Пожалуйста, укажите стоимость текущей заправки", editTextCost)
            }
            if (volume.isEmpty()) {
                showToast("Пожалуйста, добавьте обьем заправленного топлива", editTextVolume)
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

