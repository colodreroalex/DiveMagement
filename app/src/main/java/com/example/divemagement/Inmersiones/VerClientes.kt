package com.example.divemagement.Inmersiones

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.divemagement.Clientes.ClientesActivity
import com.example.divemagement.DB.ListaClientes
import com.example.divemagement.DB.ListaClientesInmersiones
import com.example.divemagement.DB.ListaInmersiones
import com.example.divemagement.DB.miInmersionApp
import com.example.divemagement.R
import com.example.divemagement.adapter.clientesAdapter
import com.example.divemagement.adapter.inmersionesAdapter
import com.example.divemagement.databinding.ActivityHistorialClientesBinding
import com.example.divemagement.databinding.ActivityVerClientesBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VerClientes : AppCompatActivity() {

    lateinit var binding: ActivityVerClientesBinding
    lateinit var adapter: clientesAdapter
    lateinit var recyclerView: RecyclerView
    lateinit var clientes: MutableList<ListaClientes>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerClientesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.recyclerVerClientes
        recyclerView.layoutManager = LinearLayoutManager(this)

        CoroutineScope(Dispatchers.IO).launch {
            clientes = miInmersionApp.database.clientesDAO().getAllClientes()
            withContext(Dispatchers.Main) {
                adapter = clientesAdapter(clientes)
                recyclerView.adapter = adapter
            }
        }

        binding.botonFlotante.setOnClickListener {
            val intent = Intent(this, InmersionesActivity::class.java)
            startActivity(intent)
        }
    }


}