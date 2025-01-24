package com.medvedev.snapshothistory.app.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.medvedev.snapshothistory.R
import com.medvedev.snapshothistory.databinding.FragmentMapBinding

class MapFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentMapBinding? = null
    private val binding: FragmentMapBinding
        get() = _binding ?: throw RuntimeException("FragmentMapBinding == null")

    private lateinit var mMap: GoogleMap
    private var latitude: Double = DEFAULT_LOCATION
    private var longitude: Double = DEFAULT_LOCATION

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseParams()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = parentFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val location = LatLng(latitude, longitude)
        mMap.addMarker(
            MarkerOptions().position(location).title(getString(R.string.location_of_snapshot))
        )
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 10f))
    }

    private fun parseParams() {
        latitude = requireArguments().getDouble(ARG_LATITUDE, DEFAULT_LOCATION)
        longitude = requireArguments().getDouble(ARG_LONGITUDE, DEFAULT_LOCATION)
    }

    companion object {
        private const val ARG_LATITUDE = "latitude"
        private const val ARG_LONGITUDE = "longitude"
        private const val DEFAULT_LOCATION = 0.0

        fun getInstance(latitude: Double, longitude: Double): MapFragment =
            MapFragment().apply {
                arguments = Bundle().apply {
                    putDouble(ARG_LATITUDE, latitude)
                    putDouble(ARG_LONGITUDE, longitude)
                }
            }
    }
}