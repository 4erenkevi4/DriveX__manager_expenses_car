package com.cherenkevich.drivex.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.widget.Toast
import com.cherenkevich.drivex.presentation.ui.map.Polyline
import java.util.concurrent.TimeUnit


class TrackingUtility {

    companion object {

        fun getFormattedStopWatchTime(ms: Long, includeMillis: Boolean = false): String {
            var milliseconds = ms
            val hours = TimeUnit.MILLISECONDS.toHours(milliseconds)
            milliseconds -= TimeUnit.HOURS.toMillis(hours)
            val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds)
            milliseconds -= TimeUnit.MINUTES.toMillis(minutes)
            val seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds)
            if (!includeMillis) {
                return "${if (hours < 10) "0" else ""}$hours:" +
                        "${if (minutes < 10) "0" else ""}$minutes:" +
                        "${if (seconds < 10) "0" else ""}$seconds"
            }
            milliseconds -= TimeUnit.SECONDS.toMillis(seconds)
            milliseconds /= 10
            return "${if (hours < 10) "0" else ""}$hours:" +
                    "${if (minutes < 10) "0" else ""}$minutes:" +
                    "${if (seconds < 10) "0" else ""}$seconds:" +
                    "${if (milliseconds < 10) "0" else ""}$milliseconds"
        }

        /**
         * Calculates the length of a specific polyline
         */
        fun calculatePolylineLength(polyline: Polyline): Float {
            var distance = 0f
            for (i in 0..polyline.size - 2) {
                val pos1 = polyline[i]
                val pos2 = polyline[i + 1]
                val result = FloatArray(1)
                Location.distanceBetween(
                    pos1.latitude,
                    pos1.longitude,
                    pos2.latitude,
                    pos2.longitude,
                    result
                )
                distance += result[0]
            }
            return distance
        }

        fun getSpeedList(context: Context): ArrayList<Float> {
            val speed = arrayListOf<Float>()
            val locationManager = context.getSystemService(LOCATION_SERVICE) as LocationManager

            val locationListener: LocationListener = object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    location.latitude
                    speed.add(location.speed)
                    //Toast.makeText(context, "Скорость:" + location.speed, Toast.LENGTH_SHORT).show()
                }

                override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
                override fun onProviderEnabled(provider: String) {}
                override fun onProviderDisabled(provider: String) {}
            }

            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                1000,
                1f,
                locationListener
            )
            return speed
        }
    }
}