package com.example.drivex.presentation.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.drivex.R
import com.example.drivex.presentation.ui.home.HomeFragment
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*

class MapFragment : Fragment(),OnMapReadyCallback    {
    private lateinit var mMap : GoogleMap
    private var mapReady = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        var rootView =  inflater.inflate(R.layout.fragment_map, container, false)
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)
        return rootView
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomGesturesEnabled = true
        val home = LatLng(53.86517831128531, 27.56348200124547)
        mMap.addMarker(MarkerOptions().position(home).title("Home"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(home))
        mMap.maxZoomLevel


    }



}