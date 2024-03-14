package com.example.divemagement.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.divemagement.DB.ListaClientes
import com.example.divemagement.R

class clientesAdapter (private var clientesList: MutableList<ListaClientes>):
    RecyclerView.Adapter<clientesViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): clientesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return clientesViewHolder(
            layoutInflater.inflate(
                R.layout.item_clientes,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return clientesList.size
    }

    override fun onBindViewHolder(holder: clientesViewHolder, position: Int) {
        val item = clientesList[position]
        holder.render(item)
    }

    fun actualizarClientesList(newClientesList: MutableList<ListaClientes>){
        this.clientesList = newClientesList
        notifyDataSetChanged()
    }


}