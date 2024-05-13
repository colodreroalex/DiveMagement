package com.example.divemagement.Inmersiones

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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
                val nuevaVisibilidad = binding.editTextVisibilidad.text.toString()
                updateInmersion(nuevaProf, nuevaTemp, nuevaVisibilidad)
            } else {
                Toast.makeText(this, "No field can be empty", Toast.LENGTH_SHORT).show()
            }
        }

        binding.bAtras.setOnClickListener {
            volverListadoInmersiones()
        }
    }

    private fun updateInmersion(nuevaProf: Float, nuevaTemp: Float, nuevaVisibilidad: String ) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirm update")
        builder.setMessage("Are you sure you want to upgrade the dive?")
        builder.setPositiveButton("Yes") { _, _ ->
            CoroutineScope(Dispatchers.IO).launch {
                val inmersiones =
                    miInmersionApp.database.inmersionesDAO().buscarInmersionPorNombre(binding.editTextNombreInmersion.text.toString())
                if (inmersiones.isNotEmpty()) {
                    val inmersion = inmersiones[0]
                    inmersion.profundidad = nuevaProf
                    inmersion.temperatura = nuevaTemp
                    inmersion.visibilidad = nuevaVisibilidad
                    miInmersionApp.database.inmersionesDAO().updateInmersion(inmersion)
                    runOnUiThread {
                        clearTextos()
                        Toast.makeText(
                            this@UpdateInmersion,
                            "Dive updated successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        actualizarRecyclerView()
                        adapter.notifyDataSetChanged()
                        volverListadoInmersiones()
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(
                            this@UpdateInmersion,
                            "This dive does not exist in the database",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }

        // Establecer el color del botón "Sí" en rojo
        val alertDialog = builder.show()
        val positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
        positiveButton.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark))
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