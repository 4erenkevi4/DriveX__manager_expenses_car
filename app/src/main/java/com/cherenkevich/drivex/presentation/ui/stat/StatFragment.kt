package com.cherenkevich.drivex.presentation.ui.stat

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.cherenkevich.drivex.R
import com.cherenkevich.drivex.data.model.Expenses
import com.cherenkevich.drivex.presentation.adapters.StatAdapter
import com.cherenkevich.drivex.presentation.ui.activity.MainActivity
import com.cherenkevich.drivex.presentation.ui.activity.viewModels.AbstractViewModel
import com.cherenkevich.drivex.presentation.ui.dialogs.SettingsDialog
import com.cherenkevich.drivex.presentation.ui.fragments.AbstractFragment
import com.cherenkevich.drivex.utils.Constans.PAYMENT
import com.cherenkevich.drivex.utils.Constans.REFUEL
import com.cherenkevich.drivex.utils.Constans.SERVICE
import com.cherenkevich.drivex.utils.Constans.SHOPPING
import dagger.hilt.android.AndroidEntryPoint
import java.io.Serializable
import javax.inject.Inject

@AndroidEntryPoint
class StatFragment : AbstractFragment() {

    @Inject
    lateinit var prefs: SharedPreferences

    private lateinit var liveDataMileage: LiveData<Int>
    private lateinit var abstractViewModel: AbstractViewModel
    private lateinit var statisticRecyclerView: RecyclerView
    private lateinit var buttonMonth: TextView
    private lateinit var buttonYear: TextView
    private lateinit var adapter: StatAdapter
    private var isSortByMonths: Boolean = true
    private var localExpenses: List<Expenses> = listOf()
    private lateinit var toolbarStat: Toolbar
    private var currencySP: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        abstractViewModel = ViewModelProvider(this).get(AbstractViewModel::class.java)
        liveDataMileage = abstractViewModel.lastMileage

        return inflater.inflate(R.layout.fragment_stat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        statisticRecyclerView = view.findViewById(R.id.statistic_recycler_view)
        currencySP = prefs.getString(SettingsDialog.TYPE_CURENCY, "$") ?: "$"
        buttonMonth = view.findViewById(R.id.buttonMonth)
        buttonYear = view.findViewById(R.id.buttonYear)
        toolbarStat = view.findViewById(R.id.main_toolbar)
        toolbarStat.setBackgroundColor(resources.getColor(R.color.toolbar_background3))
        toolbarStat.setTitle(R.string.menu_setting)
        setToolbar(toolbarStat, R.string.menu_stat)
        buttonYear.setEnabling(enabled = false)
        abstractViewModel.expenses.observe(viewLifecycleOwner) { expenses ->
            if (expenses.isEmpty()) {
                val intentMap = Intent(activity, MainActivity::class.java)
                val builder = AlertDialog.Builder(context)
                builder
                    .setTitle(R.string.warning)
                    .setMessage(R.string.create_trip_nothing)
                    .setIcon(R.drawable.ic_warning)
                    .setPositiveButton(R.string.create_trip) { dialog, id ->
                        startActivity(intentMap)
                    }

                builder.create()
                builder.show()
            } else {
                localExpenses = expenses
                setRecyclerview(expenses)
            }
        }
        buttonYear.setOnClickListener {
            buttonYear.setEnabling(true)
            buttonMonth.setEnabling(enabled = false)
            isSortByMonths = false
            adapter.submitList(startDataSort(localExpenses), isMonthModes = false)
        }
        buttonMonth.setOnClickListener {
            buttonMonth.setEnabling(true)
            buttonYear.setEnabling(enabled = false)
            isSortByMonths = true
            adapter.submitList(startDataSort(localExpenses), true)
        }
        setFloatingMenuVisibility(false)

    }

    private fun setRecyclerview(resultExpenses: List<Expenses>) {
        val context = context ?: return
        adapter = StatAdapter(startDataSort(resultExpenses), context, currency = currencySP)
        statisticRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        statisticRecyclerView.adapter = adapter
        val snapHelper: SnapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(statisticRecyclerView)
    }

    private fun startDataSort(resultExpenses: List<Expenses>): ArrayList<StatExpenses> {
        var sortedMapExpenses: Map<Int, List<Expenses>> = mapOf()
        sortedMapExpenses = if (isSortByMonths)
            resultExpenses.groupBy { it.month }
        else
            resultExpenses.groupBy { it.year }
        return createFinishSortedList(sortedMapExpenses)

    }


    private fun createFinishSortedList(mapOfExpenses: Map<Int, List<Expenses>>): ArrayList<StatExpenses> {
        val finishList: ArrayList<StatExpenses> = arrayListOf()
        for (list in mapOfExpenses.values) {
            val stageTwo = list.groupBy { it.title }
            val refuelTotalSum =
                stageTwo[REFUEL]?.let { it.sumBy { expenses -> expenses.totalSum.toInt() } } ?: 0
            val serviceTotalSum =
                stageTwo[SERVICE]?.let { it.sumBy { expenses -> expenses.totalSum.toInt() } } ?: 0
            val paymentTotalSum =
                stageTwo[PAYMENT]?.let { it.sumBy { expenses -> expenses.totalSum.toInt() } } ?: 0
            val shoppingTotalSum =
                stageTwo[SHOPPING]?.let { it.sumBy { expenses -> expenses.totalSum.toInt() } } ?: 0

            finishList.add(
                StatExpenses(
                    refuelTotalSum = refuelTotalSum,
                    serviceTotalSum = serviceTotalSum,
                    paymentTotalSum = paymentTotalSum,
                    shopingTotalSum = shoppingTotalSum,
                    allTotalSum = refuelTotalSum + serviceTotalSum + paymentTotalSum + shoppingTotalSum,
                    month = stageTwo[REFUEL]?.first()?.month ?: stageTwo[SERVICE]?.first()?.month
                    ?: stageTwo[PAYMENT]?.first()?.month ?: stageTwo[SHOPPING]?.first()?.month!!,
                    year = stageTwo[REFUEL]?.first()?.year ?: stageTwo[SERVICE]?.first()?.year
                    ?: stageTwo[PAYMENT]?.first()?.year ?: stageTwo[SHOPPING]?.first()?.year!!,
                )
            )
        }
        return finishList
    }
}

private fun TextView.setEnabling(enabled: Boolean) {
    if (enabled) {
        this.setBackgroundResource(R.drawable.shape_corner_button)
        this.setTextColor(Color.WHITE)
    } else {
        this.setBackgroundResource(R.drawable.shape_corner_disabled_button)
        this.setTextColor(Color.GRAY)
    }
}

data class StatExpenses(
    var refuelTotalSum: Int = 0,
    var serviceTotalSum: Int = 0,
    var paymentTotalSum: Int = 0,
    var shopingTotalSum: Int = 0,
    var allTotalSum: Int = 0,
    var month: Int = 0,
    var year: Int = 0,
) : Serializable










