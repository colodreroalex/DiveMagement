package com.example.divemagement

import android.widget.ImageButton


data class Inmersion(

    val id: Int,
    val nombre: String,
    val profundidad: Float,
    val fecha: String,
    val hora: String,
    val temperatura: Float,
    val visibilidad: String,
    val lugar: String,
    val descripcion: String,
    val photo: ImageButton? = null

){

}