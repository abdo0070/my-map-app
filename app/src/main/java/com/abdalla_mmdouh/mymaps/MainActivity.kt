package com.abdalla_mmdouh.mymaps

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.abdalla_mmdouh.mymaps.models.Place
import com.abdalla_mmdouh.mymaps.models.UserPlace
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*

class MainActivity : AppCompatActivity() {
    /*
     * 1 - DESIGN
     * 2 - ANIMATION
     */
    companion object{
private val EXTRA_USER_MAP =  "EXTRA_USER_MAP"
private const val TAG = "MainActivity"
private val CREATE_MAP_CODE = 22
private val MAP_NEW_NAME = "MAP_NEW_NAME"
    lateinit var  mapAdapter : MapAdapter
    lateinit var userPlace : MutableList<UserPlace>
    lateinit var newMaptitle :String
    private const val FILE_NAME = "UserMaps.data"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        myMaps()
    }
    private fun myMaps(){
        // PASS THE EDIT NAME TO CREATE MAP ACTIVITY
        userPlace = deserializableUserMap(this).toMutableList() // FIX

        rvMaps.layoutManager = LinearLayoutManager(this)

       rvMaps.adapter = MapAdapter(this , userPlace , object : MapAdapter.OnClickListener{
            override fun onItemClicked(position: Int) {
              Log.i(TAG , "clicked on pos $position")
                val intent = Intent(this@MainActivity ,UserMapactivity::class.java)

                if (intent != null){

                intent.putExtra(EXTRA_USER_MAP,userPlace[position])
                    // SEND THE NAME OF THE MAP
                startActivity(intent)
                    overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right)
                }else{
                    Toast.makeText(this@MainActivity, "Error", Toast.LENGTH_SHORT).show()
                }
            }
        } )
        mapAdapter = rvMaps.adapter as MapAdapter
        addUserMap()
    }

    private fun addUserMap() {

        fabCreateMap.setOnClickListener{
            val intent = Intent(this@MainActivity,CreateMapActivity::class.java)
            // SHOW ALERT DIALOG TO ASK USER TO SET NAME OF THE MAP
            // GET THE DATA FROM HERE AND SEND IT TO THE FUN
            val dialogView = LayoutInflater.from(this@MainActivity).inflate(R.layout.create_map,null)
            val dialog = AlertDialog.Builder(this@MainActivity)
                .setView(dialogView)
                .setNegativeButton("CANCEL",null)
                .setPositiveButton("OK",null)
                .show()
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
                val createMapTitle = dialogView.findViewById<EditText>(R.id.etCreateMapTitle)
                if (createMapTitle.text.isBlank()){
                    return@setOnClickListener
                }
                dialog.dismiss()
                newMaptitle = createMapTitle.text.toString()
                intent.putExtra(MAP_NEW_NAME,newMaptitle)
                // pass the name to intent (CreateMapActivity)
                startActivityForResult(intent,CREATE_MAP_CODE)
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CREATE_MAP_CODE && resultCode == Activity.RESULT_OK){
            // save the data name
            val userMap = data?.getSerializableExtra(EXTRA_USER_MAP) as UserPlace
            // add that to the  recycleView
            userPlace.add(userMap)
            mapAdapter.notifyItemInserted(userPlace.size - 1)
            serializableUserMap(this,userPlace)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
    private fun getDataFile(context: Context) : File{
        return File(context.filesDir,FILE_NAME);
    }
    private fun serializableUserMap(context: Context,userMaps : List<UserPlace>){
        // Write the data
        val dataFile = getDataFile(context)
        ObjectOutputStream(FileOutputStream(getDataFile(context))).use { it.writeObject(userMaps)}
    }

    private fun deserializableUserMap(context: Context):List<UserPlace> {
        val dataFile = getDataFile(context)
        if (!dataFile.exists()){
            return emptyList()
        }
        ObjectInputStream(FileInputStream(dataFile)).use {
            return it.readObject() as List<UserPlace>
            return emptyList()
        }
    }
    private fun generateSampleData(): List<UserPlace> {
        return listOf(
            UserPlace(
                "Memories from University",
                listOf(
                    Place("Branner Hall", "Best dorm at Stanford", 37.426, -122.163),
                    Place("Gates CS building", "Many long nights in this basement", 37.430, -122.173),
                    Place("Pinkberry", "First date with my wife", 37.444, -122.170)
                )
            ),
            UserPlace("January vacation planning!",
                listOf(
                    Place("Tokyo", "Overnight layover", 35.67, 139.65),
                    Place("Ranchi", "Family visit + wedding!", 23.34, 85.31),
                    Place("Singapore", "Inspired by \"Crazy Rich Asians\"", 1.35, 103.82)
                )),
            UserPlace("Singapore travel itinerary",
                listOf(
                    Place("Gardens by the Bay", "Amazing urban nature park", 1.282, 103.864),
                    Place("Jurong Bird Park", "Family-friendly park with many varieties of birds", 1.319, 103.706),
                    Place("Sentosa", "Island resort with panoramic views", 1.249, 103.830),
                    Place("Botanic Gardens", "One of the world's greatest tropical gardens", 1.3138, 103.8159)
                )
            ),
            UserPlace("My favorite places in the Midwest",
                listOf(
                    Place("Chicago", "Urban center of the midwest, the \"Windy City\"", 41.878, -87.630),
                    Place("Rochester, Michigan", "The best of Detroit suburbia", 42.681, -83.134),
                    Place("Mackinaw City", "The entrance into the Upper Peninsula", 45.777, -84.727),
                    Place("Michigan State University", "Home to the Spartans", 42.701, -84.482),
                    Place("University of Michigan", "Home to the Wolverines", 42.278, -83.738)
                )
            ),
            UserPlace("Restaurants to try",
                listOf(
                    Place("Champ's Diner", "Retro diner in Brooklyn", 40.709, -73.941),
                    Place("Althea", "Chicago upscale dining with an amazing view", 41.895, -87.625),
                    Place("Shizen", "Elegant sushi in San Francisco", 37.768, -122.422),
                    Place("Citizen Eatery", "Bright cafe in Austin with a pink rabbit", 30.322, -97.739),
                    Place("Kati Thai", "Authentic Portland Thai food, served with love", 45.505, -122.635)
                )
            )
        )
    }
}