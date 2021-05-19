package com.example.drivex.presentation.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.drivex.R
import com.example.drivex.data.model.Refuel
import com.example.drivex.presentation.adapters.MainAdapter
import com.example.drivex.presentation.ui.activity.FuelActivity
import com.example.drivex.presentation.ui.activity.viewModels.AbstractViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: AbstractViewModel
    lateinit var allExpenses:TextView
    lateinit var allMileage:TextView
    lateinit var liveDataCost: LiveData<String>
    lateinit var liveDataMileage: LiveData<String>
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val view : View = inflater
            .inflate(R.layout.fragment_home, container, false)
        viewModel = ViewModelProvider(this).get(AbstractViewModel::class.java)
        allExpenses=view.findViewById(R.id.all_expencess_car)
        allMileage=view.findViewById(R.id.text_mileage_info)
        liveDataCost=viewModel.serviceCostSum
        liveDataMileage=viewModel.lastMileageStr
        setinfoView()
        setRecyclerview(view)
        return view
    }

    private fun setinfoView() {
        liveDataCost.observe(viewLifecycleOwner, { allExpenses.text=it })
        liveDataMileage.observe(viewLifecycleOwner, { allMileage.text=it })

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