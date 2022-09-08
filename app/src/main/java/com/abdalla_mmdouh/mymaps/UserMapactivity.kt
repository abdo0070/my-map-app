package com.abdalla_mmdouh.mymaps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.abdalla_mmdouh.mymaps.databinding.ActivityUserMapactivityBinding
import com.abdalla_mmdouh.mymaps.models.UserPlace
import com.google.android.gms.maps.model.LatLngBounds

class UserMapactivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityUserMapactivityBinding
    private lateinit var userMaps : UserPlace
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserMapactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userMaps = intent.getSerializableExtra("EXTRA_USER_MAP") as UserPlace
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        supportActionBar?.title = userMaps.name
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val boundBuilder = LatLngBounds.builder()
        // Add a marker in Sydney and move the camera
        for (place in userMaps.Places){
            val latLan = LatLng(place.latitude,place.longitude)
            boundBuilder.include(latLan)
            mMap.addMarker(MarkerOptions().position(latLan).title(place.title).snippet(place.description))
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(boundBuilder.build(),1000,1000,0))
    }
}