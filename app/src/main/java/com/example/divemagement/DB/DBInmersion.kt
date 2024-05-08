package com.example.divemagement.DB
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(ListaInmersiones::class, ListaClientes::class, ListaClientesInmersiones::class), version =1)
abstract class DBInmersion: RoomDatabase() {
    abstract fun inmersionesDAO(): inmersionesDAO
    abstract fun clientesDAO(): clientesDAO

    abstract fun clientesInmersionesDAO(): clientesInmersionesDAO
}