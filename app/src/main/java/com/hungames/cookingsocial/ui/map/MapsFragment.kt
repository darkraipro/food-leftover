package com.hungames.cookingsocial.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.ResultReceiver
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnSuccessListener
import com.hungames.cookingsocial.R
import com.hungames.cookingsocial.util.Constants
import com.hungames.cookingsocial.util.TAG_MAP
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


// No need for Data Binding
@AndroidEntryPoint
class MapsFragment : Fragment() {

    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var addressResultReceiver: AddressResultReceiver
    private val REQUEST_LOCATION_PERMISSION = 1

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        map = googleMap
        addressResultReceiver = AddressResultReceiver(Handler(Looper.getMainLooper()))
        enableLocationAccess()
        var lastLocation: Location? = null
        if (hasPermission()){
            fusedLocationClient.lastLocation.addOnSuccessListener(requireActivity(), OnSuccessListener { location: Location? ->
                if (location==null){
                    Timber.tag(TAG_MAP).w("onSuccess task location: null")
                    return@OnSuccessListener
                }
                lastLocation = location
                if (!Geocoder.isPresent()){
                    Toast.makeText(context, "No geo coder available", Toast.LENGTH_LONG).show()
                    return@OnSuccessListener
                }
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location.latitude, location.longitude), 15f))
                startJobIntentService(location)
            }).addOnFailureListener(requireActivity()){
                e -> Timber.tag(TAG_MAP).w("getLastLocation failure: $e")
            }
            // get Users from cache(DB) if they have one. Update RegisteredUser to contain address
        }else {
            requestPermission()
        }
    }

    private fun startJobIntentService(location: Location){
        val intent = Intent(context, FetchAddressIntentService::class.java).apply{
            putExtra(Constants.LOCATION_DATA, location)
            putExtra(Constants.RECEIVER, addressResultReceiver)
        }
        FetchAddressIntentService.enqueueWork(requireContext(), intent, Constants.ADDR_RECEIVER_JOB_ID)

    }



    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_LOCATION_PERMISSION){
            if (grantResults.contains(PackageManager.PERMISSION_GRANTED)){
                enableLocationAccess()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity().applicationContext)
        enableLocationAccess()
        mapFragment?.getMapAsync(callback)
    }

    @SuppressLint("MissingPermission")
    private fun enableLocationAccess(){
        if (hasPermission()){
            map.isMyLocationEnabled = true
        } else {
            requestPermission()
        }
    }

    private fun requestPermission(){
        ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_PERMISSION)
    }

    private fun hasPermission(): Boolean{
        return ContextCompat.checkSelfPermission(requireActivity().applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private inner class AddressResultReceiver internal constructor(handler: Handler): ResultReceiver(handler){
        override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
            super.onReceiveResult(resultCode, resultData)
            val output = resultData?.getString(Constants.RESULT_DATA_KEY)
            if (resultCode == Constants.FAILURE_RESULT){
                return
            } else {
                Toast.makeText(context, output, Toast.LENGTH_LONG).show()
            }
        }
    }
}