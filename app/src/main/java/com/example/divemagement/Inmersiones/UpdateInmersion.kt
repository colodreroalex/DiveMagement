package com.example.divemagement.Inmersiones

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.divemagement.DB.miInmersionApp
import com.example.divemagement.adapter.inmersionesAdapter
import com.example.divemagement.databinding.ActivityUpdateInmersionBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UpdateInmersion : AppCompatActivity() {

    lateinit var binding: ActivityUpdateInmersionBinding
    lateinit var adapter: inmersionesAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUpdateInmersionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = inmersionesAdapter(mutableListOf())

        binding.botonActualizarInmersion.setOnClickListener{
            if(!binding.editTextNombreInmersion.text.isNullOrEmpty() && !binding.editTextProfundidad.text.isNullOrEmpty() && !binding.editTextTemperatura.text.isNullOrEmpty()){
                val nuevaProf = binding.editTextProfundidad.text.toString().toFloat()
                val nuevaTemp = binding.editTextTemperatura.text.toString().toFloat()
                updateInmersion(nuevaProf, nuevaTemp)
            } else {
                Toast.makeText(this, "Ningun campo puede estar vacío", Toast.LENGTH_SHORT).show()
            }
        }

        binding.bAtras.setOnClickListener {
            volverListadoInmersiones()
        }
    }

    private fun updateInmersion(nuevaProf: Float, nuevaTemp: Float) {
        CoroutineScope(Dispatchers.IO).launch {
            val inmersiones = miInmersionApp.database.inmersionesDAO().buscarInmersionPorNombre(binding.editTextNombreInmersion.text.toString())
            if (inmersiones.isNotEmpty()) {
                val inmersion = inmersiones[0]
                inmersion.profundidad = nuevaProf
                inmersion.temperatura = nuevaTemp
                miInmersionApp.database.inmersionesDAO().updateInmersion(inmersion)
                runOnUiThread {
                    clearTextos()
                    Toast.makeText(this@UpdateInmersion, "Inmersion actualizada correctamente", Toast.LENGTH_SHORT).show()
                    actualizarRecyclerView()
                    adapter.notifyDataSetChanged()
                    volverListadoInmersiones()
                }
            } else {
                runOnUiThread {
                    Toast.makeText(this@UpdateInmersion, "Esta inmersion no existe en la base de datos", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun actualizarRecyclerView() {
        CoroutineScope(Dispatchers.IO).launch {
            val inmersiones = miInmersionApp.database.inmersionesDAO().getAllInmersiones()
            adapter.updateInmersionesList(inmersiones)

        }
    }

    fun clearTextos() {
        binding.editTextNombreInmersion.setText("")
        binding.editTextProfundidad.setText("")

    }

    fun volverListadoInmersiones(){
        val intent = Intent(this, InmersionesActivity::class.java)
        startActivity(intent)
    }
}