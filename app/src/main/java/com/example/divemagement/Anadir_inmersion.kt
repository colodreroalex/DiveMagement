package com.example.divemagement

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.divemagement.DB.ListaInmersiones
import com.example.divemagement.DB.miInmersionApp
import com.example.divemagement.adapter.inmersionesAdapter
import com.example.divemagement.databinding.ActivityAnadirInmersionBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class Anadir_inmersion : ActivitysWithMenuLista() {


    private lateinit var binding: ActivityAnadirInmersionBinding
    private lateinit var adapter: inmersionesAdapter

    //Variable ResultLauncher que se encargará de abrir la galeria
    private lateinit var resultado: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        title = "Añadir Inmersion"
        super.onCreate(savedInstanceState)
        binding = ActivityAnadirInmersionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adapter = inmersionesAdapter(mutableListOf())
        // Esconder el teclado cuando se inicie la activity de inmersiones
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        inizializarGaleria()

        binding.buttonGuardar.setOnClickListener {
            if (binding.editTextNombre.text.isNotEmpty() && binding.editTextProfundidad.text.isNotEmpty() && binding.editTextFecha.text.isNotEmpty() && binding.editTextHora.text.isNotEmpty() && binding.editTextVisibilidad.text.isNotEmpty() && binding.editTextTemperatura.text.isNotEmpty() && binding.editTextVisibilidad.text.isNotEmpty() && binding.editTextLugar.text.isNotEmpty() && binding.editTextDescripcion.text.isNotEmpty()) {
                CoroutineScope(Dispatchers.IO).launch {
                    val inmersion = miInmersionApp.database.inmersionesDAO()
                        .buscarInmersionPorNombre(binding.editTextNombre.text.toString())
                    if (inmersion.isEmpty()) {
                        miInmersionApp.database.inmersionesDAO().insertInmersion(
                            ListaInmersiones(
                                nombre = binding.editTextNombre.text.toString(),
                                profundidad = binding.editTextProfundidad.text.toString().toFloat(),
                                fecha = binding.editTextFecha.text.toString(),
                                hora = binding.editTextHora.text.toString(),
                                visibilidad = binding.editTextVisibilidad.text.toString(),
                                temperatura = binding.editTextTemperatura.text.toString().toInt()
                                    .toFloat(),
                                lugar = binding.editTextLugar.text.toString(),
                                descripcion = binding.editTextDescripcion.text.toString()

                            )

                        )
                        runOnUiThread {
                            clearCampos()
                            Toast.makeText(
                                this@Anadir_inmersion,
                                "Inmersion insertada",
                                Toast.LENGTH_SHORT
                            ).show()
                            actualizarRecyclerView()
                            adapter.notifyDataSetChanged()
                            finish()
                        }
                    } else {
                        runOnUiThread {
                            Toast.makeText(
                                this@Anadir_inmersion,
                                "Esta inmersion ya está en la base de datos",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                }
            } else {
                Toast.makeText(this, "Ningun campo puede estar vacío", Toast.LENGTH_SHORT).show()
            }
        }

        binding.salir.setOnClickListener {
            adapter.notifyDataSetChanged()
            finish()
        }
    }

    fun actualizarRecyclerView() {
        CoroutineScope(Dispatchers.IO).launch {
            val personajes = miInmersionApp.database.inmersionesDAO().getAllInmersiones()
            runOnUiThread {
                adapter.updateInmersionesList(personajes)
            }
        }
    }

    fun clearCampos() {
        binding.editTextNombre.setText("")
        binding.editTextProfundidad.setText("")
        binding.editTextFecha.setText("")
        binding.editTextHora.setText("")
        binding.editTextTemperatura.setText("")
        binding.editTextVisibilidad.setText("")
        binding.editTextLugar.setText("")
        binding.editTextDescripcion.setText("")
    }


    fun inizializarGaleria() {
        //Inicializamos el ResultLauncher con el método registerForActivityResult y el contrato ActivityResultContracts.StartActivityForResult
        resultado =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
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
                            Toast.makeText(
                                this,
                                "Imagen seleccionada: $uri",
                                Toast.LENGTH_SHORT
                            ).show()
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