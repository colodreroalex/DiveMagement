package com.example.divemagement.DB
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface clientesDAO {

        //Obtener todos los clientes
        @Query("SELECT * FROM t_clientes")
        fun getAllClientes(): MutableList<ListaClientes>
        @Query("SELECT * FROM t_clientes WHERE username = :username")
        fun buscarClientePorNombre(username: String): ListaClientes
        //Funcion para asignar un valor al rol del cliente
        @Query("UPDATE t_clientes SET rol = :rol WHERE email = :email AND password = :password")
        fun asignarRol(email: String, password: String, rol: String)
        //Funcion para buscar a clientes por email
        @Query("SELECT * FROM t_clientes WHERE email = :email")
        fun buscarClientePorEmail(email: String): MutableList<ListaClientes>
        //Obtener id de cliente por email
        @Query("SELECT id FROM t_clientes WHERE email = :email")
        fun getIdClientePorEmail(email: String): Int
        @Insert
        fun insertCliente(cliente: ListaClientes)
        @Update
        fun updateCliente(cliente: ListaClientes)
        @Delete
        suspend fun deleteCliente(cliente: ListaClientes)
        //Funcion para obtener un cliente por su id
        @Query("SELECT * FROM t_clientes WHERE id = :idCliente")
        fun getClientById(idCliente: Int): ListaClientes
}


