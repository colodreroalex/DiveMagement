package com.example.divemagement

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.divemagement.DB.DbHelper
import com.example.divemagement.adapter.inmersionesAdapter
import com.example.divemagement.databinding.ActivityDeleteInmersionBinding

class Delete_inmersion : ActivitysWithMenuLista() {

    lateinit var dbHelper: DbHelper
    private lateinit var binding: ActivityDeleteInmersionBinding
    private lateinit var adapter: inmersionesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        title = "Eliminar Inmersion"
        super.onCreate(savedInstanceState)
        binding = ActivityDeleteInmersionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Esconder el teclado cuando se inicie la activity de inmersiones
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        dbHelper = DbHelper(this)

        binding.botonEliminarInmersion.setOnClickListener {


            val nombreInmersion = binding.editTextNombreInmersion.text.toString()



            if (!nombreInmersion.isNullOrEmpty()) {

                if (dbHelper.existeInmersionOno(nombreInmersion)) {
                    dbHelper.deleteInmersionPorNombre(nombreInmersion)

                    // Actualiza la lista de inmersiones en el adaptador
                    val inmersionesList: List<Inmersion> = dbHelper.getInmersiones()
                    adapter.updateInmersionesList(inmersionesList)
                } else {
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Error")
                    builder.setMessage("No existe ninguna inmersion con ese nombre")
                    builder.setPositiveButton("Aceptar", null)
                    val dialog: AlertDialog = builder.create()
                    dialog.show()
                }

            } else {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Error")
                builder.setMessage("El campo nombre no puede estar vacio")
                builder.setPositiveButton("Aceptar", null)
                val dialog: AlertDialog = builder.create()
                dialog.show()
            }

        }

        binding.botonAtras.setOnClickListener {
            Intent(this, InmersionesActivity::class.java).also {
                startActivity(it)
            }
        }
    }
}