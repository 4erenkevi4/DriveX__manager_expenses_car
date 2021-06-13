package com.example.drivex.presentation.ui.notification

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.drivex.R
import com.example.drivex.utils.Constans.NOTIFY1
import com.example.drivex.utils.Constans.NOTIFY10
import com.example.drivex.utils.Constans.NOTIFY11
import com.example.drivex.utils.Constans.NOTIFY12
import com.example.drivex.utils.Constans.NOTIFY2
import com.example.drivex.utils.Constans.NOTIFY3
import com.example.drivex.utils.Constans.NOTIFY4
import com.example.drivex.utils.Constans.NOTIFY5
import com.example.drivex.utils.Constans.NOTIFY6
import com.example.drivex.utils.Constans.NOTIFY7
import com.example.drivex.utils.Constans.NOTIFY8
import com.example.drivex.utils.Constans.NOTIFY9
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.nightonke.boommenu.BoomMenuButton
import java.util.*

class NotificationFragment : Fragment() {
    lateinit var settime: FloatingActionButton
    val calendar = Calendar.getInstance()
    lateinit var editTextDesc: EditText
    lateinit var textNotification: TextView
    private lateinit var boomMenu: BoomMenuButton
    private lateinit var notificationViewModel: NotificationViewModel
    private lateinit var buttonTO: ImageView
    private lateinit var buttonDrive: ImageView
    private lateinit var buttonWash: ImageView
    private lateinit var buttonPitstop: ImageView
    private lateinit var buttonDiagnostic: ImageView
    private lateinit var buttonStrah: ImageView
    private lateinit var buttonLicense: ImageView
    private lateinit var buttonOil: ImageView
    private lateinit var buttonPay: ImageView
    private lateinit var buttonTexosm: ImageView
    private lateinit var buttonShopping: ImageView
    private lateinit var buttonOther: ImageView
    private var titleOfNotification: String = "Напоминание"
    lateinit var listButton: ArrayList<ImageView>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notificationViewModel = ViewModelProvider(this).get(NotificationViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_notification, container, false)
        val animForFab = AnimationUtils.loadAnimation(context, R.anim.fab_button)
        settime = root.findViewById(R.id.settime)
        boomMenu = requireActivity().findViewById(R.id.boom_menu)
        buttonTO = root.findViewById(R.id.notify_to)
        buttonDrive = root.findViewById(R.id.notify_drive)
        buttonWash = root.findViewById(R.id.notify_wash)
        buttonPitstop = root.findViewById(R.id.notify_pitstop)
        buttonDiagnostic = root.findViewById(R.id.notify_diagnost)
        buttonStrah = root.findViewById(R.id.notify_strah)
        buttonLicense = root.findViewById(R.id.notify_license)
        buttonTexosm = root.findViewById(R.id.notify_texosmotr)
        buttonShopping = root.findViewById(R.id.notify_shopping)
        buttonOther = root.findViewById(R.id.notify_other)
        buttonOil = root.findViewById(R.id.notify_oil)
        buttonPay = root.findViewById(R.id.notify_pay)
        editTextDesc = root.findViewById(R.id.editText_desc_of_notification)
        boomMenu.visibility = View.INVISIBLE
        settime.setOnClickListener {
            settime.startAnimation(animForFab)
            openDatePickerDialog()
        }
        setTitleOfNotify()
        return root
    }

    private fun setTitleOfNotify() {


        listButton = arrayListOf(
            buttonTO,
            buttonDiagnostic,
            buttonDrive,
            buttonLicense,
            buttonOil,
            buttonOther,
            buttonPay,
            buttonPitstop,
            buttonPitstop,
            buttonStrah,
            buttonTexosm,
            buttonWash,
        )

        buttonTO.setOnClickListener {
            onClick(buttonTO, NOTIFY1)
        }
        buttonDrive.setOnClickListener {
            onClick(buttonDrive, NOTIFY2)
        }
        buttonWash.setOnClickListener {
            onClick(buttonWash, NOTIFY3)
        }
        buttonPitstop.setOnClickListener {
            onClick(buttonWash, NOTIFY4)
        }
        buttonDiagnostic.setOnClickListener {
            onClick(buttonDiagnostic, NOTIFY5)
        }
        buttonStrah.setOnClickListener {
            onClick(buttonStrah, NOTIFY6)
        }
        buttonLicense.setOnClickListener {
            onClick(buttonLicense, NOTIFY7)
        }
        buttonOil.setOnClickListener {
            onClick(buttonOil, NOTIFY8)
        }
        buttonPay.setOnClickListener {
            onClick(buttonPay, NOTIFY9)
        }
        buttonTexosm.setOnClickListener {
            onClick(buttonTexosm, NOTIFY10)
        }
        buttonShopping.setOnClickListener {
            onClick(buttonShopping, NOTIFY11)
        }
        buttonOther.setOnClickListener {
            onClick(buttonOther, NOTIFY12)
        }
    }

    private fun onClick(button: ImageView, title: String) {
        val animForButton = AnimationUtils.loadAnimation(context, R.anim.fab_go)
        titleOfNotification = title
        button.startAnimation(animForButton)
        listButton.forEach { it.isClickable = false }
        listButton.forEach { it.isFocusable = false }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun startAlarm(calendar: Calendar) {
        val alarmManager = activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        var intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra("Title", titleOfNotification)
        intent.putExtra("Description", editTextDesc.text.toString())
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun openDatePickerDialog() {
        val dateSetListener = DatePickerDialog(
            requireContext(),
            dateSetListener,
            calendar[Calendar.YEAR],
            calendar[Calendar.MONTH],
            calendar[Calendar.DAY_OF_MONTH]
        )
        dateSetListener.setTitle("Выберите дату напоминания")
        dateSetListener.show()
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun openTimePickerDialog() {
        val timePickerDialog = TimePickerDialog(
            context,
            onTimeSetListener,
            calendar[Calendar.HOUR_OF_DAY],
            calendar[Calendar.MINUTE],
            true
        )
        timePickerDialog.setTitle("Выберите время напоминания")
        timePickerDialog.show()
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private val dateSetListener =
        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            openTimePickerDialog()
        }
    private var onTimeSetListener = TimePickerDialog.OnTimeSetListener { view, hour, minute ->
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)
       Toast.makeText(context,"Создано напоминание: $titleOfNotification",Toast.LENGTH_SHORT).show()
        listButton.forEach { it.clearAnimation() }
        startAlarm(calendar)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        boomMenu.visibility = View.VISIBLE
        listButton.forEach { it.isClickable = true }
        listButton.forEach { it.isFocusable = true }
    }
}