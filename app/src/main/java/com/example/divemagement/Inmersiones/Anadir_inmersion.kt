package com.example.divemagement.Inmersiones

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.divemagement.DB.ListaInmersiones
import com.example.divemagement.DB.miInmersionApp
import com.example.divemagement.R
import com.example.divemagement.adapter.inmersionesAdapter
import com.example.divemagement.databinding.ActivityAnadirInmersionBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class Anadir_inmersion : AppCompatActivity() {


    private lateinit var binding: ActivityAnadirInmersionBinding
    private lateinit var adapter: inmersionesAdapter

    //Variable ResultLauncher que se encargará de abrir la galeria
    private lateinit var resultado: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
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
            saveInmersion()
        }

        binding.salir.setOnClickListener {
            adapter.notifyDataSetChanged()
            volverListadoInmersiones()
        }
    }

    private fun saveInmersion() {
        val name = binding.editTextNombre.text.toString()
        val prof = binding.editTextProfundidad.text.toString().toFloat()
        val date = binding.editTextFecha.text.toString()
        val hour = binding.editTextHora.text.toString()
        val vis = binding.editTextVisibilidad.text.toString()
        val temp = binding.editTextTemperatura.text.toString().toFloat()
        val place = binding.editTextLugar.text.toString()
        val desc = binding.editTextDescripcion.text.toString()
        val foto = binding.imagenButton.toString()

        if(!checkProfundidad(prof) || !checkTemperatura(temp)){
            //Este return sirve para que no se ejecute el código
            //de abajo si la profundidad o la temperatura no son correctas
            return
        }

        if (!binding.editTextNombre.text.isNullOrEmpty() && !binding.editTextProfundidad.text.isNullOrEmpty() && !binding.editTextFecha.text.isNullOrEmpty() && !binding.editTextHora.text.isNullOrEmpty() && !binding.editTextVisibilidad.text.isNullOrEmpty() && !binding.editTextTemperatura.text.isNullOrEmpty() && !binding.editTextVisibilidad.text.isNullOrEmpty() && !binding.editTextLugar.text.isNullOrEmpty() && !binding.editTextDescripcion.text.isNullOrEmpty()) {
            AlertDialog.Builder(this)
                .setTitle("Confirmación")
                .setMessage("Are you sure you want to insert this dive?")
                .setPositiveButton("Yes") { _, _ ->
                    CoroutineScope(Dispatchers.IO).launch {
                        val inmersion = miInmersionApp.database.inmersionesDAO()
                            .buscarInmersionPorNombre(binding.editTextNombre.text!!.toString())
                        if (inmersion.isEmpty()) {
                            miInmersionApp.database.inmersionesDAO().insertInmersion(
                                ListaInmersiones(
                                    nombre = name,
                                    profundidad = prof,
                                    fecha = date,
                                    hora = hour,
                                    visibilidad = vis,
                                    temperatura = temp,
                                    lugar = place,
                                    descripcion = desc,
                                    photo = foto
                                )
                            )
                            runOnUiThread {
                                limpiarCampos()
                                Toast.makeText(
                                    this@Anadir_inmersion,
                                    "Inserted immersion",
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
                                    "This dive is already in the database",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
                .setNegativeButton("No", null)
                .show()
        } else {
            Toast.makeText(this, "No field can be empty", Toast.LENGTH_SHORT).show()
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

    private fun checkProfundidad(profundidad: Float): Boolean{
        if(profundidad < 1){
            Toast.makeText(this, "The depth cannot be less than 0 meters", Toast.LENGTH_SHORT).show()
            return false
        }
        if(profundidad > 40){
            Toast.makeText(this, "The depth cannot be greater than 40 meters", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun checkTemperatura(temperatura: Float): Boolean{
        if(temperatura < 0){
            Toast.makeText(this, "The temperature cannot be less than 0 degrees", Toast.LENGTH_SHORT).show()
            return false
        }
        if(temperatura > 35){
            Toast.makeText(this, "The temperature cannot be higher than 35 degrees", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }





}