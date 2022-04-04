package com.example.drivex.presentation.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.activity.viewModels
import com.example.drivex.R
import com.example.drivex.data.model.MapModels
import com.example.drivex.presentation.ui.map.MapViewModel
import com.example.drivex.presentation.ui.map.TrackingService
import com.example.drivex.utils.Constans
import com.example.drivex.utils.Constans.MAP_VIEW_BUNDLE_KEY
import com.example.drivex.utils.TrackingUtility
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_tracking.*
import java.util.*
import kotlin.math.round

@AndroidEntryPoint
class MapsActivity : AbstractActivity() {

    private var mMap: GoogleMap? = null
    private lateinit var btnToggleRun: Button
    private lateinit var btnFinishRun: Button
    private lateinit var mapToolbar:Toolbar
    private lateinit var tvTimer: TextView
    private var isTracking = false
    private var curTimeInMillis = 0L
    private var pathPoints = mutableListOf<MutableList<LatLng>>()
    private var sppedList = mutableListOf<Float>()
    private val viewModel: MapViewModel by viewModels()
    private var menu: Menu? = null
    override fun initCalendar(textViewDate: TextView) {
        //Nothing
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.fragment_tracking)
        btnToggleRun = findViewById(R.id.btnToggleRun)
        btnFinishRun = findViewById(R.id.btnFinishRun)
        tvTimer = findViewById(R.id.tvTimer)
        mapToolbar = findViewById(R.id.back_toolbar)

        val viewMap = mapGoogle as SupportMapFragment
        viewMap.getMapAsync {
            mMap = it
            addAllPolylines()
        }

        btnToggleRun.setOnClickListener {
            toggleDrive()
        }

        btnFinishRun.setOnClickListener {
            zoomToWholeTrack()
            enDriveAndSaveToDB()
        }
        setToolbar(mapToolbar, null, true)
        subscribeToObservers()
    }


    private fun subscribeToObservers() {
        TrackingService.isTracking.observe(this) {
            updateTracking(it)
        }

        TrackingService.pathPoints.observe(this) {
            pathPoints = it
            addLatestPolyline()
            moveCameraToUser()
        }

        TrackingService.timeDriveInMillis.observe(this) {
            curTimeInMillis = it
            val formattedTime = TrackingUtility.getFormattedStopWatchTime(it, true)
            tvTimer.text = formattedTime
        }
        TrackingService.speedLivedata.observe(this) {
            sppedList.add(it)
        }


    }

    private fun moveCameraToUser() {
        if (pathPoints.isNotEmpty() && pathPoints.last().isNotEmpty()) {
            mMap?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    pathPoints.last().last(),
                    Constans.MAP_ZOOM
                )
            )
        }
    }

    private fun addAllPolylines() {
        for (polyline in pathPoints) {
            val polylineOptions = PolylineOptions()
                .color(Constans.POLYLINE_COLOR)
                .width(Constans.POLYLINE_WIDTH)
                .addAll(polyline)
            mMap?.addPolyline(polylineOptions)
        }
    }

    private fun addLatestPolyline() {
        if (pathPoints.isNotEmpty() && pathPoints.last().size > 1) {
            val preLastLatLng = pathPoints.last()[pathPoints.last().size - 2]
            val lastLatLng = pathPoints.last().last()
            val polylineOptions = PolylineOptions()
                .color(Constans.POLYLINE_COLOR)
                .width(Constans.POLYLINE_WIDTH)
                .add(preLastLatLng)
                .add(lastLatLng)

            mMap?.addPolyline(polylineOptions)
        }
    }

    private fun updateTracking(isTracking: Boolean) {
        this.isTracking = isTracking
        if (!isTracking && curTimeInMillis > 0L) {
            btnToggleRun.text = getString(R.string.start_text)
            btnFinishRun.visibility = View.VISIBLE
        } else if (isTracking) {
            btnToggleRun.text = getString(R.string.stop_text)
            menu?.getItem(0)?.isVisible = true
            btnFinishRun.visibility = View.GONE
        }
    }

    @SuppressLint("MissingPermission")
    private fun toggleDrive() {
        if (isTracking) {
            menu?.getItem(0)?.isVisible = true
            pauseTrackingService()
        } else {
            startOrResumeTrackingService()
        }
    }

    private fun startOrResumeTrackingService() =
        Intent(applicationContext, TrackingService::class.java).also {
            it.action = Constans.ACTION_START_OR_RESUME_SERVICE
            applicationContext.startService(it)
        }

    private fun pauseTrackingService() =
        Intent(this, TrackingService::class.java).also {
            it.action = Constans.ACTION_PAUSE_SERVICE
            startService(it)
        }

    private fun stopTrackingService() =
        Intent(this, TrackingService::class.java).also {
            it.action = Constans.ACTION_STOP_SERVICE
            startService(it)
        }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY)
        if (mapViewBundle != null) {
            mapGoogle?.onSaveInstanceState(mapViewBundle)
        }
    }

    override fun putData(isUpdate: Boolean) {
        //Nothing
    }

    private fun zoomToWholeTrack() {
        val bounds = LatLngBounds.Builder()
        for (polyline in pathPoints) {
            if(polyline.isNullOrEmpty()) return
            for (point in polyline) {
                bounds.include(point)
            }
        }
        val viewMap = findViewById<View>(R.id.mapGoogle)
        mMap?.moveCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds.build(),
                viewMap.width,
                viewMap.height,
                (viewMap.width * 0.05f).toInt()
            )
        )
    }

    private fun enDriveAndSaveToDB() {
        mMap?.snapshot { bmp ->
            var distanceInMeters = 0
            for (polyline in pathPoints) {
                distanceInMeters += TrackingUtility.calculatePolylineLength(polyline).toInt()
            }
            //val speedList = TrackingUtility.getSpeedList(this)
            val maxSpeed = Math.round((Collections.max(sppedList) * 3.6))
            val avgSpeed =
                round((distanceInMeters / 1000f) / (curTimeInMillis / 1000f / 60 / 60) * 10) / 10f
            val timestamp = Calendar.getInstance().timeInMillis
            val run =
                MapModels(bmp, timestamp, avgSpeed, distanceInMeters, curTimeInMillis, maxSpeed)
            viewModel.insertRun(run)

            stopDrive()
        }
    }

    private fun stopDrive() {
        tvTimer.text = "00:00:00"
        stopTrackingService()
        val intentMain = Intent(this, MainActivity::class.java)
        startActivity(intentMain)
    }
}





