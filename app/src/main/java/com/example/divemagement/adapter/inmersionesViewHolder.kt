package com.example.divemagement.adapter

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.divemagement.DB.ListaInmersiones
import com.example.divemagement.Inmersiones.RegistroInmersion

import com.example.divemagement.databinding.ItemInmersionesBinding

class inmersionesViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val binding = ItemInmersionesBinding.bind(view)


    fun render(inmersionModel: ListaInmersiones, clickable: Boolean){
        binding.titulo.text = inmersionModel.nombre
        binding.profundidad.text = inmersionModel.profundidad.toString()
        binding.temperatura.text = inmersionModel.temperatura.toString()
        Glide.with(binding.cardViewImage.context).load(inmersionModel.photo).into(binding.cardViewImage)


        if(clickable) {
            binding.cardview.setOnClickListener(View.OnClickListener {
                val intent = Intent(binding.cardview.context, RegistroInmersion::class.java)
                intent.putExtra("nombre", inmersionModel.nombre)
                intent.putExtra("profundidad", inmersionModel.profundidad)
                intent.putExtra("fecha", inmersionModel.fecha)
                intent.putExtra("hora", inmersionModel.hora)
                intent.putExtra("visibilidad", inmersionModel.visibilidad)
                intent.putExtra("temperatura", inmersionModel.temperatura)
                intent.putExtra("lugar", inmersionModel.lugar)
                intent.putExtra("descripcion", inmersionModel.descripcion)
                intent.putExtra("photo", inmersionModel.photo)
                binding.cardview.context.startActivity(intent)
            })
        }


    }




}