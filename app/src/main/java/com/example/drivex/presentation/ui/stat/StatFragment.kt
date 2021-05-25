package com.example.drivex.presentation.ui.stat

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
import com.example.drivex.R
import com.example.drivex.presentation.ui.activity.viewModels.AbstractViewModel

class StatFragment : Fragment() {

    private lateinit var statViewModel: StatViewModel
    lateinit var liveDataCost: LiveData<String>
    lateinit var liveDataMileage: LiveData<String>
    private lateinit var viewModel: AbstractViewModel
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
        val textView: TextView = root.findViewById(R.id.text_stat)
        val textView2: TextView = root.findViewById(R.id.text_stat2)
        liveDataCost = viewModel.allExpensesSum
        button = root.findViewById(R.id.button2)
        liveDataMileage = viewModel.lastMileageStr

        button.setOnClickListener {
            liveDataCost.observe(viewLifecycleOwner, { textView.text = "Общие расходы: $it" })
            liveDataMileage.observe(viewLifecycleOwner, { textView2.text = "Пробег: $it" })
        }
        return root
    }
}
