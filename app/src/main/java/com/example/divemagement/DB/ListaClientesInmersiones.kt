package com.example.divemagement.DB

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "t_clientes_inmersiones",
    primaryKeys = ["id_cliente", "id_inmersion"],
    foreignKeys = [
        ForeignKey(
            entity = ListaClientes::class,
            parentColumns = ["id"],
            childColumns = ["id_cliente"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ListaInmersiones::class,
            parentColumns = ["id"],
            childColumns = ["id_inmersion"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ListaClientesInmersiones(
    @ColumnInfo(name = "id_cliente")
    val idCliente: Int,
    @ColumnInfo(name = "id_inmersion")
    val idInmersion: Int
)
