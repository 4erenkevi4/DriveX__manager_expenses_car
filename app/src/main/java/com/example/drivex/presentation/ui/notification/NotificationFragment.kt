package com.example.drivex.presentation.ui.notification

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
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


open class NotificationFragment : Fragment() {
    val calendar = Calendar.getInstance()
    lateinit var editTextDesc: EditText
    private var isCalendarSettUp: Boolean = false
    private lateinit var startNotificationButton: FloatingActionButton
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
    private var notifyId: Int = 1
    lateinit var listButton: ArrayList<ImageView>

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notificationViewModel = ViewModelProvider(this).get(NotificationViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_notification, container, false)
        boomMenu = requireActivity().findViewById(R.id.boom_menu)
        buttonTO = root.findViewById(R.id.notify_to)
        buttonDrive = root.findViewById(R.id.notify_drive)
        buttonWash = root.findViewById(R.id.notify_wash)
        buttonPitstop = root.findViewById(R.id.notify_pitstop)
        buttonDiagnostic = root.findViewById(R.id.notify_diagnost)
        buttonStrah = root.findViewById(R.id.notify_strah)
        startNotificationButton = root.findViewById(R.id.settime)
        buttonLicense = root.findViewById(R.id.notify_license)
        buttonTexosm = root.findViewById(R.id.notify_texosmotr)
        buttonShopping = root.findViewById(R.id.notify_shopping)
        buttonOther = root.findViewById(R.id.notify_other)
        buttonOil = root.findViewById(R.id.notify_oil)
        buttonPay = root.findViewById(R.id.notify_pay)
        editTextDesc = root.findViewById(R.id.editText_desc_of_notification)
        boomMenu.visibility = View.INVISIBLE
        boomMenu.isVisible = false
        setTitleOfNotify()
        return root
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
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
            onClick(buttonTO, NOTIFY1, 1)
        }
        buttonDrive.setOnClickListener {
            onClick(buttonDrive, NOTIFY2, 2)
        }
        buttonWash.setOnClickListener {
            onClick(buttonWash, NOTIFY3, 3)
        }
        buttonPitstop.setOnClickListener {
            onClick(buttonWash, NOTIFY4, 4)
        }
        buttonDiagnostic.setOnClickListener {
            onClick(buttonDiagnostic, NOTIFY5, 5)
        }
        buttonStrah.setOnClickListener {
            onClick(buttonStrah, NOTIFY6, 6)
        }
        buttonLicense.setOnClickListener {
            onClick(buttonLicense, NOTIFY7, 7)
        }
        buttonOil.setOnClickListener {
            onClick(buttonOil, NOTIFY8, 8)
        }
        buttonPay.setOnClickListener {
            onClick(buttonPay, NOTIFY9, 9)
        }
        buttonTexosm.setOnClickListener {
            onClick(buttonTexosm, NOTIFY10, 10)
        }
        buttonShopping.setOnClickListener {
            onClick(buttonShopping, NOTIFY11, 11)
        }
        buttonOther.setOnClickListener {
            onClick(buttonOther, NOTIFY12, 12)
        }
        startNotificationButton.setOnClickListener {
            if (isCalendarSettUp)
                startNotification()
            else
                openDatePickerDialog()
            
        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun onClick(button: ImageView, title: String, notificationId: Int) {
        listButton.forEach { it.clearAnimation() }
        val animForButton = AnimationUtils.loadAnimation(context, R.anim.fab_go)
        titleOfNotification = title
        notifyId = notificationId
        button.startAnimation(animForButton)
        isCalendarSettUp = false
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

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private var onTimeSetListener = TimePickerDialog.OnTimeSetListener { view, hour, minute ->
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)
        if (editTextDesc.text.isEmpty()) {
            isCalendarSettUp = true
            openDialog()
        } else {
            startNotification()
        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun startNotification() {
        isCalendarSettUp = false
        Toast.makeText(context, "Создано напоминание: $titleOfNotification", Toast.LENGTH_SHORT)
            .show()
        notificationViewModel.makeActiveButtons(listButton)
        notificationViewModel.startAlarm(
            requireContext(),
            calendar,
            notifyId,
            titleOfNotification,
            editTextDesc.text.toString()
        )
    }

    private fun openDialog() {
        val myDialogFragment =
            MyDialogFragment(
                calendar,
                notifyId,
                titleOfNotification,
                editTextDesc.text.toString(),
                listButton,
                startNotificationButton
            )
        val manager = childFragmentManager
        myDialogFragment.show(manager, "dialog")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        boomMenu.visibility = View.VISIBLE
        listButton.forEach { it.isClickable = true }
        listButton.forEach { it.isFocusable = true }
    }
}

class MyDialogFragment(
    private val calendar: Calendar,
    private val notifyId: Int,
    private val titleOfNotification: String,
    private val editTextDesc: String,
    private val listButton: ArrayList<ImageView>,
    private val startNotificationButton: FloatingActionButton
) : DialogFragment() {
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val notificationViewModel = ViewModelProvider(this).get(NotificationViewModel::class.java)
        val title = "Продолжить без описания напоминания?"
        val message =
            "Вы не заполнили описание напоминания. Создать уведомление без описания, или добавить подробности? "
        val button1String = "Продолжить"
        val button2String = "Добавить"
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        builder.setTitle(title) // заголовок
        builder.setMessage(message) // сообщение
        builder.setPositiveButton(
            button1String
        ) { dialog, id ->
            notificationViewModel.makeActiveButtons(listButton)
            Toast.makeText(
                activity, "Создано напоминание: ",
                Toast.LENGTH_SHORT
            ).show()

            notificationViewModel.startAlarm(
                requireContext(),
                calendar,
                notifyId,
                titleOfNotification,
                editTextDesc
            )
        }
        builder.setNegativeButton(
            button2String
        ) { dialog, id ->
            startNotificationButton.isVisible = true
            Toast.makeText(activity, "Введите описание напоминания", Toast.LENGTH_SHORT)
                .show()
        }
        builder.setCancelable(true)
        return builder.create()
    }
}

