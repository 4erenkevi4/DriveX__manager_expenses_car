package com.cherenkevich.drivex.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cherenkevich.drivex.R

class ServiceAdapter(private val listOfMaintenance: Array<String> = arrayOf(), private val click: (String, View) -> Unit):
    RecyclerView.Adapter<ServiceAdapter.ViewHolder>() {
    class ViewHolder(
        view: View,
        val onClick: (String,View) -> Unit
    ) : RecyclerView.ViewHolder(view) {
        private val root: View = view.findViewById(R.id.item_root)
        private val nameService = root.findViewById<TextView>(R.id.name_service)
        private val iconService = root.findViewById<View>(R.id.icon_Service)

        fun bind(service: String) {
            nameService.text = service
        }
        fun oClick(string: String,view: View) {
            root.setOnClickListener {
                onClick(string,view)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_service,
            parent,
            false
        )
        return ViewHolder(view,click)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listOfMaintenance[position]
        holder.bind(item)
        holder.oClick(item,holder.itemView)
    }

    override fun getItemCount(): Int {
        return listOfMaintenance.size
    }
    
}