package com.example.drivex.presentation.ui.dialogs

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.example.drivex.R


class SettingsDialog(var arrayItems: Array<String>, val type : String) : DialogFragment() {

    companion object{
        const val TYPE_VOLUME= "volume"
        const val TYPE_CONSUMPTION= "consumption"
        const val TYPE_DISTANCE= "distance"
        const val TYPE_CURENCY= "currency"
        const val TYPE_SOUND= "sound"
        const val TYPE_CAR= "car"
        const val TYPE_AVATAR= "avatar"
    }

    val prefs: SharedPreferences? = context?.getSharedPreferences(
        "com.drivex.app", Context.MODE_PRIVATE
    )

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(R.string.select_unit_settings)
                .setSingleChoiceItems(
                    arrayItems, -1
                ) { _, item ->
                    parentFragmentManager.setFragmentResult("requestKey", bundleOf(type to arrayItems[item]))
                    Toast.makeText(
                        activity, "${R.string.selected_general} ${arrayItems[item]}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .setPositiveButton(
                    "OK"
                ) { _, id ->
                    // User clicked OK, so save the selectedItems results somewhere
                    // or return them to the component that opened the dialog
                }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}