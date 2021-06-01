package com.example.drivex.presentation.ui.map

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.drivex.R
import com.example.drivex.data.model.MapModels
import com.example.drivex.presentation.ui.dialogs.CancelDriveDialog
import com.example.drivex.presentation.ui.home.HomeFragment
import com.example.drivex.utils.Constans.ACTION_PAUSE_SERVICE
import com.example.drivex.utils.Constans.ACTION_START_OR_RESUME_SERVICE
import com.example.drivex.utils.Constans.ACTION_STOP_SERVICE
import com.example.drivex.utils.Constans.MAP_VIEW_BUNDLE_KEY
import com.example.drivex.utils.Constans.MAP_ZOOM
import com.example.drivex.utils.Constans.POLYLINE_COLOR
import com.example.drivex.utils.Constans.POLYLINE_WIDTH
import com.example.drivex.utils.TrackingUtility
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.snackbar.Snackbar

import timber.log.Timber
import java.util.*
import kotlin.math.round

const val CANCEL_DIALOG_TAG = "CancelDialog"

class MapFragment: Fragment(R.layout.fragment_tracking) {

    private var mapReady = false

    lateinit var btnToggleRun:Button
    lateinit var btnFinishRun: Button
    lateinit var tvTimer:TextView
    //lateinit var mapView : MapView

    private var mMap: GoogleMap? = null
    private var isTracking = false
    private var curTimeInMillis = 0L
    private var pathPoints = mutableListOf<MutableList<LatLng>>()
    private val viewModel: MapViewModel by viewModels()
    private var menu: Menu? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        val mapView = childFragmentManager.findFragmentById(R.id.mapGoogle) as SupportMapFragment
        mapView.getMapAsync {
            mMap = it
            addAllPolylines()
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapViewBundle = savedInstanceState?.getBundle(MAP_VIEW_BUNDLE_KEY)
        //mapView.onCreate(mapViewBundle)
        btnToggleRun=view.findViewById<Button>(R.id.btnToggleRun)
        btnFinishRun=view.findViewById<Button>(R.id.btnFinishRun)
        tvTimer=view.findViewById<TextView>(R.id.tvTimer)
        // restore dialog instance
        if(savedInstanceState != null) {
            val cancelRunDialog = parentFragmentManager.findFragmentByTag(CANCEL_DIALOG_TAG) as CancelDriveDialog?
            cancelRunDialog?.setYesListener {
                stopRun()
            }
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
        TrackingService.isTracking.observe(viewLifecycleOwner, Observer {
            updateTracking(it)
        })

        TrackingService.pathPoints.observe(viewLifecycleOwner, Observer {
            pathPoints = it
            addLatestPolyline()
            moveCameraToUser()
        })

        TrackingService.timeRunInMillis.observe(viewLifecycleOwner, Observer {
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
                    MAP_ZOOM
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
                .color(POLYLINE_COLOR)
                .width(POLYLINE_WIDTH)
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
                .color(POLYLINE_COLOR)
                .width(POLYLINE_WIDTH)
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
        Intent(requireContext(), TrackingService::class.java).also {
            it.action = ACTION_START_OR_RESUME_SERVICE
            requireContext().startService(it)
        }

    /**
     * Pauses the tracking service
     */
    private fun pauseTrackingService() =
        Intent(requireContext(), TrackingService::class.java).also {
            it.action = ACTION_PAUSE_SERVICE
            requireContext().startService(it)
        }

    /**
     * Stops the tracking service.
     */
    private fun stopTrackingService() =
        Intent(requireContext(), TrackingService::class.java).also {
            it.action = ACTION_STOP_SERVICE
            requireContext().startService(it)
        }

    override fun onSaveInstanceState(outState: Bundle) {
        val mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY)
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
        val mapView = view?.findViewById<MapView>(R.id.mapView)
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
            val id =4
            val avgSpeed =
                round((distanceInMeters / 1000f) / (curTimeInMillis / 1000f / 60 / 60) * 10) / 10f
            val timestamp = Calendar.getInstance().timeInMillis
            val run = MapModels(id,bmp, timestamp, avgSpeed, distanceInMeters, curTimeInMillis)
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
        findNavController().navigate(R.id.action_nav_map_to_nav_car)
    }




}