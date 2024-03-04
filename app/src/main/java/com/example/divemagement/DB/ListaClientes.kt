package com.example.divemagement.DB
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "t_clientes")
data class ListaClientes (
    @PrimaryKey(autoGenerate = true)
    var id:Int=0,
    var username : String = "",
    var password : String = "",
    var telefono : String = "",
    var email : String = ""
)
