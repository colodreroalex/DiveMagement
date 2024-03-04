package com.example.divemagement

import com.example.divemagement.DB.ListaInmersiones

class InmersionesProvider {

    companion object{


        val inmersionesList = mutableListOf<ListaInmersiones>(
            ListaInmersiones(1, "Cave Diving", 30.0f, "02/02/2022", "09:00", 19.0f, "Buena", "Mallorca", "Inmersion en cuevas",""),
            ListaInmersiones(2, "Night Diving", 25.0f, "03/03/2022", "10:00", 20.0f, "Regular", "Ibiza", "Inmersion nocturna", ""),
            ListaInmersiones(3, "Sharks Diving", 28.0f, "04/04/2022", "11:00", 21.0f, "Mala", "Menorca", "Inmersion con tiburones", ""),
            ListaInmersiones(4, "Reef Diving", 32.0f, "05/05/2022", "12:00", 22.0f, "Buena", "Formentera", "Inmersion en arrecifes", ""),
            ListaInmersiones(5, "Shipwreck Diving", 35.0f, "06/06/2022", "13:00", 23.0f, "Regular", "Tenerife", "Inmersion en naufragios","" ),
            ListaInmersiones(6, "Deep Diving", 40.0f, "07/07/2022", "14:00", 24.0f, "Mala", "Gran Canaria", "Inmersion profunda","" ),
            ListaInmersiones(7, "Wall Diving", 45.0f, "08/08/2022", "15:00", 25.0f, "Buena", "Fuerteventura", "Inmersion en pared", ""),
            ListaInmersiones(8, "Current Diving", 50.0f, "09/09/2022", "16:00", 26.0f, "Regular", "Lanzarote", "Inmersion en corrientes","" ),
            ListaInmersiones(9, "Cenote Diving", 55.0f, "10/10/2022", "17:00", 27.0f, "Buena", "Mexico", "Inmersion en cenotes", "")
        )
    }



}