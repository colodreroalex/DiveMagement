package com.example.divemagement.DB

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "t_inscripciones",
    primaryKeys = ["Id","id_cliente", "id_inmersion"],
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
    ],
    indices = [
        androidx.room.Index(value = ["id_cliente"]),
        androidx.room.Index(value = ["id_inmersion"])
    ]
)
data class ListaClientesInmersiones(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "id_cliente")
    val idCliente: Int,
    @ColumnInfo(name = "id_inmersion")
    val idInmersion: Int
)
