package com.example.drivex.presentation.ui.activity

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.animation.doOnEnd
import androidx.core.net.toUri
import androidx.core.view.isVisible
import com.example.drivex.R
import com.example.drivex.utils.Constans.PAYMENT
import com.example.drivex.utils.Constans.REFUEL
import com.example.drivex.utils.Constans.SERVICE
import com.example.drivex.utils.Constans.SHOPPING
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
    }

    @SuppressLint("SimpleDateFormat")
    fun initCalendar(textViewDate: TextView, context: Context) {
        textViewDate.text =
            SimpleDateFormat("yyyy-MM-dd HH:mm").format(System.currentTimeMillis())
        textViewDate.setOnClickListener {
            val cal = Calendar.getInstance()
            val dateSetListener =
                DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, monthOfYear)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    val timeSetListener =
                        TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                            cal.set(Calendar.HOUR_OF_DAY, hour)
                            cal.set(Calendar.MINUTE, minute)

                            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)
                            textViewDate.text = sdf.format(cal.timeInMillis)
                        }
                    TimePickerDialog(
                        context,
                        timeSetListener,
                        cal.get(Calendar.HOUR_OF_DAY),
                        cal.get(Calendar.MINUTE),
                        true
                    ).show()
                }

            DatePickerDialog(
                context, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    fun initCamera(
        containerPhoto: ImageView,
        buttonPhoto: View,
        recyclerView: View? = null,
        descRecyclerView: View? = null,
    ) {
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
        startErrorRotateAnimation(view)
        Toast.makeText(
            this,
            text,
            Toast.LENGTH_SHORT
        ).show()
    }

    fun setToolbar(
        toolbar: Toolbar?,
        textTitleResID: Int?,
        isBackButtonEnabled: Boolean = false
    ) {
        this.setSupportActionBar(toolbar)
        this.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        textTitleResID?.let { toolbar?.setTitle(it) }
        toolbar?.setTitleTextColor(Color.WHITE)
        if (isBackButtonEnabled)
            toolbar?.setNavigationOnClickListener { this.onBackPressed() }
    }

    private fun startErrorRotateAnimation(view: View) {
        val rotateAnimator = ObjectAnimator.ofFloat(view, "rotation", 0f, 10f)
        rotateAnimator.run {
            this.repeatCount = 2
            this.duration = 70
            this.start()
            this.doOnEnd {
                ObjectAnimator.ofFloat(view, "rotation", 10f, 0f).start()
            }
        }
    }

    private fun startSuccecAnimation(view: View) {
        val pulseAnimator = ObjectAnimator.ofFloat(view, "rotation", 0f, 30f)
        pulseAnimator.run {
            this.repeatCount = 1
            this.duration = 200
            this.start()
            this.doOnEnd {
                ObjectAnimator.ofFloat(view, "rotation", 30f, 0f).start()
            }
        }
    }

}




