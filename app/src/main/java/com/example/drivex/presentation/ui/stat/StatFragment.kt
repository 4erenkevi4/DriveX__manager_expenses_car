package com.example.drivex.presentation.ui.stat

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
import androidx.recyclerview.widget.RecyclerView
import com.example.drivex.R
import com.example.drivex.data.model.Expenses
import com.example.drivex.presentation.adapters.ServiceAdapter
import com.example.drivex.presentation.adapters.StatAdapter
import com.example.drivex.presentation.ui.activity.viewModels.AbstractViewModel
import com.example.drivex.presentation.ui.fragments.AbstractFragment
import com.example.drivex.utils.Constans.PAYMENT
import com.example.drivex.utils.Constans.REFUEL
import com.example.drivex.utils.Constans.SERVICE
import com.example.drivex.utils.Constans.SHOPPING
import com.github.mikephil.charting.charts.PieChart
import java.io.Serializable


class StatFragment : AbstractFragment() {



    private lateinit var liveDataCost: LiveData<Double>
    lateinit var liveDataMileage: LiveData<Int>
    lateinit var liveDataRefuelSum: LiveData<Int>
    lateinit var liveDataServiceSum: LiveData<Int>
    lateinit var liveDataShoppingSum: LiveData<Int>
    lateinit var liveDataPaymentSum: LiveData<Int>
    private lateinit var abstractViewModel: AbstractViewModel
    private lateinit var pieChart: PieChart
    lateinit var mileage: TextView
    private lateinit var allExpenses: TextView
    lateinit var expensesRefuel: TextView
    lateinit var expensesService: TextView
    lateinit var expensesShopping: TextView
    lateinit var expensesPayment: TextView
    private lateinit var toolbarStat: Toolbar

    ////////
    private lateinit var statisticRecyclerView: RecyclerView
    private lateinit  var resultExpenses: List<Expenses>
    lateinit var adapter: StatAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        abstractViewModel = ViewModelProvider(this).get(AbstractViewModel::class.java)
        liveDataRefuelSum = abstractViewModel.refuelSum
        liveDataServiceSum = abstractViewModel.serviceSum
        liveDataShoppingSum = abstractViewModel.shoppingSum
        liveDataPaymentSum = abstractViewModel.paymentsSum
        liveDataCost = abstractViewModel.allExpensesSum
        liveDataMileage = abstractViewModel.lastMileage

        return inflater.inflate(R.layout.fragment_stat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //TODO("Temp old initial view logic")
        // oldLogic(view)
        statisticRecyclerView = view.findViewById(R.id.statistic_recycler_view)

        abstractViewModel.expenses.observe(viewLifecycleOwner) { expenses ->
           // prepareDaraForAdapter(expenses)
            setRecyclerview(expenses)
        }
       // adapter.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setRecyclerview( resultExpenses: List<Expenses>) {
        val adapter = StatAdapter(prepareDaraForAdapter(resultExpenses))
        statisticRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        statisticRecyclerView.adapter = adapter
    }

    private fun prepareDaraForAdapter(expenses: List<Expenses>):ArrayList<StatExpenses> {
        val test = expenses.groupBy { it.title }
        val statList: ArrayList<StatExpenses> = arrayListOf()
        for (i in 1..3) {
            statList.add(
                StatExpenses(
                    refuelTotalSum = (test[REFUEL] as List).sumBy { it.totalSum.toInt() },
                    serviceTotalSum = (test[SERVICE] as List).sumBy { it.totalSum.toInt() },
                    paymentTotalSum = (test[PAYMENT] as List).sumBy { it.totalSum.toInt() },
                    shopingTotalSum = (test[SHOPPING] as List).sumBy { it.totalSum.toInt() },
                )
            )
        }
        return statList
    }

    private fun initialStatExpenses(test: Map<String, List<Expenses>>): List<StatExpenses> {
        val statList: ArrayList<StatExpenses> = arrayListOf()
        statList.add(
            StatExpenses(
                refuelTotalSum = (test[REFUEL] as List).sumBy { it.totalSum.toInt() },
                serviceTotalSum = (test[SERVICE] as List).sumBy { it.totalSum.toInt() },
                paymentTotalSum = (test[PAYMENT] as List).sumBy { it.totalSum.toInt() },
                shopingTotalSum = (test[SHOPPING] as List).sumBy { it.totalSum.toInt() },
                allTotalSum = 1,
            )
        )
        return statList.toList()
    }
}


data class StatExpenses(
    var refuelTotalSum: Int = 0,
    var serviceTotalSum: Int = 0,
    var paymentTotalSum: Int = 0,
    var shopingTotalSum: Int = 0,
    var allTotalSum: Int = 0,
    var startPeriod: Long? = null,
    var endPeriod: Long? = null,
) : Serializable


//////////////////
/*private fun oldLogic(view: View) {
    mileage = view.findViewById(R.id.text_stat5)
    allExpenses = view.findViewById(R.id.text_stat2)
    expensesRefuel = view.findViewById(R.id.text_stat)
    expensesService = view.findViewById(R.id.text_stat3)
    expensesShopping = view.findViewById(R.id.text_stat4)
    expensesPayment = view.findViewById(R.id.text_stat6)
    pieChart = view.findViewById(R.id.pieChart)
    setupPieChart()
    loadPieChartData()
    setFloatingMenuVisibility(false)
    liveDataCost.observe(viewLifecycleOwner) { allExpenses.text = "Общие расходы : $it" }
    liveDataMileage.observe(viewLifecycleOwner) { mileage.text = "Актуальный пробег: $it Км" }
    liveDataRefuelSum.observe(
        viewLifecycleOwner
    ) { expensesRefuel.text = "Расходы на топливо: $it BYN" }
    liveDataServiceSum.observe(viewLifecycleOwner) {
        expensesService.text = "Расходы на ТО: $it BYN"
    }
    liveDataShoppingSum.observe(viewLifecycleOwner) {
        expensesShopping.text = "Расходы на платежи: $it BYN"
    }
    liveDataPaymentSum.observe(viewLifecycleOwner) {
        expensesPayment.text = "Расходы на покупки: $it BYN"
    }
    toolbarStat = view.findViewById(R.id.back_toolbar)
    toolbarStat.setTitle(R.string.menu_setting)

    setToolbar(toolbarStat, R.string.menu_stat, isBackButtonEnabled = true)
}

private fun setupPieChart() {
    pieChart.isDrawHoleEnabled = true
    pieChart.setUsePercentValues(true)
    pieChart.setEntryLabelTextSize(12F)
    pieChart.setEntryLabelColor(Color.BLACK)
    pieChart.centerText = "Статистика расходов"
    pieChart.setCenterTextSize(24F)
    pieChart.description.isEnabled = false
    val l: Legend = pieChart.legend
    l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
    l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
    l.orientation = Legend.LegendOrientation.VERTICAL
    l.setDrawInside(false)
    l.isEnabled = true
}

private fun loadPieChartData() {
    val entries: ArrayList<PieEntry> = ArrayList()

    GlobalScope.launch(Dispatchers.Default) {
        entries.add(PieEntry(abstractViewModel.totalRef(REFUEL).toFloat(), REFUEL))
        entries.add(PieEntry(abstractViewModel.totalRef(SERVICE).toFloat(), SERVICE))
        entries.add(PieEntry(abstractViewModel.totalRef(SHOPPING).toFloat(), SHOPPING))
        entries.add(PieEntry(abstractViewModel.totalRef(PAYMENT).toFloat(), PAYMENT))
    }
    val colors: ArrayList<Int> = ArrayList()
    for (color in ColorTemplate.MATERIAL_COLORS) {
        colors.add(color)
    }
    for (color in ColorTemplate.VORDIPLOM_COLORS) {
        colors.add(color)
    }
    val dataSet = PieDataSet(entries, "")
    dataSet.colors = colors
    val data = PieData(dataSet)
    data.setDrawValues(true)
    data.setValueFormatter(PercentFormatter(pieChart))

    data.setValueTextSize(12f)
    data.setValueTextColor(Color.BLACK)
    pieChart.data = data
    pieChart.invalidate()
    pieChart.animateY(1400, Easing.EaseInOutQuad)
}*/












