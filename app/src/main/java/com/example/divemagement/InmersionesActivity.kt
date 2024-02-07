package com.example.divemagement

import android.os.Bundle
import android.view.WindowManager
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.divemagement.DB.DbHelper
import com.example.divemagement.adapter.inmersionesAdapter
import com.example.divemagement.databinding.ActivityInmersionesBinding

class InmersionesActivity : ActivitysWithMenuLista() {
    private lateinit var adapter: inmersionesAdapter
    private lateinit var dbHelper: DbHelper
    private lateinit var binding: ActivityInmersionesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        title = "Lista de Inmersiones"
        super.onCreate(savedInstanceState)
        binding = ActivityInmersionesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Esconder el teclado cuando se inicie la activity de inmersiones
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        //Instanciamos de la clase dbHelper
        dbHelper = DbHelper(this)

        //Obtenemos la lista de inmersiones
        val listaInmersiones = dbHelper.getInmersiones()

        //Instanciamos el adapter y le pasamos la lista de inmersiones
        adapter = inmersionesAdapter(listaInmersiones)


        binding.recycler.layoutManager = LinearLayoutManager(this) //Esto es necesario para que se muestre la lista

        //Le pasamos el adapter al recycler
        binding.recycler.adapter = adapter



        //Filtrado de inmersiones por nombre
        binding.idFiltro.addTextChangedListener { text ->
            val filteredList = listaInmersiones.filter { inmersion ->
                inmersion.nombre.contains(text.toString(), ignoreCase = true)
            }
            adapter.updateInmersionesList(filteredList)
        }

    }

    override fun onResume() {
        super.onResume()
        val listaInmersionesActualizada = dbHelper.getInmersiones()
        adapter.updateInmersionesList(listaInmersionesActualizada)

    }


}