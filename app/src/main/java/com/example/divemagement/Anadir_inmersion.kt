package com.example.divemagement

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
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

    //Variable ResultLauncher que se encargará de abrir la galeria
    private lateinit var resultado: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        title = "Añadir Inmersion"
        super.onCreate(savedInstanceState)
        binding = ActivityAnadirInmersionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Esconder el teclado cuando se inicie la activity de inmersiones
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        dbHelper = DbHelper(this)
        val sqlite = dbHelper.writableDatabase

        inizializarGaleria()

        binding.buttonGuardar.setOnClickListener {
            // Crear un AlertDialog
            AlertDialog.Builder(this)
                .setTitle("Confirmar")
                .setMessage("¿Deseas guardar los datos?")
                .setPositiveButton("Sí") { _, _ ->
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

                            // Volver a ListadoActivity.kt
                            val intent = Intent(this, InmersionesActivity::class.java)
                            startActivity(intent)
                            finish()

                        } else {
                            // Muestra un mensaje de error o realiza alguna acción en caso de datos inválidos.
                            Toast.makeText(
                                this,
                                "Por favor, ingresa datos válidos para Profundidad y Temperatura",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: Exception) {
                        // Maneja cualquier excepción que pueda ocurrir durante el proceso.
                        e.printStackTrace()
                    }
                }
                .setNegativeButton("No", null)
                .show()
        }


        binding.salir.setOnClickListener {
            finish()
        }
    }

    private fun inizializarGaleria() {
        //Inicializamos el ResultLauncher con el método registerForActivityResult y el contrato ActivityResultContracts.StartActivityForResult
        resultado = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            //Comprobamos que el resultado sea RESULT_OK
            if (result.resultCode == RESULT_OK) {
                //Recogemos el intent que nos ha devuelto la galeria
                val data: Intent? = result.data

                //Comprobamos que el intent no sea null
                if (data != null) {
                    //Recogemos la uri de la imagen seleccionada
                    val uri = data.data
                    //Comprobamos que la uri no sea null
                    if (uri != null) {
                        //Mostramos un mensaje con la uri de la imagen seleccionada
                        Toast.makeText(this, "Imagen seleccionada: $uri", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        val imagenSeleccionada: ImageButton = binding.imagenButton
        imagenSeleccionada.setOnClickListener {
            //Creamos un intent para abrir la galeria
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            //Lanzamos el intent con el ResultLauncher
            resultado.launch(intent)
        }
    }
}