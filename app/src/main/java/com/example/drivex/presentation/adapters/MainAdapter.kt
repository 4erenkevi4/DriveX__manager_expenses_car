package com.example.drivex.presentation.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.drivex.R
import com.example.drivex.data.model.MapModels
import com.example.drivex.data.model.Refuel
import kotlin.math.exp

class MainAdapter(private val click: (Long) -> Unit) :
    RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<Refuel>() {
        override fun areItemsTheSame(oldItem: Refuel, newItem: Refuel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Refuel, newItem: Refuel): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    var listExp = AsyncListDiffer(this, diffCallback)

    inner class RunViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    fun submitList(list: List<Refuel>) = listExp.submitList(list)

    class ViewHolder(
        view: View,
        val onClick: (Long) -> Unit
    ) : RecyclerView.ViewHolder(view) {
        private val root: View = view.findViewById(R.id.root)
        private var iconType: ImageView = root.findViewById(R.id.image_type)
        private val nameType: TextView = root.findViewById(R.id.name_type)
        private val date: TextView = root.findViewById(R.id.date)
        private val cost: TextView = root.findViewById(R.id.cost)

        @SuppressLint("SetTextI18n")
        fun bind(expenss: Refuel) {
            date.text = expenss.date
            cost.text = expenss.totalSum.toString() + " BYN"
            iconType.setImageResource(expenss.icon)
            nameType.text = expenss.title

            if (expenss.title == "Заправка") {
                nameType.setTextColor(R.color.teal_700.toInt())
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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listExp.currentList[position]
        holder.bind(item)
        holder.oClick(item.id)

    }

    override fun getItemCount(): Int = listExp.currentList.size

    fun setData(expenses: List<Refuel>) {
        this.listExp.submitList(expenses)
        notifyDataSetChanged()
    }

}