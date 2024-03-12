package com.example.divemagement.ActivitysClientes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.divemagement.R
import com.example.divemagement.databinding.ActivityPerfilClienteBinding
import com.google.android.material.textfield.TextInputEditText

class ActivityPerfilCliente : AppCompatActivity() {

    lateinit var binding: ActivityPerfilClienteBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPerfilClienteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Intenta obtener el email del Intent, si no existe, usa SharedPreferences
        val emailUsuarioRegistrado = intent.getStringExtra("user_email") ?: run {
            val sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
            sharedPreferences.getString("user_email", "No definido")
        }

        binding.email.setText(emailUsuarioRegistrado)




    }
}