package com.example.drivex.presentation.ui.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Camera
import android.graphics.Color
import android.hardware.Camera.*
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.view.PreviewView
import com.example.drivex.utils.CameraxHelper
import timber.log.Timber
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


abstract class AbstractActivity: AppCompatActivity(), ScreenManager {
    private val CAMERA_PIC_REQUEST = 2

    companion object {
        const val TAKE_PICTURE_REQUEST = 1
        var outputFileUri: Uri? = null

        @SuppressLint("SimpleDateFormat")
         fun initCalendar(textViewDate: TextView,context: Context) {
            textViewDate.setOnClickListener {
                textViewDate.text =
                    SimpleDateFormat("dd.MM.yyyy").format(System.currentTimeMillis())
                val cal = Calendar.getInstance()
                val dateSetListener =
                    DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
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


    override fun initPhotoButton(view: View) {
        view.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, CAMERA_PIC_REQUEST)
        }
    }

     @RequiresApi(Build.VERSION_CODES.R)
     override fun saveFullImage(view: View) {
         val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
         val file = File(
             Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
             "test.jpg"
         )
         //outputFileUri = Uri.fromFile(file)
         // intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri)

     }
    fun makePhoto(view: View?) {

    }



    override fun initSaveButton(view: View) {
        view.setOnClickListener {
            putData()
        }
    }

    override fun showToast(text: String,view: View ) {
        view.setBackgroundColor(Color.RED)
        Toast.makeText(
            this,
            text,
            Toast.LENGTH_SHORT
        ).show()
    }
}



