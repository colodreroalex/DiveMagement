package com.example.divemagement

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.divemagement.DB.DbHelper
import com.example.divemagement.adapter.inmersionesAdapter
import com.example.divemagement.databinding.ActivityAnadirInmersionBinding


class Anadir_inmersion : ActivitysWithMenuLista() {

    lateinit var dbHelper: DbHelper
    private lateinit var binding: ActivityAnadirInmersionBinding
    private lateinit var adapter: inmersionesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        title = "Añadir Inmersion"
        super.onCreate(savedInstanceState)
        binding = ActivityAnadirInmersionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Esconder el teclado cuando se inicie la activity de inmersiones
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        dbHelper = DbHelper(this)
        val sqlite = dbHelper.writableDatabase

        binding.buttonGuardar.setOnClickListener {
            try {
                val nombre = binding.editTextNombre.text.toString()
                val profundidad = binding.editTextProfundidad.text.toString().toFloatOrNull()
                val fecha = binding.editTextFecha.text.toString()
                val hora = binding.editTextHora.text.toString()
                val temperatura = binding.editTextTemperatura.text.toString().toFloatOrNull()
                val visibilidad = binding.editTextVisibilidad.text.toString()
                val lugar = binding.editTextLugar.text.toString()
                val descripcion = binding.editTextDescripcion.text.toString()


                if (nombre.isNotEmpty() && profundidad != null && temperatura != null) {
                    val inmersion = Inmersion(
                        InmersionesProvider.inmersionesList.size + 1,
                        nombre,
                        profundidad,
                        fecha,
                        hora,
                        temperatura,
                        visibilidad,
                        lugar,
                        descripcion,
                        null
                    )

                    dbHelper.insertInmersion(inmersion)
                    val updatedInmersionesList = dbHelper.getInmersiones()

                    // Update the data set of your RecyclerView's adapter
                    adapter.updateInmersionesList(updatedInmersionesList)

                    // Notify the adapter that the data set has changed
                    adapter.notifyDataSetChanged()

                    // Realiza las operaciones necesarias y cierra la conexión a la base de datos.
                    dbHelper.close()

                    finish()
                } else {
                    // Muestra un mensaje de error o realiza alguna acción en caso de datos inválidos.
                    // Por ejemplo, puedes mostrar un Toast.
                    Toast.makeText(
                        this,
                        "Por favor, ingresa datos válidos para Profundidad y Temperatura",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                // Maneja cualquier excepción que pueda ocurrir durante el proceso.
                e.printStackTrace()
                // Puedes mostrar un mensaje de error o realizar alguna acción específica aquí.
            }
        }

        binding.salir.setOnClickListener {
            finish()
        }
    }
}