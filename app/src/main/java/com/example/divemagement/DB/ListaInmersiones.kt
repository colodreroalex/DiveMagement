package com.example.divemagement.DB
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation


@Entity(tableName = "t_inmersiones")
data class ListaInmersiones (
    @PrimaryKey(autoGenerate = true)
    var id:Int=0,
    var nombre:String="",
    var profundidad:Float=0.0f,
    var fecha:String="",
    var hora:String="",
    var temperatura:Float=0.0f,
    var visibilidad:String="",
    var lugar:String="",
    var descripcion:String="",
    var photo:String?=""
)
