package com.example.divemagement.DB
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface clientesDAO {

        @Query("SELECT * FROM t_clientes")
        fun getAllClientes(): MutableList<ListaClientes>

        @Query("SELECT * FROM t_clientes WHERE username = :username")
        fun buscarClientePorNombre(username: String): ListaClientes

        //Funcion para asignar un valor al rol del cliente
        @Query("UPDATE t_clientes SET rol = :rol WHERE email = :email AND password = :password")
        fun asignarRol(email: String, password: String, rol: String)




        @Insert
        fun insertCliente(cliente: ListaClientes)



        @Update
        fun updateCliente(cliente: ListaClientes)

        @Delete
        suspend fun deleteCliente(cliente: ListaClientes)
}


