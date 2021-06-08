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
import com.example.drivex.data.RefuelDao
import com.example.drivex.data.RefuelRoomDatabase
import com.example.drivex.presentation.ui.activity.viewModels.AbstractViewModel
import com.example.drivex.utils.Constans.PAYMENT
import com.example.drivex.utils.Constans.REFUEL
import com.example.drivex.utils.Constans.SERVICE
import com.example.drivex.utils.Constans.SHOPPING
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.nav_header_main.*
import kotlinx.coroutines.*

class StatFragment : Fragment() {

    lateinit var liveDataCost: LiveData<String>
    lateinit var liveDataMileage: LiveData<Int>
    lateinit var liveDataRefuelSum: LiveData<Int>
    lateinit var liveDataServiceSum: LiveData<Int>
    lateinit var liveDataShoppingSum: LiveData<Int>
    lateinit var liveDataPaymentSum: LiveData<Int>
    private lateinit var viewModel: AbstractViewModel
    private lateinit var pieChart: PieChart
    lateinit var text1:TextView
    lateinit var text2:TextView
    private val refuelDao: RefuelDao by lazy {
        val db = RefuelRoomDatabase.getInstance(requireContext())
        db.refuelDao()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(this).get(AbstractViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_stat, container, false)
        text1 = root.findViewById(R.id.text_stat)
        text2= root.findViewById(R.id.text_stat2)
        pieChart = root.findViewById(R.id.pieChart)

        liveDataRefuelSum = viewModel.refuelSum
        liveDataServiceSum = viewModel.serviceSum
        liveDataShoppingSum = viewModel.shoppingSum
        liveDataPaymentSum = viewModel.paymentsSum

        liveDataCost = viewModel.allExpensesSum
        liveDataMileage = viewModel.lastMileage
        liveDataCost.observe(viewLifecycleOwner, { text1.text = "Общие расходы: $it" })
        liveDataMileage.observe(viewLifecycleOwner, Observer{
            it?.let {
                val totalMileage = "Пробег: ${it} km"
                 text2.text = totalMileage
            }
        })
GlobalScope.launch(Dispatchers.Default) {
    pieChart.addSegment( Segment(REFUEL, viewModel.totalRef(REFUEL)),SegmentFormatter(Color.RED))
    pieChart.addSegment( Segment(SERVICE, viewModel.totalRef(SERVICE)),SegmentFormatter(Color.YELLOW))
    pieChart.addSegment( Segment(SHOPPING,viewModel.totalRef(SHOPPING)),SegmentFormatter(Color.CYAN))
    pieChart.addSegment( Segment(PAYMENT,viewModel.totalRef(PAYMENT)),SegmentFormatter(Color.MAGENTA))

}


            liveDataCost.observe(viewLifecycleOwner, { text1.text = "Общие расходы: $it" })
            liveDataMileage.observe(viewLifecycleOwner, { text2.text = "Пробег: $it" })

        return root
    }
    suspend fun getMileage():Int{
        val ffd:Int = refuelDao.getLastMileageInt()
        return ffd
    }

}



