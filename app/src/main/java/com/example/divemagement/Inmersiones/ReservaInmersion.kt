package com.example.divemagement.Inmersiones

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.divemagement.DB.ListaClientesInmersiones
import com.example.divemagement.DB.ListaInmersiones
import com.example.divemagement.DB.miInmersionApp
import com.example.divemagement.R
import com.example.divemagement.databinding.ActivityReservaInmersionBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReservaInmersion : AppCompatActivity() {

    lateinit var binding: ActivityReservaInmersionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReservaInmersionBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //Recogemos los datos del intent
        val nombre = intent.getStringExtra("nombre")
        val profundidad = intent.getFloatExtra("profundidad", 0.0F)
        val fecha = intent.getStringExtra("fecha")
        val hora = intent.getStringExtra("hora")
        val visibilidad = intent.getStringExtra("visibilidad")
        val temperatura = intent.getFloatExtra("temperatura", 0.0F)
        val lugar = intent.getStringExtra("lugar")
        val descripcion = intent.getStringExtra("descripcion")
        val photo = intent.getStringExtra("photo")

        //Mostramos los datos en los TextView
        binding.editTextNombre.text = nombre?.let { Editable.Factory.getInstance().newEditable(it) }
        binding.editTextProfundidad.text = profundidad.toString().let { Editable.Factory.getInstance().newEditable(it) }
        binding.editTextFecha.text = fecha?.let { Editable.Factory.getInstance().newEditable(it) }
        binding.editTextHora.text = hora?.let { Editable.Factory.getInstance().newEditable(it) }
        binding.editTextVisibilidad.text = visibilidad?.let { Editable.Factory.getInstance().newEditable(it) }
        binding.editTextTemperatura.text = temperatura.toString().let { Editable.Factory.getInstance().newEditable(it) }
        binding.editTextLugar.text = lugar?.let { Editable.Factory.getInstance().newEditable(it) }
        binding.editTextDescripcion.text = descripcion?.let { Editable.Factory.getInstance().newEditable(it) }

        binding.bReservarInmersion.setOnClickListener {
            //El it hace referencia al view que ha llamado a la función
            reservaInmersion(it)
        }

    }

    //Funcion para que al hacer click en el boton de reserva se pregunte al usuario si esta seguro de reservar la inmersion
    fun reservaInmersion(view: View) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Reservar inmersion")
        builder.setMessage("¿Estas seguro de reservar esta inmersion?")
        builder.setPositiveButton("Si") { dialog, which ->
            //Si el usuario pulsa en si, se asigna la inmersion al usuario
            CoroutineScope(Dispatchers.IO).launch {
                val userId = FirebaseAuth.getInstance().currentUser?.uid
                val inmersionId = intent.getIntExtra("inmersionId", 0)
                if (userId != null) {
                    miInmersionApp.database.clientesInmersionesDAO().insertClienteInmersion(
                        ListaClientesInmersiones(userId.toInt(), inmersionId)
                    )
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@ReservaInmersion, "Inmersion reservada", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        builder.setNegativeButton("No") { dialog, which ->
            //Si el usuario pulsa en no, se cierra el dialogo
            dialog.dismiss()
        }
        builder.show()
    }


}