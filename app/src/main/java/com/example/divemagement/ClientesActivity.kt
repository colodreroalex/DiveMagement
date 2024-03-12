package com.example.divemagement

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager

import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar

import androidx.core.widget.addTextChangedListener
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.divemagement.DB.ListaInmersiones
import com.example.divemagement.DB.miInmersionApp
import com.example.divemagement.adapter.inmersionesAdapter
import com.example.divemagement.databinding.ActivityClientesBinding
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ClientesActivity : AppCompatActivity() {

    private lateinit var adapter: inmersionesAdapter
    private lateinit var binding: ActivityClientesBinding
    private lateinit var inmersiones: MutableList<ListaInmersiones>
    private lateinit var recyclerView: RecyclerView

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: Toolbar
    private lateinit var toggle: ActionBarDrawerToggle


    override fun onCreate(savedInstanceState: Bundle?) {
        title="Lista de inmersiones"
        super.onCreate(savedInstanceState)
        binding = ActivityClientesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Esconder el teclado cuando se inicie la activity de inmersiones
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        adapter = inmersionesAdapter(mutableListOf())
        inmersiones = InmersionesProvider.inmersionesList

        getInmersiones()
        adapter.notifyDataSetChanged()

        //Filtrado de inmersiones por nombre
        binding.idFiltro.addTextChangedListener { text ->
            val textoFiltro = text.toString()
            CoroutineScope(Dispatchers.IO).launch {
                val inmersionesFiltradas = miInmersionApp.database.inmersionesDAO().getAllInmersiones().filter { inmersion ->
                    inmersion.nombre.contains(textoFiltro, ignoreCase = true)
                }
                runOnUiThread {
                    adapter.updateInmersionesList(inmersionesFiltradas as MutableList<ListaInmersiones>)
                }
            }

        }

        //Configuración del toolbar
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        //Configuración del drawerLayout y navigationView
        drawerLayout = binding.drawerLayout
        navigationView = binding.navView

        //Configuración del actionBarDrawerToggle
        toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toggle.syncState()

        // Configuración del NavigationView para manejar los clics en los elementos del menú
        navigationView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.perfil -> {
                    startActivity(Intent(this, InmersionesActivity::class.java))
                }
                R.id.historial -> {
                    startActivity(Intent(this, ClientesActivity::class.java))
                }
                R.id.cierraSesion -> {
                    startActivity(Intent(this, EstadisticasActivity::class.java))
                }
            }
            drawerLayout.closeDrawers()
            true
        }


    }

    fun getInmersiones() {
        CoroutineScope(Dispatchers.IO).launch {
            inmersiones = miInmersionApp.database.inmersionesDAO().getAllInmersiones()
            runOnUiThread {
                adapter = inmersionesAdapter(inmersiones)
                recyclerView = binding.recyclerClientes
                recyclerView.layoutManager = LinearLayoutManager(this@ClientesActivity)
                recyclerView.adapter = adapter
            }
        }
    }

    override fun onResume() {
        super.onResume()

        CoroutineScope(Dispatchers.IO).launch {
            val listaInmersionesActualizada = miInmersionApp.database.inmersionesDAO().getAllInmersiones()

            runOnUiThread {
                adapter.updateInmersionesList(listaInmersionesActualizada)
            }
        }
    }
}