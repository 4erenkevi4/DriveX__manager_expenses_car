package com.example.drivex.presentation.ui.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.example.drivex.R
import java.io.File
import java.nio.ByteBuffer
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class CameraActivity : AbstractActivity() {

    private lateinit var buttonPhoto: ImageView
    private lateinit var backButton: ImageView
    private lateinit var previewView: PreviewView
    private lateinit var outputDirectory: File
    private var cameraControl: CameraControl? = null
    var imagePreview: Preview? = null
    var imageAnalysis: ImageAnalysis? = null
    val cameraExecutor = Executors.newSingleThreadExecutor()
    var imageCapture: ImageCapture? = null
    private var linearZoom = 0f

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

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun startCamera(previewView: PreviewView) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        val cameraSelector =
            CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()
        cameraProviderFuture.addListener({
            imagePreview = Preview.Builder().apply {
                setTargetAspectRatio(AspectRatio.RATIO_16_9)
                setTargetRotation(previewView.display.rotation)
            }.build()
            imageAnalysis = ImageAnalysis.Builder().apply {
                setImageQueueDepth(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            }.build()
            imageAnalysis?.setAnalyzer(cameraExecutor, CameraActivity.LuminosityAnalyzer())

            imageCapture = ImageCapture.Builder().apply {
                setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                setFlashMode(ImageCapture.FLASH_MODE_AUTO)
            }.build()

            val cameraProvider = cameraProviderFuture.get()
            val camera = cameraProvider.bindToLifecycle(
                this,
                cameraSelector,
                imagePreview,
                // imageAnalysis,
                imageCapture
            )

            previewView.implementationMode = PreviewView.ImplementationMode.COMPATIBLE
            imagePreview?.setSurfaceProvider(previewView.surfaceProvider)
            cameraControl = camera.cameraControl
        }, ContextCompat.getMainExecutor(this))
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun takePicture(previewView: PreviewView, intent: Intent) {
        val file = createFile(
            outputDirectory,
            FILENAME,
            PHOTO_EXTENSION
        )
        val outputFileOptions = ImageCapture.OutputFileOptions.Builder(file).build()
        imageCapture?.takePicture(
            outputFileOptions,
            cameraExecutor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val msg = "Photo capture succeeded: ${file.absolutePath}"
                    previewView.post {
                        Toast.makeText(baseContext, msg, Toast.LENGTH_LONG).show()
                    }
                    intent.putExtra(URI_PHOTO, file.absoluteFile.toString())
                    this@CameraActivity.setResult(0, intent)
                    this@CameraActivity.finish()
                }

                override fun onError(exception: ImageCaptureException) {
                    val msg = "Photo capture failed: ${exception.message}"
                    previewView.post {
                        Toast.makeText(baseContext, msg, Toast.LENGTH_LONG).show()
                    }
                }
            })
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun createFile(baseFolder: File, format: String, extension: String) =
        File(
            baseFolder, SimpleDateFormat(format, Locale.US)
                .format(System.currentTimeMillis()) + extension
        )


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    // Manage camera Zoom
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return when (keyCode) {
            KeyEvent.KEYCODE_VOLUME_UP -> {
                if (linearZoom <= 0.9) {
                    linearZoom += 0.1f
                }
                cameraControl?.setLinearZoom(linearZoom)
                true
            }
            KeyEvent.KEYCODE_VOLUME_DOWN -> {
                if (linearZoom >= 0.1) {
                    linearZoom -= 0.1f
                }
                cameraControl?.setLinearZoom(linearZoom)
                true
            }
            else -> super.onKeyDown(keyCode, event)
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    class LuminosityAnalyzer : ImageAnalysis.Analyzer {
        private var lastAnalyzedTimestamp = 0L

        /**
         * Helper extension function used to extract a byte array from an
         * image plane buffer
         */
        private fun ByteBuffer.toByteArray(): ByteArray {
            rewind()    // Rewind the buffer to zero
            val data = ByteArray(remaining())
            get(data)   // Copy the buffer into a byte array
            return data // Return the byte array
        }

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun analyze(image: ImageProxy) {
            image.imageInfo.rotationDegrees
            val currentTimestamp = System.currentTimeMillis()
            // Calculate the average luma no more often than every second
            if (currentTimestamp - lastAnalyzedTimestamp >=
                TimeUnit.SECONDS.toMillis(1)
            ) {
                // Since format in ImageAnalysis is YUV, image.planes[0]
                // contains the Y (luminance) plane
                val buffer = image.planes[0].buffer
                // Extract image data from callback object
                val data = buffer.toByteArray()
                // Convert the data into an array of pixel values
                val pixels = data.map { it.toInt() and 0xFF }
                // Compute average luminance for the image
                val luma = pixels.average()
                // Log the new luma value
                Log.d("CameraXApp", "Average luminosity: $luma")
                // Update timestamp of last analyzed frame
                lastAnalyzedTimestamp = currentTimestamp
            }
            image.close()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @JvmName("getOutputDirectory1")
    fun getOutputDirectory(): File {
        // TODO: 29/01/2021 Remove externalMediaDirs (deprecated)
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, "DrivexCam").apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else filesDir
    }

    override fun initCalendar(textViewDate: TextView) {
        TODO("Not yet implemented")
    }

    override fun putData() {
        TODO("Not yet implemented")
    }


}