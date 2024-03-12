package com.example.divemagement.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.divemagement.DB.ListaInmersiones
import com.example.divemagement.R

class inmersionesAdapter(private var inmersionesList: MutableList<ListaInmersiones>) :
    RecyclerView.Adapter<inmersionesViewHolder>() {



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
        holder.render(item)

    }

    fun updateInmersionesList(newInmersionesList: MutableList<ListaInmersiones>) {
        this.inmersionesList = newInmersionesList
        notifyDataSetChanged()
    }


}