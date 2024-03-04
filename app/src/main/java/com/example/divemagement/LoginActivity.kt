package com.example.divemagement

import android.content.Intent
import android.os.Bundle
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

        CoroutineScope(Dispatchers.IO).launch{
            val inmersionesDao = miInmersionApp.database.inmersionesDAO()
            for (inmersion in inmersiones){
                inmersionesDao.insertInmersion(inmersion)
            }
        }


    }

    override fun onPause() {
        super.onPause()
        email.text.clear()
        tbPasswordLogin.text.clear()
    }


    //Metodo para logear con FireBase al Admin - probisional
    private fun login(){

        val email = binding.email.text.toString()
        val password = binding.tbPasswordLogin.text.toString()


        if(email.isNotEmpty() && password.isNotEmpty()){

            if(email == "admin@admin.com" && password == "adminadmin"){

                FirebaseAuth.getInstance().signInWithEmailAndPassword(
                    binding.email.text.toString(),
                    binding.tbPasswordLogin.text.toString()
                ).addOnCompleteListener{
                    //Si la autenticacion tuvo exito
                    if(it.isSuccessful){
                        //Abrir la actividad de inmersiones
                        val intent = Intent(this, InmersionesActivity::class.java)
                        startActivity(intent)
                        ActivityWithMenus.isLoggedIn = true // Usuario logeado
                    }else{ //Si la autenticacion no tuvo exito
                        Toast.makeText(this, "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else{
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Error")
                builder.setMessage("¡Debes ser ADMIN para entrar!")
                builder.setPositiveButton("Aceptar", null)
                val dialog: AlertDialog = builder.create()
                dialog.show()

                //Limpiar los campos
                binding.email.text.clear()
                binding.tbPasswordLogin.text.clear()
            }

        }else{
            Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show()
        }

    }

}