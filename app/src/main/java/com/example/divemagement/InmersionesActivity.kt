package com.example.divemagement
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.divemagement.DB.DbHelper
import com.example.divemagement.adapter.inmersionesAdapter
import com.example.divemagement.databinding.ActivityInmersionesBinding

class InmersionesActivity : ActivityWithMenus() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityInmersionesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ActivityWithMenus.actividadActual = 2

        val DbHelper = DbHelper(this)
        val listaInmersiones = DbHelper.getInitialInmersiones()

        val adapter = inmersionesAdapter(listaInmersiones)
        binding.recyclerViewInmersiones.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewInmersiones.adapter = adapter
    }
}