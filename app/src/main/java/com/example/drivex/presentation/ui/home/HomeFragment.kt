package com.example.drivex.presentation.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.drivex.R
import com.example.drivex.presentation.adapters.MainAdapter
import com.example.drivex.presentation.ui.activity.FuelActivity
import com.example.drivex.presentation.ui.activity.viewModels.AbstractViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

 class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: AbstractViewModel
    lateinit var allExpenses: TextView
    lateinit var allMileage: TextView
    lateinit var allVolume: TextView
    lateinit var allCostFuel: TextView
    lateinit var allCostService: TextView
    lateinit var liveDataCost: LiveData<String>
    lateinit var liveDataMileage: LiveData<String>
    lateinit var liveDataCostFUel: LiveData<String>
    lateinit var liveDataVolumeFUel: LiveData<String>
    lateinit var liveDataCostService: LiveData<String>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater
            .inflate(R.layout.fragment_home, container, false)
        viewModel = ViewModelProvider(this).get(AbstractViewModel::class.java)
        allExpenses = view.findViewById(R.id.all_expencess_car)
        allMileage = view.findViewById(R.id.text_mileage_info)
        allVolume = view.findViewById(R.id.volume_summ)
        allCostFuel = view.findViewById(R.id.fuel_expenses)
        allCostService = view.findViewById(R.id.service_summ)
        liveDataCost = viewModel.allExpensesSum
        liveDataMileage = viewModel.lastMileageStr
        liveDataCostFUel = viewModel.allFuelCostSum
        liveDataVolumeFUel = viewModel.allFuelVolumeSum
        liveDataCostService = viewModel.allServiceCostSum
        setinfoView()
        setRecyclerview(view)
        return view
    }

    @SuppressLint("SetTextI18n")
    private fun setinfoView() {
        liveDataCost.observe(viewLifecycleOwner, { allExpenses.text = "Общие расходы: $it" })
        liveDataMileage.observe(viewLifecycleOwner, { allMileage.text = "Пробег: $it" })
        liveDataCostFUel.observe(viewLifecycleOwner,
            { allCostFuel.text = "Общие затраты на топливо: $it" })
        liveDataVolumeFUel.observe(viewLifecycleOwner,
            { allVolume.text = "Общий обьем топлива: $it" })
        liveDataCostService.observe(viewLifecycleOwner,
            { allCostService.text = "Общие затраты на сервис: $it" })


    }

    private fun setRecyclerview(view: View) {
        val adapter = MainAdapter { id ->
            startActivity(Intent(context, FuelActivity::class.java).putExtra("id", id))
        }
        recyclerView = view.findViewById(R.id.recycler_view_home)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        GlobalScope.launch(Dispatchers.Default) {
            adapter.setData(viewModel.readAllDataByDate())
        }
    }

}