package com.example.divemagement.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.divemagement.DB.DbHelper
import com.example.divemagement.Inmersion
import com.example.divemagement.R

class inmersionesAdapter(private var inmersionesList: List<Inmersion>) :
    RecyclerView.Adapter<inmersionesViewHolder>() {

    private lateinit var dbHelper: DbHelper

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): inmersionesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return inmersionesViewHolder(
            layoutInflater.inflate(
                R.layout.item_inmersiones,
                parent,
                false
            )
        )
    }


    override fun getItemCount(): Int {
        return inmersionesList.size
    }

    override fun onBindViewHolder(holder: inmersionesViewHolder, position: Int) {
        val item = inmersionesList[position]

        /*holder.itemView.setOnLongClickListener{
            dbHelper.deleteInmersion(item.id)
            inmersionesList = inmersionesList.filter { it.id != item.id }
            notifyDataSetChanged()
            true
        }*/

        holder.render(item)
    }


    fun updateInmersionesList(newInmersionesList: List<Inmersion>) {
        this.inmersionesList = newInmersionesList
        notifyDataSetChanged()
    }


}