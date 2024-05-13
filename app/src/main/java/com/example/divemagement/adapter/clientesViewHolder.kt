package com.example.divemagement.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.divemagement.DB.ListaClientes
import com.example.divemagement.databinding.ItemClientesBinding

class clientesViewHolder(view: View): RecyclerView.ViewHolder(view) {

    val binding = ItemClientesBinding.bind(view)


    fun render(clienteModel: ListaClientes){
        binding.nombreCliente.text = clienteModel.username
        binding.clientetlf.text = clienteModel.telefono
        binding.clienteEmail.text= clienteModel.email
    }

    //Fun Bind para eliminar cliente al pulsar
    /*fun bind(cliente: ListaClientes, deleteCliente: (ListaClientes) -> Unit){
        binding.nombreCliente.text = cliente.username
        binding.clientetlf.text = cliente.telefono
        binding.clienteEmail.text= cliente.email

        itemView.setOnClickListener{deleteCliente(cliente)}

    }*/


}