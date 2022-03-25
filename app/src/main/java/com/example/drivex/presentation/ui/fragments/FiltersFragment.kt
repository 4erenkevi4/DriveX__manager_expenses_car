package com.example.drivex.presentation.ui.fragments

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.AppCompatSpinner
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import com.example.drivex.R
import com.example.drivex.presentation.ui.activity.MainActivity
import com.example.drivex.presentation.ui.activity.viewModels.AbstractViewModel
import com.example.drivex.utils.Constans.FILTERS
import com.example.drivex.utils.Constans.LIST_OF_FILTERS_SP
import com.example.drivex.utils.Constans.PAYMENT
import com.example.drivex.utils.Constans.REFUEL
import com.example.drivex.utils.Constans.SERVICE
import com.example.drivex.utils.Constans.SHOPPING
import com.google.android.material.switchmaterial.SwitchMaterial
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FiltersFragment : AbstractFragment() {

    @Inject
    lateinit var prefs: SharedPreferences
    private lateinit var abstractViewModel: AbstractViewModel
    private lateinit var refuelSwitsh: SwitchMaterial
    private lateinit var serviceSwitsh: SwitchMaterial
    private lateinit var paymentsSwitsh: SwitchMaterial
    private lateinit var buyingSwitsh: SwitchMaterial
    private lateinit var periodSpinner: AppCompatSpinner
    private lateinit var resetFiltersButton: TextView
    private var filters = mutableListOf<String>()
    private lateinit var saveButton: Button
    private var spFilters: Set<String>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        abstractViewModel = ViewModelProvider(this).get(AbstractViewModel::class.java)
        return inflater.inflate(R.layout.fragment_filters, container, false)
    }

    @SuppressLint("CommitPrefEdits")
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
            abstractViewModel.saveToSP(keyType = LIST_OF_FILTERS_SP, set = setOf(), prefs = prefs)
            spFilters = setOf()
            filters = mutableListOf()
            initFilters()
            (activity as MainActivity).replaceFragment(
                R.id.action_global_nav_car
            )
        }
        spFilters = prefs.getStringSet(LIST_OF_FILTERS_SP, setOf<String>())
        initFilters()
        saveButton.setOnClickListener {
            abstractViewModel.saveToSP(
                keyType = LIST_OF_FILTERS_SP,
                set = filters.toSet(),
                prefs = prefs
            )

            (activity as MainActivity).replaceFragment(
                R.id.action_global_nav_car,
                bundleOf(Pair(FILTERS, filters))
            )
        }
    }

    private fun initFilters() {
        spFilters?.let {
            refuelSwitsh.isChecked = it.contains(REFUEL)
            paymentsSwitsh.isChecked = it.contains(PAYMENT)
            serviceSwitsh.isChecked = it.contains(SERVICE)
            buyingSwitsh.isChecked = it.contains(SHOPPING)
        }
        refuelSwitsh.setOnCheckedChangeListener { _, isChecked ->
            applyFilter(isChecked, REFUEL)
        }
        paymentsSwitsh.setOnCheckedChangeListener { _, isChecked ->
            applyFilter(isChecked, PAYMENT)
        }
        serviceSwitsh.setOnCheckedChangeListener { _, isChecked ->
            applyFilter(isChecked, SERVICE)
        }
        buyingSwitsh.setOnCheckedChangeListener { _, isChecked ->
            applyFilter(isChecked, SHOPPING)
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