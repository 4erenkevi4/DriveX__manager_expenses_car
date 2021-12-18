package com.example.drivex.presentation.ui.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.camera.view.PreviewView
import com.example.drivex.R
import com.example.drivex.presentation.ui.activity.AbstractActivity

class CameraActivity : AbstractActivity() {

    private lateinit var buttonPhoto: ImageView
    private lateinit var backButton: ImageView
    private lateinit var previewView: PreviewView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_camera_preview)
        buttonPhoto = findViewById(R.id.take_photo_button)
        backButton = findViewById(R.id.button_back)
        previewView = findViewById(R.id.fuel_preview_view)
        val callingActivity = intent.getStringExtra("Activity")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            startCamera(previewView)
            buttonPhoto.setOnClickListener {
                when (callingActivity) {
                    FUEL_ACTIVITY_PACKAGE -> {
                        takePicture(
                            previewView,
                            Intent(this, FuelActivity::class.java)
                        )
                    }
                    SERVICE_ACTIVITY_PACKAGE -> {
                        takePicture(
                            previewView, Intent(this, ServiceActivity::class.java).putExtra(
                                "RESTART_AFTER_CAMERA",
                                true
                            )
                        )
                    }
                }

            }
            outputDirectory = getOutputDirectory()
        }
        backButton.setOnClickListener {
            onBackPressed()
        }
    }

    override fun initCalendar(textViewDate: TextView) {
        TODO("Not yet implemented")
    }

    override fun putData() {
        TODO("Not yet implemented")
    }


}