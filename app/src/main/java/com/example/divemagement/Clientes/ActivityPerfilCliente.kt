package com.example.divemagement.Clientes

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.divemagement.DB.miInmersionApp
import com.example.divemagement.adapter.clientesAdapter

import com.example.divemagement.databinding.ActivityPerfilClienteBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ActivityPerfilCliente : AppCompatActivity() {

    lateinit var binding: ActivityPerfilClienteBinding
    lateinit var adapter: clientesAdapter


    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        // Handle the returned Uri
        binding.imageButton.setImageURI(uri)
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityPerfilClienteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //PopUp informando al Usuario que en este apartado podrá modificar sus datos
        AlertDialog.Builder(this)
            .setTitle("Actualizar datos")
            .setMessage("Aquí podrás modificar tu nombre y teléfono.")
            .setPositiveButton("Entendido", null)
            .show()

        // Intenta obtener el email del Intent, si no existe, usa SharedPreferences
        val emailUsuarioRegistrado = intent.getStringExtra("user_email") ?: run {
            val sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
            sharedPreferences.getString("user_email", "No definido")
        }

        // Asigna el email al TextView
        binding.email.setText(emailUsuarioRegistrado)

        //Al pulsar en el boton se deben actualizar los datos del usuario
        binding.updateButton.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Actualizar datos")
                .setMessage("¿Estás seguro de que quieres actualizar los datos?")
                .setPositiveButton("Sí") { _, _ ->
                    if(binding.username.toString().isNotEmpty() && binding.phone.text.toString().isNotEmpty()){
                        //Actualizar datos de ese cliente en la bbdd
                        val nombre = binding.username.text.toString()
                        val telefono = binding.phone.text.toString()

                        updateCliente(nombre, telefono)

                    } else {
                        //Mostrar mensaje de error
                        Toast.makeText(this, "Ningun campo puede estar vacío", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("No", null)
                .show()
        }

        binding.imageButton.setOnClickListener {
            getContent.launch("image/*")
        }

        binding.exitButton.setOnClickListener {
            volverClientesActivity()
        }


    }


    private fun updateCliente(nombre: String, telefono: String) {
        //Actualizar datos del cliente en la bbdd
        CoroutineScope(Dispatchers.IO).launch {

            //Obtener el cliente de la bbdd por el email desde el cual ingreso
            val cliente = miInmersionApp.database.clientesDAO().buscarClientePorEmail(binding.email.text.toString())[0]

            //Actualizar datos del cliente
            cliente.username = nombre
            cliente.telefono = telefono

            //Actualizar cliente en la bbdd
            miInmersionApp.database.clientesDAO().updateCliente(cliente)

            //Mostrar mensaje de éxito
            runOnUiThread {
                clearTextos()
                Toast.makeText(this@ActivityPerfilCliente, "Cliente actualizado correctamente", Toast.LENGTH_SHORT).show()
                actualizarRecyclerView()
                adapter.notifyDataSetChanged()
                volverClientesActivity()
            }
        }
    }

    fun clearTextos() {
        binding.username.setText("")
        binding.phone.setText("")
    }

    fun actualizarRecyclerView() {
        CoroutineScope(Dispatchers.IO).launch {
            val clientes = miInmersionApp.database.clientesDAO().getAllClientes()
            adapter.actualizarClientesList(clientes)
        }
    }

    fun volverClientesActivity(){
        val intent = Intent(this, ClientesActivity::class.java)
        startActivity(intent)
    }

}