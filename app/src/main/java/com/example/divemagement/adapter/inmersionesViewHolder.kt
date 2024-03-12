package com.example.divemagement.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.divemagement.DB.ListaInmersiones

import com.example.divemagement.databinding.ItemInmersionesBinding

class inmersionesViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val binding = ItemInmersionesBinding.bind(view)


    fun render(inmersionModel: ListaInmersiones){
        binding.titulo.text = inmersionModel.nombre
        binding.profundidad.text = inmersionModel.profundidad.toString()
        binding.temperatura.text = inmersionModel.temperatura.toString()

        Glide.with(binding.cardViewImage.context).load(inmersionModel.photo).into(binding.cardViewImage)

        
    }


}