package com.example.drivex.presentation.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import com.example.drivex.presentation.ui.dialogs.SettingsDialog.Companion.TYPE_CAR
import com.example.drivex.presentation.ui.dialogs.SettingsDialog.Companion.TYPE_CONSUMPTION
import com.example.drivex.presentation.ui.dialogs.SettingsDialog.Companion.TYPE_CURENCY
import com.example.drivex.presentation.ui.dialogs.SettingsDialog.Companion.TYPE_DISTANCE
import com.example.drivex.presentation.ui.dialogs.SettingsDialog.Companion.TYPE_VOLUME
import com.example.drivex.presentation.ui.setting.SettingFragment
import com.example.drivex.utils.Constans.REQUEST_CODE_LOCATION_PERMISSION
import com.example.drivex.utils.TrackingUtility
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

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
    lateinit var carModel:TextView
    lateinit var liveDataCost: LiveData<Double>
    lateinit var liveDataMileage: LiveData<String>
    lateinit var liveDataCostFUel: LiveData<Int>
    lateinit var liveDataVolumeFUel: LiveData<Int>
    lateinit var liveDataCostService: LiveData<Int>
    lateinit var liveDatarefuelSum: LiveData<Int>
    private  var currencySP:String = ""
    private  var consumptionSP:String = ""
    private  var volumeSP:String = ""
    private var distanceSP: String = ""
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
        recyclerView = view.findViewById(R.id.recycler_view_home)
        carModel = view.findViewById(R.id.text_model_info)
        liveDataCost = viewModel.allExpensesSum
        liveDataMileage = viewModel.lastMileageStr
        liveDataCostFUel = viewModel.allFuelCostSum
        liveDataVolumeFUel = viewModel.allFuelVolumeSum
        liveDataCostService = viewModel.allServiceCostSum
        liveDatarefuelSum = viewModel.refuelSum

        viewModel.expenses.observe(viewLifecycleOwner,  { expenses ->
            adapter.submitList(expenses)
        })
        setinfoView()
        requestPermissions()
        getSharedPref()
        return view
    }

    private fun getSharedPref() {
        val prefs: SharedPreferences? = activity?.getSharedPreferences(
            SettingFragment.APP_PREFERENCES, Context.MODE_PRIVATE)
        if (prefs != null) {
            currencySP = prefs.getString(TYPE_CURENCY, "$") ?: "$"
            consumptionSP = prefs.getString(TYPE_CONSUMPTION, "L/100km") ?: "L/100km"
            volumeSP = prefs.getString(TYPE_VOLUME, "L") ?: "L"
            distanceSP = prefs.getString(TYPE_DISTANCE, "Km") ?: "Km"
            carModel.text = (prefs.getString(TYPE_CAR,""))?: "Your Car"
        }
        setRecyclerview(currencySP)
    }

    @SuppressLint("SetTextI18n")
    private fun setinfoView() {
        liveDataCost.observe(viewLifecycleOwner, { allExpenses.text = getString(R.string.total_expenses)+": $it $currencySP" })
        liveDataMileage.observe(viewLifecycleOwner, { allMileage.text = getString(R.string.mileage)+": $it $distanceSP" })
        liveDatarefuelSum.observe(viewLifecycleOwner,
            { allCostFuel.text = getString(R.string.refuel_expenses)+": $it $currencySP" })
        liveDataVolumeFUel.observe(viewLifecycleOwner,
            { allVolume.text = getString(R.string.total_fuel_volume)+": $it $volumeSP" })
        liveDataCostService.observe(viewLifecycleOwner,
            { allCostService.text = getString(R.string.total_expenses_for_service)+": $it $currencySP" })
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
            val expenses = adapter.listExp.currentList[position]
            viewModel.delete(expenses)
            Snackbar.make(requireView(), getText(R.string.entry_deleted), Snackbar.LENGTH_LONG).apply {
                setAction(getText(R.string.cancel_general)) {
                    viewModel.insert(expenses)
                }
                show()
            }
        }
    }

    private fun setRecyclerview(currencySP: String) {
        adapter = MainAdapter (context, currency = currencySP) { id ->
            startActivity(Intent(context, FuelActivity::class.java).putExtra("id", id))
        }
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView)
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