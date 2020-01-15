package com.demo.openweather.common

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.net.Uri.fromParts
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import com.google.android.gms.location.*
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener


class GpsProvider(private val activity :Activity, private var mFusedLocationClient : FusedLocationProviderClient) {

    var latitude = ""
    var longitude = ""

    private var function: ((latitude :String, longitude :String) -> Unit)? = null

    fun locationListener(function: (latitude :String, longitude :String) -> Unit) {
        this.function = function
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }
    @SuppressLint("MissingPermission")
    fun getLastLocation() {
        if (isLocationEnabled()) {

            mFusedLocationClient.lastLocation.addOnCompleteListener(activity) { task ->
                val location: Location? = task.result
                if (location == null) {
                    requestNewLocationData()
                } else {
                    latitude = location.latitude.toString()
                    longitude = location.longitude.toString()
                    Log.e("lat n long", "$latitude,$longitude")
                    function?.invoke(latitude,longitude)
                }
            }
        } else {
            Toast.makeText(activity, "Turn on location", Toast.LENGTH_LONG).show()
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            activity.startActivity(intent)
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation: Location = locationResult.lastLocation
            latitude = mLastLocation.latitude.toString()
            longitude = mLastLocation.longitude.toString()
        }
    }


    /**
     * Requesting location permission
     * This uses single permission model from dexter
     * Once the permission granted, fetches the coordinates
     * On permanent denial opens settings dialog
     */
    fun requestLocationPermission() {
        Dexter.withActivity(activity)
            .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    getLastLocation()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    // check for permanent denial of permission
                    if (response.isPermanentlyDenied) {
                        showSettingsDialog()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest,
                    token: PermissionToken) {
                    token.continuePermissionRequest()
                }
            }).check()
    }

    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
    private fun showSettingsDialog() {
        val permissons = activity.getString(com.demo.openweather.R.string.permissons)
        val appSettings = activity.getString(com.demo.openweather.R.string.app_permissons)
        val settings = activity.getString(com.demo.openweather.R.string.settings)
        val cancel = activity.getString(com.demo.openweather.R.string.cancel)
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(permissons)
        builder.setMessage(appSettings)
        builder.setPositiveButton(settings) { dialog, which ->
            dialog.cancel()
            openSettings()
        }
        builder.setNegativeButton(cancel,
            DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
        builder.show()

    }

    // navigating user to app settings
    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = fromParts("package", activity.packageName, null)
        intent.data = uri
        activity.startActivityForResult(intent,101)
    }


}