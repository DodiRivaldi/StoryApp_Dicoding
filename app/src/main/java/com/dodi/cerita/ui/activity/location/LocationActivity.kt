package com.dodi.cerita.ui.activity.location

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewModelScope
import com.dodi.cerita.R
import com.dodi.cerita.abstraction.showToast
import com.dodi.cerita.databinding.ActivityLocationBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LocationActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityLocationBinding
    private lateinit var token: String
    private val viewModel: LocationViewModel by viewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        viewModel.viewModelScope.launch {
            viewModel.getToken().collect {
                token = it!!
            }
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

    }

    private val reqPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isAllow: Boolean ->
            if (isAllow) {
                getLoc()
            } else {
                showToast(this, getString(R.string.enablepermission))
            }

        }

    private fun getLoc() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
            fusedLocationClient.lastLocation.addOnSuccessListener {
                if (it != null) {
                    mMap.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            LatLng(it.latitude, it.longitude), 18f
                        )
                    )
                } else {
                    showToast(this, getString(R.string.enablepermission))
                }
            }
        } else {
            reqPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        getDataLoc()
    }

    private fun getDataLoc() {
        viewModel.viewModelScope.launch {
            viewModel.getDataStory(token).collect {
                it.onSuccess { response ->
                    getLoc()
                    response.listStory.forEach { item ->
                        mMap.addMarker(
                            MarkerOptions().position(LatLng(item.lat, item.lon)).title(item.name)
                                .snippet(item.description)
                        )
                    }
                }
                it.onFailure { ex ->
                    showToast(this@LocationActivity,ex.localizedMessage)
                }
            }
        }
    }
}