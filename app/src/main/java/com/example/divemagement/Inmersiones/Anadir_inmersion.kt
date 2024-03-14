package com.example.divemagement.Inmersiones

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.divemagement.ActivitysWithMenuLista
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

        inicializarGaleria()

        binding.imagenButton.setOnClickListener {
            abrirGaleria()
        }


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
                            limpiarCampos()
                            Toast.makeText(
                                this@Anadir_inmersion,
                                "Inmersion insertada",
                                Toast.LENGTH_SHORT
                            ).show()
                            actualizarRecyclerView()
                            adapter.notifyDataSetChanged()
                            volverListadoInmersiones()
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
            //finish()
            volverListadoInmersiones()
        }
    }

    fun actualizarRecyclerView() {
        CoroutineScope(Dispatchers.IO).launch {
            val inmersiones = miInmersionApp.database.inmersionesDAO().getAllInmersiones()
            runOnUiThread {
                adapter.updateInmersionesList(inmersiones)
            }
        }
    }

    fun volverListadoInmersiones(){
        val intent = Intent(this, InmersionesActivity::class.java)
        startActivity(intent)
    }

    fun limpiarCampos() {
        binding.editTextNombre.setText("")
        binding.editTextProfundidad.setText("")
        binding.editTextFecha.setText("")
        binding.editTextHora.setText("")
        binding.editTextTemperatura.setText("")
        binding.editTextVisibilidad.setText("")
        binding.editTextLugar.setText("")
        binding.editTextDescripcion.setText("")
    }


    // Inicializa el ActivityResultLauncher
    fun inicializarGaleria() {
        resultado = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data: Intent? = result.data
                if (data != null) {
                    val uri = data.data
                    if (uri != null) {
                        binding.imagenButton.setImageURI(uri) // Actualiza directamente el ImageButton con la imagen seleccionada
                    }
                }
            }
        }
    }

    // Lanza el intent para abrir la galería
    fun abrirGaleria() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        resultado.launch(intent)
    }



}