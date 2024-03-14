package com.example.divemagement.Clientes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.divemagement.DB.ListaClientes
import com.example.divemagement.DB.miInmersionApp
import com.example.divemagement.adapter.clientesAdapter
import com.example.divemagement.databinding.ActivityHistorialClientesBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ActivityHistorialClientes : AppCompatActivity() {

    lateinit var binding: ActivityHistorialClientesBinding
    lateinit var adapter: clientesAdapter
    lateinit var clientes: MutableList<ListaClientes>
    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        title = "Historial de Clientes"
        super.onCreate(savedInstanceState)
        binding = ActivityHistorialClientesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = clientesAdapter(mutableListOf())

        CoroutineScope(Dispatchers.IO).launch {
            //Obtener los clientes de la bbdd
            clientes = miInmersionApp.database.clientesDAO().getAllClientes()
            runOnUiThread {
                adapter.actualizarClientesList(clientes)
                adapter.notifyDataSetChanged()
                recyclerView = binding.recyclerclientes
                recyclerView.layoutManager = LinearLayoutManager(this@ActivityHistorialClientes)
                recyclerView.adapter = adapter
            }
        }

        binding.botonFlotante.setOnClickListener {
            val intent = Intent(this, ClientesActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        CoroutineScope(Dispatchers.IO).launch {
            clientes = miInmersionApp.database.clientesDAO().getAllClientes()
            runOnUiThread {
                adapter.actualizarClientesList(clientes)
                adapter.notifyDataSetChanged()
            }
        }
    }


}