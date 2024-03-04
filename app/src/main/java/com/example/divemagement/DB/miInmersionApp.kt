package com.example.divemagement.DB
import android.app.Application
import androidx.room.Room

class miInmersionApp: Application() {
    companion object {
        lateinit var database: DBInmersion
    }

    override fun onCreate() {
        super.onCreate()
        miInmersionApp.database = Room.databaseBuilder(this, DBInmersion::class.java, "DBInmersion").build()
    }
}