package com.abdalla_mmdouh.mymaps

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.abdalla_mmdouh.mymaps.models.UserPlace

private const val TAG = "MapAdapter"

class MapAdapter(val context:Context,val userPlace: List<UserPlace> ,val onClickListener : OnClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnClickListener{
        fun onItemClicked(position: Int)
    }

    override fun getItemCount() = userPlace.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.user_map,parent,false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val place = userPlace[position]
        val textView = holder.itemView.findViewById<TextView>(R.id.tvUserMapName)
        textView.text = "the place ${place.name}"
        holder.itemView.setOnClickListener{
            onClickListener.onItemClicked(position)
            Log.i(TAG , "the clicked place name is ${place.name}")
        }
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    }
}
