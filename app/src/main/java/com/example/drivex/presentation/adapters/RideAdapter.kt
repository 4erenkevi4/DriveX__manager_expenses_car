package com.example.drivex.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.drivex.R
import com.example.drivex.data.model.MapModels
import kotlinx.android.synthetic.main.item_ride.view.*
import java.text.SimpleDateFormat
import java.util.*

class RideAdapter : RecyclerView.Adapter<RideAdapter.RunViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<MapModels>() {
        override fun areItemsTheSame(oldItem: MapModels, newItem: MapModels): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MapModels, newItem: MapModels): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    inner class RunViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    fun submitList(list: List<MapModels>) = differ.submitList(list)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RunViewHolder {
        return RunViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_ride,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: RunViewHolder, position: Int) {
        val drive = differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this).load(drive.img).into(ivRunImage)

            val calendar = Calendar.getInstance().apply {
                timeInMillis = drive.timestamp
            }
            val dateFormat = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
            tvDate.text = dateFormat.format(calendar.time)

            "${drive.avgSpeedInKMH}km/h".also {
                tvAvgSpeed.text = it
            }

                "${drive.distanceInMeters / 1000f}km".also {
                    tvDistance.text = it
                }

                "${drive.timeInMillis.toInt() / 60000}min".also {
                    tvTime.text = it
                }


        }
    }
}
