package com.example.divemagement.ActivitysClientes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.divemagement.DB.miInmersionApp
import com.example.divemagement.R
import com.example.divemagement.databinding.ActivityHistorialClientesBinding
import com.example.divemagement.databinding.ActivityPerfilClienteBinding

class ActivityHistorialClientes : AppCompatActivity() {

    lateinit var binding: ActivityHistorialClientesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistorialClientesBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }


}