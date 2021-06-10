package com.example.drivex.presentation.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.drivex.R
import com.example.drivex.presentation.adapters.MainAdapter
import com.example.drivex.presentation.ui.activity.FuelActivity
import com.example.drivex.presentation.ui.activity.viewModels.AbstractViewModel
import com.example.drivex.utils.Constans.REQUEST_CODE_LOCATION_PERMISSION
import com.example.drivex.utils.TrackingUtility
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.util.*

@AndroidEntryPoint
class HomeFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: AbstractViewModel
    lateinit var  adapter:MainAdapter
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
    lateinit var liveDatarefuelSum: LiveData<Int>
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
        liveDatarefuelSum = viewModel.refuelSum
        viewModel.expens.observe(viewLifecycleOwner,  { expens ->
            adapter.submitList(expens)
        })
        setinfoView()
        setRecyclerview(view)
        requestPermissions()
        return view
    }
    private val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.layoutPosition
            val expenses = adapter.differ.currentList[position]
            viewModel.delete(expenses)
            Snackbar.make(requireView(), "Поездка удалена", Snackbar.LENGTH_LONG).apply {
                setAction("Undo") {
                    viewModel.insert(expenses)
                }
                show()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setinfoView() {
        liveDataCost.observe(viewLifecycleOwner, { allExpenses.text = "Общие расходы: $it" })
        liveDataMileage.observe(viewLifecycleOwner, { allMileage.text = "Пробег: $it" })
        liveDatarefuelSum.observe(viewLifecycleOwner,
            { allCostFuel.text = "Общие затраты на топливо: $it BYN" })
        liveDataVolumeFUel.observe(viewLifecycleOwner,
            { allVolume.text = "Общий обьем топлива: $it" })
        liveDataCostService.observe(viewLifecycleOwner,
            { allCostService.text = "Общие затраты на сервис: $it" })


    }

    private fun setRecyclerview(view: View) {
        adapter = MainAdapter { id ->
            startActivity(Intent(context, FuelActivity::class.java).putExtra("id", id))
        }
        recyclerView = view.findViewById(R.id.recycler_view_home)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())



        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView)
        recyclerView.adapter = adapter
        GlobalScope.launch(Dispatchers.Default) {
            adapter.setData(viewModel.readAllDataByDate())
        }

    }

    private fun requestPermissions() {
        if (TrackingUtility.hasLocationPermissions(requireContext())) {
            return
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            EasyPermissions.requestPermissions(
                this,
                "You need to accept location permission to use this app",
                REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        } else {
            EasyPermissions.requestPermissions(
                this,
                "You need to accept location permissions to use this app",
                REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }
    }
    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).setThemeResId(R.style.Base_Theme_AppCompat_Dialog_Alert).build().show()
        } else {
            requestPermissions()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {}

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }


}