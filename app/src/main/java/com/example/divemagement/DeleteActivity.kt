package com.example.divemagement

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.divemagement.DB.miInmersionApp
import com.example.divemagement.adapter.inmersionesAdapter
import com.example.divemagement.databinding.ActivityDeleteBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DeleteActivity : ActivitysWithMenuLista() {

    lateinit var binding: ActivityDeleteBinding
    lateinit var adapter: inmersionesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_delete)

        binding = ActivityDeleteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = inmersionesAdapter(mutableListOf())

        binding.botonEliminarInmersion.setOnClickListener {
            if (binding.editTextNombreInmersion.text.isNotEmpty()) {
                val nombreInmersion = binding.editTextNombreInmersion.text.toString()
                borrarInmersion(nombreInmersion)

            } else {
                Toast.makeText(this, "El campo no puede estar vacio", Toast.LENGTH_SHORT).show()
            }
        }

        binding.bAtras.setOnClickListener {
            volverListadoInmersiones()
        }
    }

    fun volverListadoInmersiones(){
        val intent = Intent(this, InmersionesActivity::class.java)
        startActivity(intent)
    }


    fun borrarInmersion(nombre: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val inmersion_borrar =
                miInmersionApp.database.inmersionesDAO().buscarInmersionPorNombre(nombre)
            if (inmersion_borrar.isNotEmpty()) {
                val inmersionBorrada = inmersion_borrar[0]
                miInmersionApp.database.inmersionesDAO().deleteInmersion(inmersionBorrada)

                runOnUiThread {
                    clearTextos()
                    Toast.makeText(
                        this@DeleteActivity,
                        "Inmersion eliminada correctamente",
                        Toast.LENGTH_SHORT
                    ).show()
                    actualizarRecyclerView()
                    adapter.notifyDataSetChanged()
                    volverListadoInmersiones()
                }
            } else {
                runOnUiThread {
                    Toast.makeText(
                        this@DeleteActivity,
                        "Inmersion no encontrada",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }


    private fun actualizarRecyclerView() {
        CoroutineScope(Dispatchers.IO).launch {
            val inmersiones = miInmersionApp.database.inmersionesDAO().getAllInmersiones()
            runOnUiThread {
                adapter.updateInmersionesList(inmersiones)
            }
        }
    }

    fun clearTextos() {
        binding.editTextNombreInmersion.setText("")
    }
}