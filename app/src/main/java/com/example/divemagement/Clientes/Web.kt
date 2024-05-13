package com.example.divemagement.Clientes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import com.example.divemagement.databinding.ActivityWebBinding

class Web : AppCompatActivity() {

    lateinit var binding: ActivityWebBinding
    private val BASE_URL = "https://www.google.com"
    private val DIVINGCLUB_WEBSITE = "https://bluewhale-divers.com/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //WebView
        binding.webView.webChromeClient = object : WebChromeClient(){

        }

        binding.webView.webViewClient = object : WebViewClient(){

        }

        val settings = binding.webView.settings
        settings.javaScriptEnabled = true

        binding.webView.loadUrl(DIVINGCLUB_WEBSITE)


    }


}