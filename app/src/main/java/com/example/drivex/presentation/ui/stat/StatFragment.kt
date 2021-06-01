package com.example.drivex.presentation.ui.stat

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.androidplot.pie.PieChart
import com.androidplot.pie.Segment
import com.androidplot.pie.SegmentFormatter
import com.example.drivex.R
import com.example.drivex.presentation.ui.activity.viewModels.AbstractViewModel

class StatFragment : Fragment() {

    private lateinit var statViewModel: StatViewModel
    lateinit var liveDataCost: LiveData<String>
    lateinit var liveDataMileage: LiveData<String>
    lateinit var liveDataRefuelSum: LiveData<Int>
    lateinit var liveDataServiceSum: LiveData<Int>
    lateinit var liveDataShoppingSum: LiveData<Int>
    lateinit var liveDataPaymentSum: LiveData<Int>
    private lateinit var viewModel: AbstractViewModel
    private lateinit var pieChart: PieChart
    lateinit var text1:TextView
    lateinit var text2:TextView
    lateinit var button: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        statViewModel =
            ViewModelProvider(this).get(StatViewModel::class.java)
        viewModel = ViewModelProvider(this).get(AbstractViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_stat, container, false)
        text1 = root.findViewById(R.id.text_stat)
        text2= root.findViewById(R.id.text_stat2)
        pieChart = root.findViewById(R.id.pieChart)
        var expRefuel = 0
        var expService = 0
        var expShopping = 0
        var expPayment = 0
        liveDataRefuelSum = viewModel.refuelSum
        liveDataServiceSum = viewModel.serviceSum
        liveDataShoppingSum = viewModel.shoppingSum
        liveDataPaymentSum = viewModel.paymentsSum
        liveDataShoppingSum.observe(viewLifecycleOwner,{text2.text=it.toString()})
        liveDataPaymentSum.observe(viewLifecycleOwner,{expPayment})
        liveDataRefuelSum.observe(viewLifecycleOwner,
            { text1.text = it.toString()})

        button = root.findViewById(R.id.button2)
        liveDataCost = viewModel.allExpensesSum
        liveDataMileage = viewModel.lastMileageStr
        //liveDataCost.observe(viewLifecycleOwner, { textView.text = "Общие расходы: $it" })
        //liveDataMileage.observe(viewLifecycleOwner, { textView2.text = "Пробег: $it" })

        val ExpensesList = arrayListOf<Int>(expService,expShopping,expPayment
        )

        val s1 = Segment("S1", expRefuel)
        val s2 = Segment("S1", liveDataServiceSum.value)
        val s3 = Segment("S1", liveDataShoppingSum.value)
        val s4 = Segment("S1", liveDataPaymentSum.value)

        val sf1 = SegmentFormatter(Color.BLUE)
        val sf2 = SegmentFormatter(Color.YELLOW)
        val sf3 = SegmentFormatter(Color.CYAN)
        val sf4 = SegmentFormatter(Color.MAGENTA)

        pieChart.addSegment(s1,sf1)
        pieChart.addSegment(s2,sf2)
        pieChart.addSegment(s3,sf3)
        pieChart.addSegment(s4,sf4)

        button.setOnClickListener {
            liveDataCost.observe(viewLifecycleOwner, { text1.text = "Общие расходы: $it" })
            liveDataMileage.observe(viewLifecycleOwner, { text2.text = "Пробег: $it" })
        }

        

        return root
    }
}
