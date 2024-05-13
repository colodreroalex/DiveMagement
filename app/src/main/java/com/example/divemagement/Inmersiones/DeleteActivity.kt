package com.example.divemagement.Inmersiones

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.divemagement.DB.miInmersionApp
import com.example.divemagement.adapter.inmersionesAdapter
import com.example.divemagement.databinding.ActivityDeleteBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DeleteActivity : AppCompatActivity() {

    lateinit var binding: ActivityDeleteBinding
    lateinit var adapter: inmersionesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_delete)

        binding = ActivityDeleteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = inmersionesAdapter(mutableListOf())

        binding.botonEliminarInmersion.setOnClickListener {
            if (!binding.editTextNombreInmersion.text.isNullOrEmpty()) {
                val nombreInmersion = binding.editTextNombreInmersion.text.toString()
                borrarInmersion(nombreInmersion)

            } else {
                Toast.makeText(this, "The field cannot be empty", Toast.LENGTH_SHORT).show()
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
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirm deletion")
        builder.setMessage("Are you sure you want to delete this dive?")
        builder.setPositiveButton("Sí") { _, _ ->
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
                            "Inmersión correctly deleted",
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
                            "Inmersion not found",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }

        // Agregar el estilo de texto para el botón "Sí" con color rojo
        val alertDialog = builder.show()
        val positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
        positiveButton.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark))
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