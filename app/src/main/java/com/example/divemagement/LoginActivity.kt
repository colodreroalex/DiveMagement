package com.example.divemagement

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import com.example.divemagement.DB.DbHelper
import com.example.divemagement.databinding.ActivityLoginBinding

class LoginActivity : ActivityWithMenus() {

    private lateinit var dbHelper: DbHelper
    private lateinit var tbUsernameLogin: EditText
    private lateinit var tbPasswordLogin: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ActivityWithMenus.actividadActual = 1

        dbHelper = DbHelper(this)

        tbUsernameLogin = binding.tbUsernameLogin
        tbPasswordLogin = binding.tbPasswordLogin

        binding.bLogin.setOnClickListener {
            val username = binding.tbUsernameLogin.text.toString()
            val password = binding.tbPasswordLogin.text.toString()
            login(username, password)
        }

        binding.bRegisterLogin.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onPause() {
        super.onPause()
        tbUsernameLogin.text.clear()
        tbPasswordLogin.text.clear()
    }

    //Metodo para logearse
    private fun login(username: String, password: String) {
        //Comprobar que los campos no estén vacíos
        if (username.isNotEmpty() && password.isNotEmpty()) {
            //Comprobar que el usuario existe en la bbdd
            if (dbHelper.checkUser(username, password)) {
                Toast.makeText(this, "Login correcto", Toast.LENGTH_SHORT).show()
                //Abrir la actividad de inmersiones
                val intent = Intent(this, InmersionesActivity::class.java)
                startActivity(intent)
                ActivityWithMenus.isLoggedIn = true // Usuario logeado
            } else {
                Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show()
        }
    }
}