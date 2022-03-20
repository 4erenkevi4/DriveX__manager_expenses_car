package com.example.drivex.presentation.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.net.toUri
import androidx.core.view.isGone
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.drivex.R
import com.example.drivex.presentation.adapters.MainAdapter
import com.example.drivex.presentation.ui.activity.FuelActivity
import com.example.drivex.presentation.ui.activity.MainActivity
import com.example.drivex.presentation.ui.activity.viewModels.AbstractViewModel
import com.example.drivex.presentation.ui.dialogs.SettingsDialog.Companion.TYPE_AVATAR
import com.example.drivex.presentation.ui.dialogs.SettingsDialog.Companion.TYPE_CAR
import com.example.drivex.presentation.ui.dialogs.SettingsDialog.Companion.TYPE_CONSUMPTION
import com.example.drivex.presentation.ui.dialogs.SettingsDialog.Companion.TYPE_CURENCY
import com.example.drivex.presentation.ui.dialogs.SettingsDialog.Companion.TYPE_DISTANCE
import com.example.drivex.presentation.ui.dialogs.SettingsDialog.Companion.TYPE_VOLUME
import com.example.drivex.presentation.ui.fragments.AbstractFragment
import com.example.drivex.presentation.ui.fragments.FiltersFragment
import com.example.drivex.presentation.ui.fragments.FiltersFragment.Companion.FILTERS
import com.example.drivex.presentation.ui.setting.SettingFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList


@AndroidEntryPoint
class HomeFragment : AbstractFragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: AbstractViewModel
    lateinit var adapter: MainAdapter
    lateinit var allExpenses: TextView
    lateinit var allMileage: TextView
    lateinit var allVolume: TextView
    lateinit var welcomeText: TextView
    lateinit var historyDescription: TextView
    lateinit var allCostFuel: TextView
    lateinit var allCostService: TextView
    lateinit var avatarCard: ImageView
    lateinit var carModel: TextView
    lateinit var liveDataCost: LiveData<Double>
    lateinit var liveDataMileage: LiveData<String>
    lateinit var liveDataCostFUel: LiveData<Int>
    lateinit var liveDataVolumeFUel: LiveData<Int>
    lateinit var liveDataCostService: LiveData<Int>
    lateinit var liveDatarefuelSum: LiveData<Int>
    private var currencySP: String = ""
    private var consumptionSP: String = ""
    private var volumeSP: String = ""
    private var distanceSP: String = ""
    private lateinit var toolbarHome: Toolbar
    private lateinit var filterButton: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater
            .inflate(R.layout.fragment_home, container, false)
        viewModel = ViewModelProvider(this).get(AbstractViewModel::class.java)
        allExpenses = view.findViewById<TextView?>(R.id.all_expencess_car)
            .also { it.setTextColor(resources.getColor(R.color.white20)) }
        allMileage = view.findViewById<TextView?>(R.id.text_mileage_info)
            .also { it.setTextColor(resources.getColor(R.color.white20)) }
        welcomeText = view.findViewById<TextView?>(R.id.welcome_text)
            .also { it.setTextColor(resources.getColor(R.color.white20)) }
        historyDescription = view.findViewById<TextView?>(R.id.history_description)
        allVolume = view.findViewById<TextView?>(R.id.volume_summ)
            .also { it.setTextColor(resources.getColor(R.color.white20)) }
        allCostFuel = view.findViewById<TextView?>(R.id.fuel_expenses)
            .also { it.setTextColor(resources.getColor(R.color.white20)) }
        allCostService = view.findViewById<TextView?>(R.id.service_summ)
            .also { it.setTextColor(resources.getColor(R.color.white20)) }
        recyclerView = view.findViewById(R.id.recycler_view_home)
        carModel = view.findViewById(R.id.text_model_info)
        avatarCard = view.findViewById(R.id.avatarCardImage)
        toolbarHome = view.findViewById(R.id.toolbar_home)
        filterButton = view.findViewById(R.id.search_toolbar)
        filterButton.setOnClickListener {
            val fragmentManager = parentFragmentManager.beginTransaction()
            fragmentManager.replace( R.id.nav_host_fragment, FiltersFragment(), )
            fragmentManager.commit()
        }
        liveDataCost = viewModel.allExpensesSum
        liveDataMileage = viewModel.lastMileageStr
        liveDataCostFUel = viewModel.allFuelCostSum
        liveDataVolumeFUel = viewModel.allFuelVolumeSum
        liveDataCostService = viewModel.allServiceCostSum
        liveDatarefuelSum = viewModel.refuelSum

        viewModel.expenses.observe(viewLifecycleOwner) { expenses ->
            adapter.submitList(expenses)
            if (expenses.size == 1)
                historyDescription.text = getString(R.string.general_history)
        }
        setinfoView()
        getSharedPref()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments?.containsKey(FiltersFragment.FILTERS) == true)
            applyfilters()
        setFloatingMenuVisibility(true)
        setToolbar(toolbarHome, R.string.menu_home)
    }

    private fun applyfilters() {
       val filters = arguments?.getSerializable(FILTERS)
    }

    private fun getSharedPref() {
        val activity = activity as? MainActivity ?: return
        val prefs: SharedPreferences? = activity.getSharedPreferences(
            SettingFragment.APP_PREFERENCES, Context.MODE_PRIVATE
        )
        if (prefs?.all.isNullOrEmpty().not()) {
            currencySP = prefs!!.getString(TYPE_CURENCY, "$") ?: "$"
            consumptionSP = prefs.getString(TYPE_CONSUMPTION, "L/100km") ?: "L/100km"
            volumeSP = prefs.getString(TYPE_VOLUME, "L") ?: "L"
            distanceSP = prefs.getString(TYPE_DISTANCE, "Km") ?: "Km"
            carModel.text = (prefs.getString(TYPE_CAR, "")) ?: "Your Car"
            carModel.setTextColor(Color.WHITE)
            prefs.getString(TYPE_AVATAR, "").also { imageCarUri = it!!.toUri() }
            if (imageCarUri.toString().isNotEmpty())
                avatarCard.setImageBitmap(createBitmapFile(imageCarUri!!))
        } else {
            carModel.text = getString(R.string.welcome_text_home_fragment)
            welcomeText.text = getString(R.string.welcome_text_home_fragment_2)
            avatarCard.setOnClickListener {
               activity.replaceFragment(R.id.action_global_nav_settings)
            }
        }
        setRecyclerview(currencySP)
    }

    @SuppressLint("SetTextI18n")
    private fun setinfoView() {
        liveDataCost.observe(viewLifecycleOwner) {
            chekVisibilyty(it, allExpenses)
            allExpenses.text = getString(R.string.total_expenses) + ": $it $currencySP"
        }
        liveDataMileage.observe(viewLifecycleOwner) {
            chekVisibilyty(it, allMileage)
            allMileage.text = getString(R.string.mileage) + ": $it $distanceSP"
        }
        liveDatarefuelSum.observe(viewLifecycleOwner) {
            chekVisibilyty(it, allCostFuel)
            allCostFuel.text = getString(R.string.refuel_expenses) + ": $it $currencySP"
        }
        liveDataVolumeFUel.observe(viewLifecycleOwner) {
            chekVisibilyty(it, allVolume)
            allVolume.text = getString(R.string.total_fuel_volume) + ": $it $volumeSP"
        }
        liveDataCostService.observe(viewLifecycleOwner) {
            chekVisibilyty(it, allCostService)
            allCostService.text =
                getString(R.string.total_expenses_for_service) + ": $it $currencySP"
        }
    }

    private fun chekVisibilyty(value: Any?, expenses: TextView) {
        if (value == null || value.toString() == "0")
            expenses.isGone = true
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
            Snackbar.make(requireView(), getText(R.string.entry_deleted), Snackbar.LENGTH_LONG)
                .apply {
                    setAction(getText(R.string.cancel_general)) {
                        viewModel.insert(expenses)
                    }
                    show()
                }
        }
    }

    private fun setRecyclerview(currencySP: String) {
        adapter = MainAdapter(context, currency = currencySP) { id ->
            startActivity(Intent(context, FuelActivity::class.java).putExtra("id", id))
        }
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView)
        if (adapter.itemCount == 0)
            historyDescription.text = getString(R.string.welcome_text_home_fragment_3)
    }
}