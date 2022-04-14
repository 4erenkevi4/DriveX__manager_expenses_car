package com.cherenkevich.drivex.presentation.ui.notification

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
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.cherenkevich.drivex.presentation.ui.notification.NotificationViewModel
import com.cherenkevich.drivex.R
import com.cherenkevich.drivex.presentation.ui.fragments.AbstractFragment
import com.cherenkevich.drivex.utils.Constans.NOTIFY1
import com.cherenkevich.drivex.utils.Constans.NOTIFY10
import com.cherenkevich.drivex.utils.Constans.NOTIFY11
import com.cherenkevich.drivex.utils.Constans.NOTIFY12
import com.cherenkevich.drivex.utils.Constans.NOTIFY2
import com.cherenkevich.drivex.utils.Constans.NOTIFY3
import com.cherenkevich.drivex.utils.Constans.NOTIFY4
import com.cherenkevich.drivex.utils.Constans.NOTIFY5
import com.cherenkevich.drivex.utils.Constans.NOTIFY6
import com.cherenkevich.drivex.utils.Constans.NOTIFY7
import com.cherenkevich.drivex.utils.Constans.NOTIFY8
import com.cherenkevich.drivex.utils.Constans.NOTIFY9
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*


open class NotificationFragment : AbstractFragment() {
    val calendar = Calendar.getInstance()
    lateinit var editTextDesc: EditText
    private var isCalendarSettUp: Boolean = false
    private lateinit var startNotificationButton: FloatingActionButton
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
    private lateinit var titleOfNotification: String
    private var notifyId: Int = 1
    lateinit var listButtons: ArrayList<ImageView>
    private lateinit var toolbarNotify: Toolbar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notificationViewModel = ViewModelProvider(this).get(NotificationViewModel::class.java)
        return inflater.inflate(R.layout.fragment_notification, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        titleOfNotification = getString(R.string.reminderGeneral)
        buttonTO = view.findViewById(R.id.notify_to)
        buttonDrive = view.findViewById(R.id.notify_drive)
        buttonWash = view.findViewById(R.id.notify_wash)
        buttonPitstop = view.findViewById(R.id.notify_pitstop)
        buttonDiagnostic = view.findViewById(R.id.notify_diagnost)
        buttonStrah = view.findViewById(R.id.notify_strah)
        startNotificationButton = view.findViewById(R.id.settime)
        buttonLicense = view.findViewById(R.id.notify_license)
        buttonTexosm = view.findViewById(R.id.notify_texosmotr)
        buttonShopping = view.findViewById(R.id.notify_shopping)
        buttonOther = view.findViewById(R.id.notify_other)
        buttonOil = view.findViewById(R.id.notify_oil)
        buttonPay = view.findViewById(R.id.notify_pay)
        editTextDesc = view.findViewById(R.id.editText_desc_of_notification)
        toolbarNotify = view.findViewById(R.id.main_toolbar)
        toolbarNotify.setBackgroundColor(resources.getColor(R.color.toolbar_background3))
        setFloatingMenuVisibility(false)
        setTitleOfNotify()
        setToolbar(toolbarNotify, R.string.menu_notify)
    }

    private fun setTitleOfNotify() {
        listButtons = arrayListOf(
            buttonTO,
            buttonDrive,
            buttonWash,
            buttonPitstop,
            buttonDiagnostic,
            buttonStrah,
            buttonLicense,
            buttonOil,
            buttonTexosm,
            buttonPay,
            buttonShopping,
            buttonOther
        )

        buttonTO.setOnClickListener {
            onClick(it, NOTIFY1, 1)
        }
        buttonDrive.setOnClickListener {
            onClick(it, NOTIFY2, 2)
        }
        buttonWash.setOnClickListener {
            onClick(it, NOTIFY3, 3)
        }
        buttonPitstop.setOnClickListener {
            onClick(it, NOTIFY4, 4)
        }
        buttonDiagnostic.setOnClickListener {
            onClick(it, NOTIFY5, 5)
        }
        buttonStrah.setOnClickListener {
            onClick(it, NOTIFY6, 6)
        }
        buttonLicense.setOnClickListener {
            onClick(it, NOTIFY7, 7)
        }
        buttonOil.setOnClickListener {
            onClick(it, NOTIFY8, 8)
        }
        buttonPay.setOnClickListener {
            onClick(it, NOTIFY9, 9)
        }
        buttonTexosm.setOnClickListener {
            onClick(it, NOTIFY10, 10)
        }
        buttonShopping.setOnClickListener {
            onClick(it, NOTIFY11, 11)
        }
        buttonOther.setOnClickListener {
            onClick(it, NOTIFY12, 12)
        }
        startNotificationButton.setOnClickListener {
            if (isCalendarSettUp)
                startNotification()
            else
                openDatePickerDialog()

        }
    }

    private fun onClick(button: View, title: String, notificationId: Int) {
        listButtons.forEach { it.clearAnimation()
        }
        val animForButton = AnimationUtils.loadAnimation(context, R.anim.fab_go)
        titleOfNotification = title
        notifyId = notificationId
        button.startAnimation(animForButton)
        isCalendarSettUp = false
    }

    private fun openDatePickerDialog() {
        val dateSetListener = DatePickerDialog(
            requireContext(),
            dateSetListener,
            calendar[Calendar.YEAR],
            calendar[Calendar.MONTH],
            calendar[Calendar.DAY_OF_MONTH]
        )
        dateSetListener.setTitle(getString(R.string.dateOfReminder))
        dateSetListener.show()
    }

    private fun openTimePickerDialog() {
        val timePickerDialog = TimePickerDialog(
            context,
            onTimeSetListener,
            calendar[Calendar.HOUR_OF_DAY],
            calendar[Calendar.MINUTE],
            true
        )
        timePickerDialog.setTitle(getString(R.string.timeOfReminder))
        timePickerDialog.show()
    }

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
        if (editTextDesc.text.isEmpty()) {
            isCalendarSettUp = true
            openDialog()
        } else {
            startNotification()
        }
    }


    private fun startNotification() {
        isCalendarSettUp = false
        Toast.makeText(context, "${getString(R.string.creationReminder)} $titleOfNotification", Toast.LENGTH_SHORT)
            .show()
        notificationViewModel.makeActiveButtons(listButtons)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            notificationViewModel.startAlarm(
                requireContext(),
                calendar,
                notifyId,
                titleOfNotification,
                editTextDesc.text.toString()
            )
        }
    }

    private fun openDialog() {
        val myDialogFragment =
            MyDialogFragment(
                calendar,
                notifyId,
                titleOfNotification,
                editTextDesc.text.toString(),
                listButtons,
                startNotificationButton
            )
        val manager = childFragmentManager
        myDialogFragment.show(manager, "dialog")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        listButtons.forEach { it.isClickable = true }
        listButtons.forEach { it.isFocusable = true }
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
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        builder.setTitle(getString(R.string.continieReminder))
        builder.setMessage(getString(R.string.emptyDescriptionOfReminder))
        builder.setPositiveButton(
            getString(R.string.accept_general)
        ) { dialog, id ->
            notificationViewModel.makeActiveButtons(listButton)
            Toast.makeText(
                activity, getString(R.string.creationReminder),
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
            getString(R.string.cancel_general)
        ) { dialog, id ->
            startNotificationButton.isVisible = true
            Toast.makeText(activity, getString(R.string.enterDescriptionOfReminder), Toast.LENGTH_SHORT)
                .show()
        }
        builder.setCancelable(true)
        return builder.create()
    }
}

