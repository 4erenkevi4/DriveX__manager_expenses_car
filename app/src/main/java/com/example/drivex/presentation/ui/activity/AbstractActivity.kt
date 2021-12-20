package com.example.drivex.presentation.ui.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.view.isVisible
import java.text.SimpleDateFormat
import java.util.*


abstract class AbstractActivity : AppCompatActivity(), ScreenManager {

    var uriPhoto: String? = null

    companion object {
        private const val TAG = "MainActivity"
        const val URI_PHOTO = "uriCurrentPhoto"
        const val FILENAME = "yyyy-MM-dd-HH-mm-ss-SSS"
        const val PHOTO_EXTENSION = ".jpg"
        const val FUEL_ACTIVITY_PACKAGE = "presentation.ui.activity.FuelActivity"
        const val SERVICE_ACTIVITY_PACKAGE = "presentation.ui.activity.ServiceActivity"

        @SuppressLint("SimpleDateFormat")
        fun initCalendar(textViewDate: TextView, context: Context) {
            textViewDate.setOnClickListener {
                textViewDate.text =
                    SimpleDateFormat("dd.MM.yyyy").format(System.currentTimeMillis())
                val cal = Calendar.getInstance()
                val dateSetListener =
                    DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                        cal.set(Calendar.YEAR, year)
                        cal.set(Calendar.MONTH, monthOfYear)
                        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        val myFormat = "dd.MM.yyyy"
                        val sdf = SimpleDateFormat(myFormat, Locale.US)
                        textViewDate.text = sdf.format(cal.time)
                    }
                textViewDate.setOnClickListener {
                    DatePickerDialog(
                        context, dateSetListener,
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH)
                    ).show()
                }
            }
        }
    }

    fun initCamera(
        containerPhoto: ImageView,
        buttonPhoto: ImageView,
        recyclerView: View? = null,
        descRecyclerView: View? = null,
    ) {

        //uriPhoto = intent?.getStringExtra(URI_PHOTO)
        if (uriPhoto != null) {
            containerPhoto.isVisible = true
            containerPhoto.setImageURI(uriPhoto!!.toUri())
        }
        val intentCamera = Intent(this, CameraActivity::class.java)
        intentCamera.putExtra("Activity", localClassName)
        buttonPhoto.setOnClickListener {
            if (recyclerView !== null && descRecyclerView !== null) {
                recyclerView.isVisible = false
                descRecyclerView.isVisible = false
            }
            startActivityForResult(intentCamera, 0)
        }
    }

    override fun initSaveButton(view: View) {
        view.setOnClickListener {
            putData()
        }
    }

    override fun showToast(text: String, view: View) {
        view.setBackgroundColor(Color.RED)
        Toast.makeText(
            this,
            text,
            Toast.LENGTH_SHORT
        ).show()
    }
}



