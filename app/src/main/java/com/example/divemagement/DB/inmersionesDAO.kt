package com.example.divemagement.DB
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update


@Dao
interface inmersionesDAO {

    @Query("SELECT * FROM t_inmersiones")
    fun getAllInmersiones(): MutableList<ListaInmersiones>

    @Query("SELECT * FROM t_inmersiones WHERE nombre LIKE :nombre")
    fun buscarInmersionPorNombre(nombre: String): MutableList<ListaInmersiones>

    @Query("SELECT * FROM t_inmersiones WHERE nombre LIKE :nombre LIMIT 1")
    fun buscarInmersionPorNombreLista(nombre: String): ListaInmersiones?

    @Query("SELECT * FROM t_inmersiones WHERE id = :id")
    fun getInmersionesPorId(id: Int): ListaInmersiones



    //Añadir inmersion
    @Insert
    fun insertInmersion(inmersion: ListaInmersiones)

    @Update
    fun updateInmersion(inmersion: ListaInmersiones)

    @Delete
    suspend fun deleteInmersion(inmersion: ListaInmersiones)



}