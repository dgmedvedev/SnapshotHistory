package com.medvedev.snapshothistory.data.manager.location

import android.location.Location

interface AppLocationManager {
    fun getLocation(): Location?
}