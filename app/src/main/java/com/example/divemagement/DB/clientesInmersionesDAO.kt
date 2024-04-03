package com.example.divemagement.DB

import androidx.room.Dao
import androidx.room.Query

@Dao
interface clientesInmersionesDAO {


    //Obtener las inmersiones de un cliente en concreto
    @Query("SELECT * FROM t_clientes_inmersiones WHERE id_cliente = :id_cliente")
    fun getInmersionesCliente(id_cliente: Int): MutableList<ListaClientesInmersiones>


    //Inmersiones de un cliente segun su email
    @Query("SELECT * FROM t_clientes_inmersiones WHERE id_cliente = (SELECT id FROM t_clientes WHERE email = :email)")
    fun getInmersionesClientePorEmail(email: String): MutableList<ListaClientesInmersiones>

    //Asignar una inmersion a un cliente
    @Query("INSERT INTO t_clientes_inmersiones (id_cliente, id_inmersion) VALUES (:id_cliente, :id_inmersion)")
    fun asignarInmersionCliente(id_cliente: Int, id_inmersion: Int)


    //Obtener todos los clientes con sus inmersiones
    @Query("SELECT * FROM t_clientes_inmersiones")
    fun getAllClientesInmersiones(): MutableList<ListaClientesInmersiones>

}