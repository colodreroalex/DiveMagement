package com.example.divemagement.ActivitysInmersiones

import android.os.Bundle
import android.view.WindowManager
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.divemagement.ActivitysWithMenuLista
import com.example.divemagement.DB.ListaInmersiones
import com.example.divemagement.DB.miInmersionApp
import com.example.divemagement.InmersionesProvider
import com.example.divemagement.adapter.inmersionesAdapter
import com.example.divemagement.databinding.ActivityInmersionesBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class InmersionesActivity : ActivitysWithMenuLista() {
    private lateinit var adapter: inmersionesAdapter
    private lateinit var binding: ActivityInmersionesBinding
    private lateinit var inmersiones: MutableList<ListaInmersiones>
    private lateinit var recyclerView: RecyclerView




    override fun onCreate(savedInstanceState: Bundle?) {
        title = "Lista de Inmersiones"
        super.onCreate(savedInstanceState)
        binding = ActivityInmersionesBinding.inflate(layoutInflater)
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

    }

    fun getInmersiones() {
        CoroutineScope(Dispatchers.IO).launch {
            inmersiones = miInmersionApp.database.inmersionesDAO().getAllInmersiones()
            runOnUiThread {
                adapter = inmersionesAdapter(inmersiones)
                recyclerView = binding.recycler
                recyclerView.layoutManager = LinearLayoutManager(this@InmersionesActivity)
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