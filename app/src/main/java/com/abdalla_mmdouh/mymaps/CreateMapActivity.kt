package com.abdalla_mmdouh.mymaps

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.abdalla_mmdouh.mymaps.databinding.ActivityCreateMapBinding
import com.abdalla_mmdouh.mymaps.models.Place
import com.abdalla_mmdouh.mymaps.models.UserPlace
import com.google.android.gms.maps.model.Marker
import kotlinx.android.synthetic.main.map_info.*

class CreateMapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityCreateMapBinding
    private val TAG = "CreateMapActivity"
    private var markers : MutableList<Marker> = mutableListOf()
    private val EXTRA_USER_MAP = "EXTRA_USER_MAP"

    //1 - Make a Menu Option to Save The Data
    //2 - Send the data to MainActivity
    // make a snackbar to notify the user to create new map is started
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        supportActionBar?.title=  intent.getStringExtra("MAP_NEW_NAME")
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
        // add delete option
            mMap.setOnMapLongClickListener {LanLng->
                showAlertDialog(LanLng)
            }
        deletePlace()
        // add place
        val egypt = LatLng(30.0444, 31.2357)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(egypt,10f))
    }

    private fun deletePlace() {
        // ask if he is sure about delete
        mMap.setOnInfoWindowClickListener {
            markerDelete->
            val dialog =
                AlertDialog.Builder(this)
                    .setTitle("Delete Place ")
                    .setMessage("are you sure to delete !")
                    .setNegativeButton("CANCEL",null)
                    .setPositiveButton("YES",null)
                    .show()
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
                markers.remove(markerDelete)
                markerDelete.remove()
            }
        }
    }

    private fun showAlertDialog( newPlace: LatLng) {
        // 1 - set view instead of title and message
        // 2 - hint to use any view in the dialog you need to inflate that first
        // done
        // 3 - in that view i add option to set place name and place description
        // done
        // 4 - add all new markers to a list of Markers
        // done
        val dialogView = LayoutInflater.from(this@CreateMapActivity).inflate(R.layout.map_info,null)
        val dialog =
            AlertDialog.Builder(this)
                .setView(dialogView)
            .setNegativeButton("CANCEL",null)
            .setPositiveButton("OK",null)
            .show()
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
            val title = dialogView.findViewById<EditText>(R.id.etPlaceName).text.toString()
            val description = dialogView.findViewById<EditText>(R.id.etDescription).text.toString()
            if (description.trim().isEmpty()|| title.trim().isEmpty()){
                return@setOnClickListener
            }
            val mark = mMap.addMarker(MarkerOptions().position(newPlace).title(title).snippet(description))
            markers.add(mark!!)
            Log.i(TAG , "the number of places is ${markers.size}")
            dialog.dismiss()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.save_map_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.miSaveMap){
            // save and send the data to the main activity
                if (markers.isEmpty()){
                    Toast.makeText(this@CreateMapActivity,"There are no markers",Toast.LENGTH_SHORT).show()
                    return true
                }
                // that's only if the markers is not empty
                val places = markers.map { markers -> Place(markers.title!!,markers.snippet!!,markers.position.latitude,markers.position.longitude) }
            val mapName = intent.getStringExtra("MAP_NEW_NAME")!!
            val usermap = UserPlace(mapName,places)
            val data = Intent()
            data.putExtra(EXTRA_USER_MAP,usermap)
            setResult(Activity.RESULT_OK,data)
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
