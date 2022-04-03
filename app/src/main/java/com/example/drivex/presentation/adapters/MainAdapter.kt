package com.example.drivex.presentation.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.drivex.R
import com.example.drivex.data.model.Expenses
import com.example.drivex.utils.Constans.PAYMENT
import com.example.drivex.utils.Constans.REFUEL
import com.example.drivex.utils.Constans.SERVICE
import com.example.drivex.utils.Constans.SHOPPING

class MainAdapter(val context: Context?, val currency: String?, private val click: (Long) -> Unit) :
    RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<Expenses>() {
        override fun areItemsTheSame(oldItem: Expenses, newItem: Expenses): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Expenses, newItem: Expenses): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    var listExp = AsyncListDiffer(this, diffCallback)
    fun submitList(list: List<Expenses>) = listExp.submitList(list)

    class ViewHolder(
        view: View,
        val onClick: (Long) -> Unit
    ) : RecyclerView.ViewHolder(view) {
        private val root: View = view.findViewById(R.id.root)
        private var iconType: ImageView = root.findViewById(R.id.image_type)
        private val nameType: TextView = root.findViewById(R.id.name_type)
        private val date: TextView = root.findViewById(R.id.date)
        private val cost: TextView = root.findViewById(R.id.cost)
        private val photo: ImageView = root.findViewById(R.id.preview_photo)

        @RequiresApi(Build.VERSION_CODES.M)
        @SuppressLint("SetTextI18n")
        fun bind(context: Context?, expenss: Expenses, currency: String?) {
            date.text = expenss.date.toString()
            photo.setImageURI(expenss.photoURI?.toUri())
            cost.text = expenss.totalSum.toString() + " " + currency
            //iconType.setImageResource(expenss.icon ?: R.drawable.ic_car)
            context.let {
                nameType.setTextColor(it!!.getColor(R.color.purple20))
                when (expenss.title) {
                    REFUEL -> {
                        nameType.text = it.getText(R.string.refuel)
                        iconType.setImageResource(R.drawable.fuel_icon)
                    }
                    SERVICE -> {
                        nameType.text = it.getText(R.string.service)
                        iconType.setImageResource(R.drawable.service_car_icon)
                    }
                    SHOPPING -> {
                        nameType.text = it.getText(R.string.your_buy)
                        iconType.setImageResource(R.drawable.shoping_icon)
                    }
                    PAYMENT -> {
                        nameType.text = it.getText(R.string.payment)
                        iconType.setImageResource(R.drawable.pay_icon)
                    }
                }
            }
        }

        fun oClick(position: Long) {
            root.setOnClickListener {
                onClick(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_all_expencess,
            parent,
            false
        )
        return ViewHolder(view, click)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listExp.currentList[position]
        holder.bind(context, item, currency)
        holder.oClick(item.id)

    }

    override fun getItemCount(): Int = listExp.currentList.size

}