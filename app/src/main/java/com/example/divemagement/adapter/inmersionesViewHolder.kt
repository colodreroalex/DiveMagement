package com.example.divemagement.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.divemagement.DB.ListaInmersiones

import com.example.divemagement.databinding.ItemInmersionesBinding

class inmersionesViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val binding = ItemInmersionesBinding.bind(view)

    fun render(inmersionModel: ListaInmersiones){
        binding.cardViewTitle.text = inmersionModel.nombre
        binding.cardViewProfundidad.text = inmersionModel.profundidad.toString()
        binding.cardViewTemperatura.text = inmersionModel.temperatura.toString()

        Glide.with(binding.cardViewImage.context).load(inmersionModel.photo).into(binding.cardViewImage)
    }

    fun bind(inmersion: ListaInmersiones){
        binding.cardViewTitle.text = inmersion.nombre
        binding.cardViewProfundidad.text = inmersion.profundidad.toString()
        binding.cardViewTemperatura.text = inmersion.temperatura.toString()

        Glide.with(binding.cardViewImage.context).load(inmersion.photo).into(binding.cardViewImage)
    }
}