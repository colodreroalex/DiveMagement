package com.example.divemagement

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.WindowManager
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.divemagement.DB.DbHelper
import com.example.divemagement.databinding.ActivityLoginBinding
import com.example.divemagement.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ActivityWithMenus() {


    private lateinit var dbHelper: DbHelper
    private lateinit var tbUsernameLogin: EditText
    private lateinit var email: EditText
    private lateinit var pass: EditText
    private lateinit var phone: EditText
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        title = "Sign Up"
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        //  Esconder el teclado cuando se inicie la activity de inmersiones
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        ActivityWithMenus.actividadActual = 0

        dbHelper = DbHelper(this)

        tbUsernameLogin = binding.tbUsernameLogin
        email = binding.email
        pass = binding.password
        phone = binding.tlf






        ActivityWithMenus.isLoggedIn = false // Usuario no logeado

        //Comprobar que la bbdd no sea null
        if (dbHelper == null) {
            Toast.makeText(this, "ERROR AL CREAR LA BASE DE DATOS", Toast.LENGTH_SHORT).show()
        }

        binding.bRegister.setOnClickListener {
            val username = binding.tbUsernameLogin.text.toString()
            val password = binding.password.text.toString()
            val tlf = binding.tlf.text.toString()
            val correo = binding.email.text.toString()

            //Comprobar que los campos no estén vacíos
            if (username.isNotEmpty() && password.isNotEmpty() && tlf.isNotEmpty() && correo.isNotEmpty()) {
                mostrarConfirmacionDialog(username, password, tlf, correo)
            } else {
                Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show()
            }
        }


    }


    private fun mostrarConfirmacionDialog(
        username: String,
        password: String,
        tlf: String,
        correo: String
    ) {
        val alerta = AlertDialog.Builder(this)
        alerta.setTitle("Confirmación")
        alerta.setMessage("¿Estás seguro de registrar al cliente?")

        alerta.setPositiveButton("Sí") { dialog, which ->
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(binding.email.text.toString(), binding.password.text.toString())
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        // Insertar al cliente en la base de datos SQLite
                        dbHelper.addClient(username, password, tlf, correo)
                    }
                    else{
                        Toast.makeText(this, "Error al crear el usuario", Toast.LENGTH_SHORT)
                            .show()
                    }
            }

            // Limpiar los campos
            tbUsernameLogin.text.clear()
            email.text.clear()
            pass.text.clear()
            phone.text.clear()

            // Abrir la actividad de login para que el usuario pueda iniciar sesión
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        alerta.setNegativeButton("No") { dialog, which ->
            // No hacer nada si el usuario selecciona "No"
            Toast.makeText(this, "Registro cancelado", Toast.LENGTH_SHORT).show()
        }

        val dialog: AlertDialog = alerta.create()
        dialog.show()
    }


}