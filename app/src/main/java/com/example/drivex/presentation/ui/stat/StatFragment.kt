package com.example.drivex.presentation.ui.stat

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import com.example.drivex.R
import com.example.drivex.presentation.ui.activity.viewModels.AbstractViewModel
import com.example.drivex.utils.Constans.PAYMENT
import com.example.drivex.utils.Constans.REFUEL
import com.example.drivex.utils.Constans.SERVICE
import com.example.drivex.utils.Constans.SHOPPING
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.coroutines.*


class StatFragment : Fragment() {

    lateinit var liveDataCost: LiveData<Double>
    lateinit var liveDataMileage: LiveData<Int>
    lateinit var liveDataRefuelSum: LiveData<Int>
    lateinit var liveDataServiceSum: LiveData<Int>
    lateinit var liveDataShoppingSum: LiveData<Int>
    lateinit var liveDataPaymentSum: LiveData<Int>
    private lateinit var viewModel: AbstractViewModel
    private lateinit var pieChart: PieChart
    lateinit var mileage: TextView
    lateinit var allExpenses: TextView
    lateinit var expensesRefuel: TextView
    lateinit var expensesService: TextView
    lateinit var expensesShopping: TextView
    lateinit var expensesPayment: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(this).get(AbstractViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_stat, container, false)
        mileage = root.findViewById(R.id.text_stat5)
        allExpenses = root.findViewById(R.id.text_stat2)
        expensesRefuel = root.findViewById(R.id.text_stat)
        expensesService = root.findViewById(R.id.text_stat3)
        expensesShopping = root.findViewById(R.id.text_stat4)
        expensesPayment = root.findViewById(R.id.text_stat6)
        pieChart = root.findViewById(R.id.pieChart)
        setupPieChart()
        loadPieChartData()
        liveDataRefuelSum = viewModel.refuelSum
        liveDataServiceSum = viewModel.serviceSum
        liveDataShoppingSum = viewModel.shoppingSum
        liveDataPaymentSum = viewModel.paymentsSum
        liveDataCost = viewModel.allExpensesSum
        liveDataMileage = viewModel.lastMileage
        liveDataCost.observe(viewLifecycleOwner, { allExpenses.text = "Общие расходы : $it" })
        liveDataMileage.observe(viewLifecycleOwner, { mileage.text = "Актуальный пробег: $it Км" })
        liveDataRefuelSum.observe(viewLifecycleOwner, { expensesRefuel.text = "Расходы на топливо: $it BYN" })
        liveDataServiceSum.observe(viewLifecycleOwner, { expensesService.text = "Расходы на ТО: $it BYN" })
        liveDataShoppingSum.observe(viewLifecycleOwner, { expensesShopping.text = "Расходы на платежи: $it BYN" })
        liveDataPaymentSum.observe(viewLifecycleOwner, { expensesPayment.text = "Расходы на покупки: $it BYN" })

        return root
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
            entries.add(PieEntry(viewModel.totalRef(REFUEL).toFloat(), REFUEL))
            entries.add(PieEntry(viewModel.totalRef(SERVICE).toFloat(), SERVICE))
            entries.add(PieEntry(viewModel.totalRef(SHOPPING).toFloat(), SHOPPING))
            entries.add(PieEntry(viewModel.totalRef(PAYMENT).toFloat(), PAYMENT))
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
    }
}












