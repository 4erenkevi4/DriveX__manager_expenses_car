package com.example.drivex.presentation.ui.setting

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.example.drivex.R
import com.example.drivex.presentation.ui.dialogs.SettingsDialog
import com.example.drivex.presentation.ui.dialogs.SettingsDialog.Companion.TYPE_CONSUMPTION
import com.example.drivex.presentation.ui.dialogs.SettingsDialog.Companion.TYPE_CURENCY
import com.example.drivex.presentation.ui.dialogs.SettingsDialog.Companion.TYPE_VOLUME
import java.util.*


class SettingFragment : Fragment() {

    private lateinit var settingViewModel: SettingViewModel
    private lateinit var carVendor: Spinner
    private lateinit var carModel: EditText
    private lateinit var soundStartApp: SwitchCompat
    private lateinit var utilsVolume: LinearLayout
    private lateinit var utilsFuel: LinearLayout
    private lateinit var utilsCurrency: LinearLayout
    private lateinit var currency: TextView
    private lateinit var volume: TextView
    private lateinit var fuel_cons: TextView
    val prefs: SharedPreferences? = context?.getSharedPreferences(
        "com.drivex.app", Context.MODE_PRIVATE
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        settingViewModel = ViewModelProvider(this).get(SettingViewModel::class.java)
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        carVendor = view.findViewById(R.id.car_vendor)
        carModel = view.findViewById(R.id.Car_model)
        soundStartApp = view.findViewById(R.id.switch_sound)
        utilsVolume = view.findViewById(R.id.volume_picker_setting)
        utilsFuel = view.findViewById(R.id.fuel_picker_setting)
        utilsCurrency = view.findViewById(R.id.currency_picker_setting)
        currency = utilsCurrency.findViewById(R.id.curency)
        volume = utilsVolume.findViewById(R.id.volume)
        fuel_cons = utilsFuel.findViewById(R.id.fuel_settings)
        utilsVolume.setOnClickListener {
            createDialog(
                arrayOf(
                    getString(R.string.liter),
                    getString(R.string.Gallon)
                ), TYPE_VOLUME
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
        currency.text = getCurrency(Locale.getDefault())
        utilsCurrency.setOnClickListener {
            createDialog(
                arrayOf(
                    getCurrency(Locale.US),
                    getCurrency(Locale.GERMANY),
                    getCurrency(Locale.PRC),
                    getCurrency(Locale.UK),
                    getCurrency(Locale.getDefault())
                ), TYPE_CURENCY
            )
        }
    }

    override fun onStart() {
        super.onStart()
        childFragmentManager.setFragmentResultListener("requestKey", this) { key, bundle ->
            bundle.getString(TYPE_VOLUME).let {it?.setText(volume) }
            bundle.getString(TYPE_CURENCY).let { it?.setText(currency)
            }
            bundle.getString(TYPE_CONSUMPTION).let {it?.setText(fuel_cons) }
        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun getCurrency(locale: Locale): String {
        val currency: Currency = Currency.getInstance(locale)
        return currency.displayName + " " + currency.symbol
    }

    @SuppressLint("CommitPrefEdits")
    fun saveToSP(string: String) {
        val editor: SharedPreferences.Editor? = prefs?.edit()
        editor?.putString("VOLUME", string)
    }

    private fun createDialog(array: Array<String>, type: String) {
        val myDialogFragment = SettingsDialog(array, type)
        val manager: FragmentManager = childFragmentManager
        val transaction: FragmentTransaction = manager.beginTransaction()
        myDialogFragment.show(transaction, "dialog")
    }

}

private fun String.setText(textview: TextView) {
    if (this.isEmpty().not())
        textview.text = this
}
