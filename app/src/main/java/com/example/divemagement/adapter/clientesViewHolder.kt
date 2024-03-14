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
}