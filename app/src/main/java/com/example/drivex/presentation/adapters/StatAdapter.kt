package com.example.drivex.presentation.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.drivex.R
import com.example.drivex.presentation.adapters.StatAdapter.ViewHolder
import com.example.drivex.presentation.ui.stat.StatExpenses
import com.example.drivex.presentation.ui.stat.StatFragment
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

class StatAdapter(private var expenses: ArrayList<StatExpenses>, private val context:Context) :
    RecyclerView.Adapter<ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val allExpenses: TextView = view.findViewById(R.id.all_payments_summary)
        private val expensesRefuel: TextView = view.findViewById(R.id.refuel_payments_summary)
        private val expensesService: TextView = view.findViewById(R.id.desc_service_payments_summary)
        private val expensesShopping: TextView = view.findViewById(R.id.shoping_payments_summary)
        private val expensesPayment: TextView = view.findViewById(R.id.payments_summary)
        private var pieChart: PieChart = view.findViewById(R.id.pieChart)
        val monthNames  = context.resources.getStringArray(R.array.months)

        private fun setupPieChart(item: StatExpenses) {
            pieChart.isDrawHoleEnabled = true
            pieChart.setUsePercentValues(true)
            pieChart.setEntryLabelTextSize(12F)
            pieChart.setEntryLabelColor(Color.BLACK)
            pieChart.centerText =" ${monthNames[item.month?:1]}${item.year}"
            pieChart.setCenterTextSize(24F)
            pieChart.description.isEnabled = false
            val l: Legend = pieChart.legend
            l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
            l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
            l.orientation = Legend.LegendOrientation.VERTICAL
            l.setDrawInside(false)
            l.isEnabled = true
        }

        private fun loadPieChartData(item: StatExpenses) {
            val entries: ArrayList<PieEntry> = ArrayList()
            entries.run {
                this.add(PieEntry(item.refuelTotalSum.toFloat(), REFUEL))
                this.add(PieEntry(item.serviceTotalSum.toFloat(), SERVICE))
                this.add(PieEntry(item.shopingTotalSum.toFloat(), SHOPPING))
                this.add(PieEntry(item.paymentTotalSum.toFloat(), PAYMENT))
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
            data.run {
                this.setDrawValues(true)
                this.setValueFormatter(PercentFormatter(pieChart))
                this.setValueTextSize(12f)
                this.setValueTextColor(Color.BLACK)
            }
            pieChart.data = data
            pieChart.invalidate()
            pieChart.animateY(1400, Easing.EaseInOutQuad)
        }


        fun bind(item: StatExpenses) {
            allExpenses.text = item.allTotalSum.toString()
            expensesRefuel.text = item.refuelTotalSum.toString()
            expensesService.text = item.serviceTotalSum.toString()
            expensesShopping.text = item.shopingTotalSum.toString()
            expensesPayment.text = item.paymentTotalSum.toString()
            setupPieChart(item)
            loadPieChartData(item)
        }
    }

    fun submitList(expen: ArrayList<StatExpenses>) {
        expenses = expen
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.statistic_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = expenses[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return expenses.count()
    }
}
