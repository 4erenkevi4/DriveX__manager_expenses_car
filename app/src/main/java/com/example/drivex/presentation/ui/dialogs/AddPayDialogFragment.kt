package com.example.drivex.presentation.ui.dialogs

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.drivex.R
import com.example.drivex.data.model.Refuel
import com.example.drivex.data.repository.RefuelRepository
import com.example.drivex.data.repository.RefuelRepositoryImpl
import com.example.drivex.presentation.ui.activity.FuelActivity
import com.example.drivex.presentation.ui.activity.MainActivity
import com.example.drivex.presentation.ui.activity.viewModels.FuelViewModel
import org.koin.core.KoinApplication.Companion.init
import org.koin.dsl.koinApplication

class AddPayDialogFragment(application: Application) : DialogFragment() {
    lateinit var spinner: Spinner
    lateinit var inputCost: EditText
    lateinit var buttonSave: Button
    private lateinit var fuelViewModel: FuelViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootview: View = inflater.inflate(R.layout.dialog_add_payments, container, false)
        inputCost = rootview.findViewById(R.id.input_cost)
        spinner = rootview.findViewById(R.id.spinner)
        buttonSave = rootview.findViewById(R.id.btn_add_service)
        return rootview
    }
}


