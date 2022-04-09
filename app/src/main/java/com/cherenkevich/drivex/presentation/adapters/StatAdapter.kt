package com.cherenkevich.drivex.presentation.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.cherenkevich.drivex.R
import com.cherenkevich.drivex.presentation.adapters.StatAdapter.ViewHolder
import com.cherenkevich.drivex.presentation.ui.stat.StatExpenses
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import java.util.*
import kotlin.collections.ArrayList

class StatAdapter(
    private var expenses: ArrayList<StatExpenses>,
    private val context: Context,
    private val currency: String,
    private var isMonthMode: Boolean = true
) :
    RecyclerView.Adapter<ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val arrowLeft: ImageView = view.findViewById(R.id.arrow_left)
        private val arrowRight: ImageView = view.findViewById(R.id.arrow_right)
        private val allExpenses: TextView = view.findViewById(R.id.all_payments_summary)
        private val descriptionAllExpenses: TextView =
            view.findViewById(R.id.desc_all_payments_summary)
        private val expensesRefuel: TextView = view.findViewById(R.id.refuel_payments_summary)
        private val expensesService: TextView = view.findViewById(R.id.service_payments_summary)
        private val expensesShopping: TextView = view.findViewById(R.id.shoping_payments_summary)
        private val expensesPayment: TextView = view.findViewById(R.id.payments_summary)
        private val currencyTextView: TextView = view.findViewById(R.id.curency_include)
        private val currencyTextView2: TextView = view.findViewById(R.id.curency_include2)
        private val currencyTextView3: TextView = view.findViewById(R.id.curency_include3)
        private val currencyTextView4: TextView = view.findViewById(R.id.curency_include4)
        private val currencyTextView5: TextView = view.findViewById(R.id.curency_include5)
        private var pieChart: PieChart = view.findViewById(R.id.pieChart)
        val monthNames = context.resources.getStringArray(R.array.months)

        private fun setupPieChart(item: StatExpenses) {
            pieChart.isDrawHoleEnabled = true
            pieChart.setUsePercentValues(true)
            pieChart.setEntryLabelTextSize(12F)
            pieChart.setEntryLabelColor(Color.BLACK)
            pieChart.centerText =
                if (isMonthMode)
                    " ${monthNames[item.month]} ${item.year}"
                else
                    "${item.year} ${context.resources.getString(R.string.last_year)}"
            pieChart.setCenterTextSize(20F)
            pieChart.description.isEnabled = false
            val l: Legend = pieChart.legend
            l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
            l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
            l.orientation = Legend.LegendOrientation.VERTICAL
            l.setDrawInside(false)
            l.textColor = Color.WHITE
            l.isEnabled = true
        }

        private fun loadPieChartData(item: StatExpenses) {
            val entries: ArrayList<PieEntry> = ArrayList()
            entries.run {
                if (item.refuelTotalSum != 0)
                    this.add(
                        PieEntry(
                            item.refuelTotalSum.toFloat(),
                            context.getString(R.string.refuel)
                        )
                    )
                if (item.serviceTotalSum != 0)
                    this.add(
                        PieEntry(
                            item.serviceTotalSum.toFloat(),
                            context.getString(R.string.service)
                        )
                    )
                if (item.shopingTotalSum != 0)
                    this.add(
                        PieEntry(
                            item.shopingTotalSum.toFloat(),
                            context.getString(R.string.shopping)
                        )
                    )
                if (item.paymentTotalSum != 0)
                    this.add(
                        PieEntry(
                            item.paymentTotalSum.toFloat(),
                            context.getString(R.string.payments)
                        )
                    )
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


        fun bind(item: StatExpenses, position: Int) {

            arrowLeft.isVisible = (position > 0)
            arrowRight.isVisible = (itemCount > 1 && position + 1 < itemCount)
            descriptionAllExpenses.text =
                if (!isMonthMode)
                    "${context.resources.getString(R.string.total_expenses_desc)} ${item.year} ${
                        context.resources.getString(
                            R.string.last_year
                        )
                    }:"
                else
                    "${context.resources.getString(R.string.total_expenses_desc)} ${monthNames[item.month]} ${item.year}"
            allExpenses.text = item.allTotalSum.toString()
            expensesRefuel.text = item.refuelTotalSum.toString()
            expensesService.text = item.serviceTotalSum.toString()
            expensesShopping.text = item.shopingTotalSum.toString()
            expensesPayment.text = item.paymentTotalSum.toString()
            currencyTextView.text = currency
            currencyTextView2.text = currency
            currencyTextView3.text = currency
            currencyTextView4.text = currency
            currencyTextView5.text = currency
            setupPieChart(item)
            loadPieChartData(item)
        }
    }

    fun submitList(expen: ArrayList<StatExpenses>, isMonthModes: Boolean) {
        expenses = expen
        isMonthMode = isMonthModes
        notifyDataSetChanged()
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
        holder.bind(item, position)
    }

    override fun getItemCount(): Int {
        return expenses.count()
    }
}
