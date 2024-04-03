package com.example.divemagement.Inmersiones

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.divemagement.Clientes.ClientesActivity
import com.example.divemagement.DB.miInmersionApp
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
        //val photo = intent.getStringExtra("photo")

        //Mostramos los datos en los TextView
        binding.editTextNombre.text = nombre?.let { Editable.Factory.getInstance().newEditable(it) }
        binding.editTextProfundidad.text = profundidad.toString().let { Editable.Factory.getInstance().newEditable(it) }
        binding.editTextFecha.text = fecha?.let { Editable.Factory.getInstance().newEditable(it) }
        binding.editTextHora.text = hora?.let { Editable.Factory.getInstance().newEditable(it) }
        binding.editTextVisibilidad.text = visibilidad?.let { Editable.Factory.getInstance().newEditable(it) }
        binding.editTextTemperatura.text = temperatura.toString().let { Editable.Factory.getInstance().newEditable(it) }
        binding.editTextLugar.text = lugar?.let { Editable.Factory.getInstance().newEditable(it) }
        binding.editTextDescripcion.text = descripcion?.let { Editable.Factory.getInstance().newEditable(it) }
        //binding.imagenButton.setImageURI(photo?.let { Editable.Factory.getInstance().newEditable(it) })

        /*Aquí estamos utilizando el operador de llamada segura ?., que permite que nombre,prof,fecha,etc sea nulo.
        Si es no nulo, se ejecutará el bloque de código dentro de las llaves {}.*/

        binding.bReservarInmersion.setOnClickListener {
            //El it hace referencia al view que ha llamado a la función
            reservaInmersion(it)
        }

        binding.bSalir.setOnClickListener {
            val intent = Intent(this, ClientesActivity::class.java)
            startActivity(intent)
        }

    }

    //Funcion para que al hacer click en el boton de reserva se pregunte al usuario si esta seguro de reservar la inmersion
    fun reservaInmersion(view: View) {
        val constructorDialogo = AlertDialog.Builder(this)
        constructorDialogo.setTitle("Reservar inmersión")
        constructorDialogo.setMessage("¿Estás seguro de reservar esta inmersión?")
        constructorDialogo.setPositiveButton("Sí") { dialog, which ->
            // Si el usuario pulsa en sí, se asigna la inmersión al cliente
            CoroutineScope(Dispatchers.IO).launch {
                // Obtener el nombre del cliente y el ID del cliente.
                val emailCliente = FirebaseAuth.getInstance().currentUser?.email.toString()
                val idCliente = miInmersionApp.database.clientesDAO().getIdClientePorEmail(emailCliente)

                // Obtener el nombre de la inmersión.
                val nombreInmersion = intent.getStringExtra("nombre").toString()

                // Verificar si el cliente existe en la tabla de clientes.
                val clienteExiste = miInmersionApp.database.clientesDAO().buscarClientePorEmail(emailCliente) != null && miInmersionApp.database.clientesDAO().getClientById(idCliente) != null

                // Verificar si la inmersión existe en la tabla de inmersiones.
                val inmersionExiste = miInmersionApp.database.inmersionesDAO().buscarInmersionPorNombre(nombreInmersion).isNotEmpty()

                if (clienteExiste && inmersionExiste) {
                    // Si ambos existen, llamar al método `asignarInmersionCliente`.
                    val idInmersion = miInmersionApp.database.inmersionesDAO().buscarInmersionPorNombre(nombreInmersion)[0].id
                    miInmersionApp.database.clientesInmersionesDAO().asignarInmersionCliente(idCliente, idInmersion)

                    // Mostrar un mensaje de éxito.
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@ReservaInmersion, "Inmersión reservada", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Si alguno no existe, mostrar un mensaje de error correspondiente.
                    withContext(Dispatchers.Main) {
                        if (!clienteExiste) {
                            Toast.makeText(this@ReservaInmersion, "Error: Cliente no encontrado", Toast.LENGTH_SHORT).show()
                        }
                        if (!inmersionExiste) {
                            Toast.makeText(this@ReservaInmersion, "Error: Inmersión no encontrada", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
        constructorDialogo.setNegativeButton("No") { dialog, which ->
            // Si el usuario pulsa en no, se cierra el diálogo.
            dialog.dismiss()
        }
        constructorDialogo.show()
    }



}