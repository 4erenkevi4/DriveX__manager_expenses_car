package com.example.drivex.presentation.ui.setting

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.*
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.widget.Toolbar
import androidx.core.animation.doOnCancel
import androidx.core.animation.doOnEnd
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.example.drivex.R
import com.example.drivex.presentation.ui.activity.viewModels.AbstractViewModel
import com.example.drivex.presentation.ui.dialogs.SettingsDialog
import com.example.drivex.presentation.ui.dialogs.SettingsDialog.Companion.TYPE_AVATAR
import com.example.drivex.presentation.ui.dialogs.SettingsDialog.Companion.TYPE_CAR
import com.example.drivex.presentation.ui.dialogs.SettingsDialog.Companion.TYPE_CONSUMPTION
import com.example.drivex.presentation.ui.dialogs.SettingsDialog.Companion.TYPE_CURENCY
import com.example.drivex.presentation.ui.dialogs.SettingsDialog.Companion.TYPE_DISTANCE
import com.example.drivex.presentation.ui.dialogs.SettingsDialog.Companion.TYPE_OFF_SITY_CONSUMPTION
import com.example.drivex.presentation.ui.dialogs.SettingsDialog.Companion.TYPE_SITY_CONSUMPTION
import com.example.drivex.presentation.ui.dialogs.SettingsDialog.Companion.TYPE_SOUND
import com.example.drivex.presentation.ui.dialogs.SettingsDialog.Companion.TYPE_VOLUME
import com.example.drivex.presentation.ui.fragments.AbstractFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_setting.*
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class SettingFragment : AbstractFragment() {

    companion object {
        const val APP_PREFERENCES = "com.drivex.app"
    }

    private lateinit var abstractViewModel: AbstractViewModel
    private lateinit var settingViewModel: SettingViewModel
    private lateinit var carVendor: Spinner
    private lateinit var carModel: EditText
    private lateinit var soundStartApp: SwitchCompat
    private lateinit var utilsVolume: LinearLayout
    private lateinit var utilsFuel: LinearLayout
    private lateinit var utilsCurrency: LinearLayout
    private lateinit var utilsDistance: LinearLayout
    private lateinit var currency: TextView
    private lateinit var volume: TextView
    private lateinit var consumption: TextView
    private lateinit var distance: TextView
    private lateinit var buttonSave: ImageView
    private lateinit var buttonSetAvatar: ImageView
    private lateinit var toolbarSetting: Toolbar
    private lateinit var consSityValue: EditText
    private lateinit var consOffSityValue: EditText
    private lateinit var saveBtnConsOffSity: ImageButton
    private lateinit var saveBtnConsSity: ImageButton

    @Inject
    lateinit var prefs: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        settingViewModel = ViewModelProvider(this).get(SettingViewModel::class.java)
        abstractViewModel = ViewModelProvider(this).get(AbstractViewModel::class.java)
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // setToolbar(false)
        setFloatingMenuVisibility(false)
        consSityValue = view.findViewById(R.id.sity_consumption_value)
        consOffSityValue = view.findViewById(R.id.offsity_consumption_value)
        saveBtnConsOffSity = view.findViewById(R.id.save_btn_cons_off_sity)
        saveBtnConsSity = view.findViewById(R.id.save_btn_cons_sity)
        carVendor = view.findViewById(R.id.car_vendor)
        carModel = view.findViewById(R.id.Car_model)
        soundStartApp = view.findViewById(R.id.switch_sound)
        utilsVolume = view.findViewById(R.id.volume_picker_setting)
        utilsFuel = view.findViewById(R.id.fuel_picker_setting)
        utilsCurrency = view.findViewById(R.id.currency_picker_setting)
        utilsDistance = view.findViewById(R.id.distance_picker_setting)
        currency = utilsCurrency.findViewById(R.id.curency)
        volume = utilsVolume.findViewById(R.id.volume)
        consumption = utilsFuel.findViewById(R.id.fuel_settings)
        distance = utilsDistance.findViewById(R.id.dist)
        buttonSave = view.findViewById(R.id.button_save_car)
        buttonSetAvatar = view.findViewById(R.id.set_avatar_button)
        toolbarSetting = view.findViewById(R.id.back_toolbar)
        toolbarSetting.setTitle(R.string.menu_setting)
        toolbarSetting.setTitleTextColor(Color.WHITE)
        setToolbar(toolbarSetting, R.string.menu_setting, isBackButtonEnabled = true)
        applySettingsToSP(prefs)
        buttonSetAvatar.setOnClickListener {
            val photoPickerintent = Intent(Intent.ACTION_PICK)
            photoPickerintent.type = "image/*"
            requireActivity().startActivityForResult(photoPickerintent, 3)
        }
        if (arguments?.containsKey("IMAGE_URI") == true) {
            imageCarUri = requireArguments().get("IMAGE_URI") as Uri?
            abstractViewModel.saveToSP(string = imageCarUri.toString(), TYPE_AVATAR, prefs = prefs)
            buttonSetAvatar.setImageBitmap(getCroppedBitmap(imageCarUri!!))
        }

        soundStartApp.setOnCheckedChangeListener { _, isChecked ->
            abstractViewModel.saveToSP(keyType = TYPE_SOUND, value = isChecked, prefs = prefs)
        }
        carVendor.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                itemSelected: View, selectedItemPosition: Int, selectedId: Long
            ) {
                val choose = resources.getStringArray(R.array.vehicles)

                buttonSave.setOnClickListener {
                    createCarModel(choose[selectedItemPosition] + " " + carModel.text.toString())

                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        saveBtnConsOffSity.setOnClickListener {

            saveConsumptionToSP(it, consOffSityValue, TYPE_OFF_SITY_CONSUMPTION)
        }

        saveBtnConsSity.setOnClickListener {
            saveConsumptionToSP(it, consSityValue, TYPE_SITY_CONSUMPTION)
        }
        utilsVolume.setOnClickListener {
            createDialog(
                arrayOf(
                    getString(R.string.liter),
                    getString(R.string.Gallon)
                ), TYPE_VOLUME
            )
        }
        utilsDistance.setOnClickListener {
            createDialog(
                arrayOf(
                    getString(R.string.km),
                    getString(R.string.mile),
                ), TYPE_DISTANCE
            )
        }
        utilsFuel.setOnClickListener {
            createDialog(
                arrayOf(
                    getString(R.string.l_km),
                    getString(R.string.l_mile),
                    getString(R.string.g_km),
                    getString(R.string.g_mile)
                ), TYPE_CONSUMPTION
            )
        }
        utilsCurrency.setOnClickListener {
            createDialog(
                arrayOf(
                    getCurrency(Locale.getDefault()),
                    getCurrency(Locale.US),
                    getCurrency(Locale.GERMANY),
                    getCurrency(Locale.PRC),
                    getCurrency(Locale.UK)
                ), TYPE_CURENCY
            )
        }
    }

    private fun createCarModel(carVendor: String?) {
        if (carVendor.isNullOrEmpty().not() && carVendor != " ")
            abstractViewModel.saveToSP(string = carVendor, TYPE_CAR, prefs = prefs)
        buttonSave.setImageResource(R.drawable.ic_baseline_check_24)
        buttonSave.setColorFilter(Color.GREEN)
        Toast.makeText(context, "Your car $carVendor is successfully saved", Toast.LENGTH_SHORT)
            .show()
    }

    private fun saveConsumptionToSP(view: View, editText: EditText, type: String) {
        if (editText.text.isEmpty()) {
            startRotate(view)
            Toast.makeText(
                context,
                "Please add fuel consumption!",
                Toast.LENGTH_SHORT
            )
                .show()
        } else
            abstractViewModel.saveToSP(
                keyType = type,
                floatValue = editText.text.toString().toFloat(),
                prefs = prefs
            )
        editText.hint = editText.text.toString()
        editText.setText("")

    }

    private fun startRotate(view: View) {
        val rotateAnimator = ObjectAnimator.ofFloat(view, "rotation", 0f, 30f)
        rotateAnimator.run {
            this.repeatCount = 2
            this.duration = 70
            this.start()
            this.doOnEnd {
                ObjectAnimator.ofFloat(view, "rotation", 30f, 0f).start()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun applySettingsToSP(prefs: SharedPreferences) {
        if (prefs.contains(TYPE_AVATAR)) {
            prefs.getString(TYPE_AVATAR, "").also { imageCarUri = it!!.toUri() }
            buttonSetAvatar.setImageBitmap(getCroppedBitmap(imageCarUri!!))
        }
        if (prefs.contains(TYPE_VOLUME)) {
            volume.text = prefs.getString(TYPE_VOLUME, getString(R.string.volume))
        }
        if (prefs.contains(TYPE_CONSUMPTION)) {
            consumption.text = prefs.getString(TYPE_CONSUMPTION, getString(R.string.l_km))
        }
        if (prefs.contains(TYPE_CURENCY)) {
            currency.text = prefs.getString(TYPE_CURENCY, getCurrency(Locale.getDefault()))
        }
        if (prefs.contains(TYPE_SOUND)) {
            switch_sound.isChecked = prefs.getBoolean(TYPE_SOUND, false)
        }
        if (prefs.contains(TYPE_SITY_CONSUMPTION)) {
            consSityValue.hint = (prefs.getFloat(TYPE_SITY_CONSUMPTION, 0f).toString())
        }
        if (prefs.contains(TYPE_OFF_SITY_CONSUMPTION)) {
            consOffSityValue.hint =(prefs.getFloat(TYPE_OFF_SITY_CONSUMPTION, 0f).toString())
        }
        if (prefs.contains(TYPE_CAR)) {
            carModel.setText(prefs.getString(TYPE_CAR, ""))
            carVendor.isVisible = false
            buttonSave.setImageResource(R.drawable.ic_baseline_create_24)
            buttonSave.setOnClickListener {
                carVendor.isVisible = true
                buttonSave.setImageResource(R.drawable.ic_baseline_check_24)
            }
        }
    }


    override fun onStart() {
        super.onStart()
        childFragmentManager.setFragmentResultListener("requestKey", this) { key, bundle ->
            bundle.getString(TYPE_VOLUME).let {
                it?.setText(volume, TYPE_VOLUME)
            }
            bundle.getString(TYPE_CURENCY).let {
                it?.setText(currency, TYPE_CURENCY)
            }
            bundle.getString(TYPE_CONSUMPTION).let {
                it?.setText(consumption, TYPE_CONSUMPTION)
            }
            bundle.getString(TYPE_DISTANCE).let {
                it?.setText(distance, TYPE_DISTANCE)
            }
        }
    }

    private fun String.setText(textview: TextView, type: String) {
        if (this.isEmpty().not()) {
            textview.text = this
            abstractViewModel.saveToSP(string = this, keyType = type, prefs = prefs)
        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun getCurrency(locale: Locale): String {
        val currency: Currency = Currency.getInstance(locale)
        return currency.currencyCode
    }

    private fun createDialog(array: Array<String>, type: String) {
        val myDialogFragment = SettingsDialog(array, type)
        val manager: FragmentManager = childFragmentManager
        val transaction: FragmentTransaction = manager.beginTransaction()
        myDialogFragment.show(transaction, "dialog")
    }

}


