package com.example.drivex.presentation.ui.dialogs

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.drivex.R
import com.example.drivex.data.model.Refuel
import com.example.drivex.presentation.ui.activity.AbstractActivity.Companion.initCalendar
import com.example.drivex.presentation.ui.activity.viewModels.AbstractViewModel

class AddExpDialogFragment(
    application: Application,
    private val intent: Intent,
    private val contextMain: Context
) : DialogFragment() {
    lateinit var inputCost: EditText
    lateinit var saveButton: Button
    lateinit var description: EditText
    lateinit var dateView: TextView
    lateinit var viewModel: AbstractViewModel
    private val viewModelFactory = ViewModelFactory(application)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootview: View = inflater.inflate(R.layout.dialog_add_expenses, container, false)
        inputCost = rootview.findViewById(R.id.input_cost)
        saveButton = rootview.findViewById(R.id.btn_add_pay)
        description = rootview.findViewById(R.id.input_description)
        dateView = rootview.findViewById(R.id.date_view)
        viewModel = ViewModelProvider(this, viewModelFactory).get(AbstractViewModel::class.java)
        initCalendar(dateView, contextMain)

        saveButton.setOnClickListener {
            val desc: String = description.text.toString()
            val cost: String = inputCost.text.toString()
            val refuel = Refuel(
                id = 0,
                description = desc,
                totalSum = cost.toDouble(),
                date = dateView.text.toString(),
                icon = R.drawable.shoping_icon,
                title = "Покупка"
            )
            viewModel.addRefuel(refuel)
            startActivity(intent)
        }
        return rootview
    }

    @Suppress("UNCHECKED_CAST")
    class ViewModelFactory(
        private val aplication: Application
    ) : ViewModelProvider.Factory {
        override fun <T : androidx.lifecycle.ViewModel?> create(modelClass: Class<T>): T {
            return AbstractViewModel(aplication) as T
        }
    }
}