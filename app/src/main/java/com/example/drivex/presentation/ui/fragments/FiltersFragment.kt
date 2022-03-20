package com.example.drivex.presentation.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.widget.AppCompatSpinner
import androidx.core.os.bundleOf
import com.example.drivex.R
import com.example.drivex.presentation.ui.activity.MainActivity
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.switchmaterial.SwitchMaterial
import java.time.chrono.ChronoPeriod

class FiltersFragment : AbstractFragment() {

    companion object {
        const val FILTERS = "filters"
        const val REFUEL_FILTER = "refuel_filter"
        const val SERVICE_FILTER = "service_filter"
        const val PAYMENT_FILTER = "payment_filter"
        const val BUYING_FILTER = "buying_filter"
        const val TIME_FILTER = "time_filter"

    }

    private lateinit var refuelSwitsh: SwitchMaterial
    private lateinit var serviceSwitsh: SwitchMaterial
    private lateinit var paymentsSwitsh: SwitchMaterial
    private lateinit var buyingSwitsh: SwitchMaterial
    private lateinit var periodSpinner: AppCompatSpinner
    private lateinit var resetFiltersButton: TextView
    private var filters = mutableListOf<String>()
    private lateinit var saveButton: Button


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_filters, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFloatingMenuVisibility(false)
        refuelSwitsh = view.findViewById(R.id.refuel_switsh)
        serviceSwitsh = view.findViewById(R.id.service_switsh)
        paymentsSwitsh = view.findViewById(R.id.payments_switsh)
        buyingSwitsh = view.findViewById(R.id.byuing_switsh)
        periodSpinner = view.findViewById(R.id.period_spinner)
        resetFiltersButton = view.findViewById(R.id.reset_button)
        saveButton = view.findViewById(R.id.save_button)
        resetFiltersButton.setOnClickListener {
            filters = mutableListOf()
        }
        initFilters()
        saveButton.setOnClickListener {
            (activity as MainActivity).replaceFragment(
                R.id.action_global_nav_car,
                bundleOf(Pair(FILTERS, filters))
            )
        }
    }

    private fun initFilters() {
        refuelSwitsh.setOnCheckedChangeListener { _, isChecked ->
            applyFilter(isChecked, REFUEL_FILTER)
        }
        paymentsSwitsh.setOnCheckedChangeListener { _, isChecked ->
            applyFilter(isChecked, PAYMENT_FILTER)
        }
        serviceSwitsh.setOnCheckedChangeListener { _, isChecked ->
            applyFilter(isChecked, SERVICE_FILTER)
        }
        buyingSwitsh.setOnCheckedChangeListener { _, isChecked ->
            applyFilter(isChecked, BUYING_FILTER)
        }
        periodSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            val choose = resources.getStringArray(R.array.periods)
            override fun onItemSelected(
                parent: AdapterView<*>?,
                itemSelected: View, selectedItemPosition: Int, selectedId: Long
            ) {
                if (selectedItemPosition != 0)
                    applyFilter(true, choose[selectedItemPosition])

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                for (n in choose)
                    applyFilter(false, n)
            }

        }
    }

    private fun applyFilter(isCheked: Boolean, filterName: String) {
        if (isCheked)
            filters.add(filterName)
        else
            filters.remove(filterName)
    }


}