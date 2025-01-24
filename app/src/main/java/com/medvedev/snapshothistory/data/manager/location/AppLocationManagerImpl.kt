package com.medvedev.snapshothistory.data.manager.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager

class AppLocationManagerImpl(context: Context) : AppLocationManager {

    private val locationManager: LocationManager =
        context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    @SuppressLint("MissingPermission")
//    @RequiresPermission(anyOf = [android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION])
    override fun getLocation(): Location? {
        val providers = locationManager.getProviders(true)
        var lastLocation: Location? = null

        for (provider in providers) {
            val location = locationManager.getLastKnownLocation(provider)
            if (location != null) {
                if (lastLocation == null || location.time > lastLocation.time) {
                    lastLocation = location
                }
            }
        }
        return lastLocation
    }
}