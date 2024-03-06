package com.example.divemagement

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.divemagement.DB.miInmersionApp
import com.example.divemagement.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : ActivityWithMenus() {


    private lateinit var email: EditText
    private lateinit var tbPasswordLogin: EditText
    public lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        title = "Login"
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Esconder el teclado cuando se inicie la activity de inmersiones
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        ActivityWithMenus.actividadActual = 1

        val inmersiones = InmersionesProvider.inmersionesList

        email = binding.email
        tbPasswordLogin = binding.tbPasswordLogin

        binding.bLogin.setOnClickListener {
            login()
        }

        binding.bRegisterLogin.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        CoroutineScope(Dispatchers.IO).launch {
            val inmersionesDao = miInmersionApp.database.inmersionesDAO()
            inmersiones.forEach { inmersion ->
                // Ahora buscarInmersionPorNombre retorna un solo objeto o null
                val existente = inmersionesDao.buscarInmersionPorNombreLista(inmersion.nombre)
                if (existente == null) {
                    // Si no existe, insertamos la inmersión
                    inmersionesDao.insertInmersion(inmersion)
                }
            }
        }



    }

    override fun onPause() {
        super.onPause()
        email.text.clear()
        tbPasswordLogin.text.clear()
    }


    //Metodo para logear con FireBase al Admin - probisional
    private fun login() {
        val email = binding.email.text.toString().trim()
        val password = binding.tbPasswordLogin.text.toString().trim()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Determina el rol basado en las credenciales; esto es solo un ejemplo.
                    val rol = if (email == "admin@admin.com" && password == "adminadmin") "ADMIN" else "USER"
                    Log.d("LoginActivity", "Rol: $rol")
                    // Actualiza el rol en la base de datos local de forma asíncrona.
                    CoroutineScope(Dispatchers.IO).launch {
                        miInmersionApp.database.clientesDAO().asignarRol(email, password, rol)
                        withContext(Dispatchers.Main) {
                            // Una vez actualizado el rol, procede según el rol asignado.
                            if (rol == "ADMIN") {
                                val intent = Intent(this@LoginActivity, InmersionesActivity::class.java)
                                startActivity(intent)
                            } else {
                                val intent = Intent(this@LoginActivity, ClientesActivity::class.java)
                                startActivity(intent)
                            }
                            ActivityWithMenus.isLoggedIn = true // Usuario logeado
                        }
                    }
                } else {
                    // Manejo de error en caso de que la autenticación falle.
                    Toast.makeText(this, "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show()
        }
    }


    //Metodo para limpiar los campos
    private fun limpiarCampos(){
        email.text.clear()
        tbPasswordLogin.text.clear()
    }

}