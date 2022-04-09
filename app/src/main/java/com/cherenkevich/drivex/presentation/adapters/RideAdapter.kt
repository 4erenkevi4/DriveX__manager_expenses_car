package com.cherenkevich.drivex.presentation.adapters

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore.Images
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.cherenkevich.drivex.R
import com.cherenkevich.drivex.data.model.MapModels
import com.cherenkevich.drivex.presentation.ui.dialogs.SettingsDialog
import kotlinx.android.synthetic.main.item_ride.view.*
import java.text.SimpleDateFormat
import java.util.*


class RideAdapter(prefs: SharedPreferences, context: Context?) :
    RecyclerView.Adapter<RideAdapter.RunViewHolder>() {
    private val context = context
    private val consumptionInSity = prefs.getFloat(SettingsDialog.TYPE_SITY_CONSUMPTION, 0F)
    private val consumptionOffSity = prefs.getFloat(SettingsDialog.TYPE_OFF_SITY_CONSUMPTION, 0F)
    private val unityOfVolume = prefs.getString(SettingsDialog.TYPE_VOLUME, "l.")
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
            "${drive.maxSpeed}km/h".also {
                tvTopSpeed.text = it
            }

            "${drive.distanceInMeters / 1000f}km".also {
                tvDistance.text = it
            }

            "${drive.timeInMillis.toInt() / 60000}min".also {
                tvTime.text = it
            }
            if ((drive.distanceInMeters / 1000f) < 1 || consumptionInSity == 0f || consumptionOffSity == 0f)
                fuel_consumption.text = context.getString(R.string.insufficient_data)
            else {
                if (drive.avgSpeedInKMH > 35) {
                    "${(drive.distanceInMeters / 1000f) / consumptionInSity}${unityOfVolume}".also {
                        fuel_consumption.text = it
                    }
                } else {
                    "${(drive.distanceInMeters / 1000f) / consumptionOffSity}${unityOfVolume}".also {
                        fuel_consumption.text = it
                    }
                }
            }
            share_btn.setOnClickListener {
                drive.img?.let { bitmap -> startShared(bitmap, drive) }
            }
        }
    }

    private fun startShared(url: Bitmap, drive: MapModels) {
        val pathofBmp: String =
            Images.Media.insertImage(context?.contentResolver, url, "title", null)
        val mesage =
            "${context?.getString(R.string.send_extra_text)}${context?.getString(R.string.total_dist)}\"${drive.distanceInMeters / 1000f}km\""
        val intent = Intent(Intent.ACTION_SEND);
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK;
        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(pathofBmp));
        intent.putExtra(Intent.EXTRA_TEXT, mesage)
        intent.type = "image/png";
        context?.startActivity(Intent.createChooser(intent, "Share with Friends"));
    }
}
