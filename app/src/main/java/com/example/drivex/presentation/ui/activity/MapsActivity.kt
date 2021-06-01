package com.example.drivex.presentation.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.drivex.R
import com.example.drivex.data.model.MapModels
import com.example.drivex.presentation.ui.map.MapViewModel
import com.example.drivex.presentation.ui.map.TrackingService
import com.example.drivex.utils.Constans
import com.example.drivex.utils.TrackingUtility
import com.google.android.gms.maps.*

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import timber.log.Timber
import java.util.*
import kotlin.math.round

class MapsActivity : AppCompatActivity() {

    private lateinit var mMap: GoogleMap
    lateinit var btnToggleRun: Button
    lateinit var btnFinishRun: Button
    lateinit var tvTimer: TextView
    //lateinit var mapView : MapView

    private var isTracking = false
    private var curTimeInMillis = 0L
    private var pathPoints = mutableListOf<MutableList<LatLng>>()
    private val viewModel: MapViewModel by viewModels()
    private var menu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.fragment_tracking)
        btnToggleRun=findViewById<Button>(R.id.btnToggleRun)
        btnFinishRun=findViewById<Button>(R.id.btnFinishRun)
        tvTimer=findViewById<TextView>(R.id.tvTimer)


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapGoogle) as SupportMapFragment
        mapFragment.getMapAsync {
            mMap = it
            addAllPolylines()
        }

        btnToggleRun.setOnClickListener {
            toggleRun()
        }

        btnFinishRun.setOnClickListener {
            zoomToWholeTrack()
            endRunAndSaveToDB()
        }
        subscribeToObservers()
    }


    /**
     * Subscribes to changes of LiveData objects
     */
    private fun subscribeToObservers() {
        TrackingService.isTracking.observe(this, {
            updateTracking(it)
        })

        TrackingService.pathPoints.observe(this, {
            pathPoints = it
            addLatestPolyline()
            moveCameraToUser()
        })

        TrackingService.timeRunInMillis.observe(this, {
            curTimeInMillis = it
            val formattedTime = TrackingUtility.getFormattedStopWatchTime(it, true)
            tvTimer.text = formattedTime
        })
    }

    /**
     * Will move the camera to the user's location.
     */
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

    /**
     * Adds all polylines to the pathPoints list to display them after screen rotations
     */
    private fun addAllPolylines() {
        for (polyline in pathPoints) {
            val polylineOptions = PolylineOptions()
                .color(Constans.POLYLINE_COLOR)
                .width(Constans.POLYLINE_WIDTH)
                .addAll(polyline)
            mMap?.addPolyline(polylineOptions)
        }
    }

    /**
     * Draws a polyline between the two latest points.
     */
    private fun addLatestPolyline() {
        // only add polyline if we have at least two elements in the last polyline
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

    /**
     * Updates the tracking variable and the UI accordingly
     */
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

    /**
     * Toggles the tracking state
     */
    @SuppressLint("MissingPermission")
    private fun toggleRun() {
        if (isTracking) {
            menu?.getItem(0)?.isVisible = true
            pauseTrackingService()
        } else {
            startOrResumeTrackingService()
            Timber.d("Started service")
        }
    }

    /**
     * Starts the tracking service or resumes it if it is currently paused.
     */
    private fun startOrResumeTrackingService() =
        Intent(applicationContext, TrackingService::class.java).also {
            it.action = Constans.ACTION_START_OR_RESUME_SERVICE
            applicationContext.startService(it)
        }

    /**
     * Pauses the tracking service
     */
    private fun pauseTrackingService() =
        Intent(this, TrackingService::class.java).also {
            it.action = Constans.ACTION_PAUSE_SERVICE
            startService(it)
        }

    /**
     * Stops the tracking service.
     */
    private fun stopTrackingService() =
        Intent(this, TrackingService::class.java).also {
            it.action = Constans.ACTION_STOP_SERVICE
            startService(it)
        }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val mapViewBundle = outState.getBundle(Constans.MAP_VIEW_BUNDLE_KEY)
        if (mapViewBundle != null) {
            onSaveInstanceState(mapViewBundle)
        }
    }

    /**
     * Zooms out until the whole track is visible. Used to make a screenshot of the
     * MapView to save it in the database
     */
    private fun zoomToWholeTrack() {
        val bounds = LatLngBounds.Builder()
        for (polyline in pathPoints) {
            for (point in polyline) {
                bounds.include(point)
            }
        }
        val mapView = findViewById<MapView>(R.id.mapView)
        val width = mapView?.width
        val height = mapView?.height
        mMap?.moveCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds.build(),
                width!!,
                height!!,
                (height * 0.05f).toInt()
            )
        )
    }

    /**
     * Saves the recent run in the Room database and ends it
     */
    private fun endRunAndSaveToDB() {
        mMap?.snapshot { bmp ->
            var distanceInMeters = 0
            for (polyline in pathPoints) {
                distanceInMeters += TrackingUtility.calculatePolylineLength(polyline).toInt()
            }
            val id = 4
            val avgSpeed =
                round((distanceInMeters / 1000f) / (curTimeInMillis / 1000f / 60 / 60) * 10) / 10f
            val timestamp = Calendar.getInstance().timeInMillis
            val run = MapModels(id, bmp, timestamp, avgSpeed, distanceInMeters, curTimeInMillis)
            viewModel.insertRun(run)
            stopRun()
        }
    }

    /**
     * Finishes the tracking.
     */
    private fun stopRun() {
        Timber.d("STOPPING RUN")
        tvTimer.text = "00:00:00:00"
        stopTrackingService()
        val intentMain = Intent(this, MapsActivity::class.java)
        startActivity(intentMain)
    }
}





