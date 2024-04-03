package com.example.divemagement.Clientes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.divemagement.DB.ListaClientes
import com.example.divemagement.DB.ListaClientesInmersiones
import com.example.divemagement.DB.ListaInmersiones
import com.example.divemagement.DB.miInmersionApp
import com.example.divemagement.adapter.clientesAdapter
import com.example.divemagement.adapter.inmersionesAdapter
import com.example.divemagement.databinding.ActivityHistorialClientesBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ActivityHistorialClientes : AppCompatActivity() {

    lateinit var binding: ActivityHistorialClientesBinding
    lateinit var adapter: inmersionesAdapter
    lateinit var clientesInmersiones: MutableList<ListaClientesInmersiones>
    lateinit var recyclerView: RecyclerView
    lateinit var inmersiones: MutableList<ListaInmersiones>
    lateinit var clientes: MutableList<ListaClientes>

    override fun onCreate(savedInstanceState: Bundle?) {
        title = "Historial de Clientes"
        super.onCreate(savedInstanceState)
        binding = ActivityHistorialClientesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Intenta obtener el email del Intent, si no existe, usa SharedPreferences
        val emailUsuarioRegistrado = intent.getStringExtra("user_email")
            ?: getSharedPreferences("AppPreferences", MODE_PRIVATE).getString("user_email", "No definido")
            ?: "No definido"

        adapter = inmersionesAdapter(mutableListOf(), false)

        binding.botonFlotante.setOnClickListener {
            val intent = Intent(this, ClientesActivity::class.java)
            startActivity(intent)
        }

        CoroutineScope(Dispatchers.IO).launch {

            // Obtiene el ID del cliente actual
            val idClienteActual = miInmersionApp.database.clientesDAO().getIdClientePorEmail(emailUsuarioRegistrado)

            // Obtiene la lista de inmersiones de un cliente
            clientesInmersiones = miInmersionApp.database.clientesInmersionesDAO().getInmersionesCliente(idClienteActual)

            inmersiones = miInmersionApp.database.inmersionesDAO().getAllInmersiones()

            // Crea una lista de inmersiones a partir de la lista de inmersiones de un cliente
            val inmersionesCliente = mutableListOf<ListaInmersiones>()
            clientesInmersiones.forEach { clienteInmersion ->
                inmersiones.forEach { inmersion ->
                    if (clienteInmersion.idInmersion == inmersion.id) {
                        inmersionesCliente.add(inmersion)
                    }
                }
            }

            // Actualiza la lista de inmersiones del adapter
            adapter.updateInmersionesList(inmersionesCliente)

            // Actualiza la lista de inmersiones del RecyclerView
            withContext(Dispatchers.Main) {
                recyclerView = binding.recyclerHistorial
                recyclerView.layoutManager = LinearLayoutManager(this@ActivityHistorialClientes)
                recyclerView.adapter = adapter
            }
        }
    }



    override fun onResume() {
        super.onResume()
    }


}