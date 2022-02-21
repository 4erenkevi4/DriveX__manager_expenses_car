package com.example.drivex.presentation.ui.setting

import android.annotation.SuppressLint
import android.content.Context
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
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.example.drivex.R
import com.example.drivex.presentation.ui.dialogs.SettingsDialog
import com.example.drivex.presentation.ui.dialogs.SettingsDialog.Companion.TYPE_AVATAR
import com.example.drivex.presentation.ui.dialogs.SettingsDialog.Companion.TYPE_CAR
import com.example.drivex.presentation.ui.dialogs.SettingsDialog.Companion.TYPE_CONSUMPTION
import com.example.drivex.presentation.ui.dialogs.SettingsDialog.Companion.TYPE_CURENCY
import com.example.drivex.presentation.ui.dialogs.SettingsDialog.Companion.TYPE_DISTANCE
import com.example.drivex.presentation.ui.dialogs.SettingsDialog.Companion.TYPE_SOUND
import com.example.drivex.presentation.ui.dialogs.SettingsDialog.Companion.TYPE_VOLUME
import com.example.drivex.presentation.ui.fragments.AbstractFragment
import kotlinx.android.synthetic.main.fragment_setting.*
import java.util.*


class SettingFragment : AbstractFragment() {
    companion object {
        const val APP_PREFERENCES = "com.drivex.app"
    }
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        settingViewModel = ViewModelProvider(this).get(SettingViewModel::class.java)
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       // setToolbar(false)
        setFloatingMenuVisibility(false)
        val prefs: SharedPreferences? = context?.getSharedPreferences(
            APP_PREFERENCES, Context.MODE_PRIVATE
        )
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
        if (prefs != null)
            applySettingsToSP(prefs)
        buttonSetAvatar.setOnClickListener {
            val photoPickeIintent = Intent(Intent.ACTION_PICK)
            photoPickeIintent.type = "image/*"
            requireActivity().startActivityForResult(photoPickeIintent, 3)
        }
        if (arguments?.containsKey("IMAGE_URI") == true) {
            imageCarUri = requireArguments().get("IMAGE_URI") as Uri?
            saveToSP(imageCarUri.toString(), TYPE_AVATAR)
            buttonSetAvatar.setImageBitmap(getCroppedBitmap(imageCarUri!!))
        }

        soundStartApp.setOnCheckedChangeListener { _, isChecked ->
            saveToSP(type = TYPE_SOUND, boolean = isChecked)
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
            saveToSP(carVendor, TYPE_CAR)
        buttonSave.setImageResource(R.drawable.ic_baseline_check_24)
        buttonSave.setColorFilter(Color.GREEN)
        Toast.makeText(context, "Your car $carVendor is successfully saved", Toast.LENGTH_SHORT)
            .show()
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
            saveToSP(this, type)
        }
    }

    @SuppressLint("CommitPrefEdits")
    fun saveToSP(string: String? = null, type: String, boolean: Boolean = false) {
        val prefs: SharedPreferences? = context?.getSharedPreferences(
            APP_PREFERENCES, Context.MODE_PRIVATE
        )
        val editor: SharedPreferences.Editor? = prefs?.edit()
        if (string != null)
            editor?.putString(type, string)
        else
            editor?.putBoolean(type, boolean)
        editor?.apply()
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


