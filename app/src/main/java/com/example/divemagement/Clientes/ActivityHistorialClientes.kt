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

class ActivityHistorialClientes : AppCompatActivity() {

    lateinit var binding: ActivityHistorialClientesBinding
    lateinit var adapter: inmersionesAdapter
    lateinit var clientesInmersiones: MutableList<ListaClientesInmersiones>
    lateinit var recyclerView: RecyclerView
    lateinit var inmersiones: MutableList<ListaInmersiones>

    override fun onCreate(savedInstanceState: Bundle?) {
        title = "Historial de Clientes"
        super.onCreate(savedInstanceState)
        binding = ActivityHistorialClientesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Intenta obtener el email del Intent, si no existe, usa SharedPreferences
        val emailUsuarioRegistrado = intent.getStringExtra("user_email")
            ?: getSharedPreferences("AppPreferences", MODE_PRIVATE).getString("user_email", "No definido")
            ?: "No definido"

        adapter = inmersionesAdapter(mutableListOf())

        binding.botonFlotante.setOnClickListener {
            val intent = Intent(this, ClientesActivity::class.java)
            startActivity(intent)
        }
    }



    override fun onResume() {
        super.onResume()
        CoroutineScope(Dispatchers.IO).launch {

            clientesInmersiones = miInmersionApp.database.clientesInmersionesDAO().getInmersionesClientePorEmail(intent.getStringExtra("user_email")!!)
            runOnUiThread {
                adapter.updateInmersionesList(inmersiones)
                adapter.notifyDataSetChanged()
            }
        }
    }


}