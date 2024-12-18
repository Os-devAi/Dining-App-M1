package com.nexusdev.dining.views

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.nexusdev.dining.R
import com.nexusdev.dining.databinding.ActivityInfoBinding

class InfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)

        click()
    }

    private fun click() {
        binding.let {
            it.btnTelefono.setOnClickListener {
                llamarTelefono()
            }
            it.btnWhatsApp.setOnClickListener {
                messageWhtasApp()
            }
            /*it.btnMap.setOnClickListener {
                openGoogleMaps()
            }*/
            it.btnSalir.setOnClickListener {
                val auth = FirebaseAuth.getInstance()
                auth.signOut()
                val i = Intent(this, LoginActivity::class.java)
                startActivity(i)
                finish()
            }
        }
    }

    private fun llamarTelefono() {
        startActivity(Intent(Intent.ACTION_DIAL).setData(Uri.parse("tel:+19196561970")));
    }

    private fun messageWhtasApp() {
        val url = "https://wa.me/19196561970"
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }

    fun openGoogleMaps() {
        val uri = "http://maps.google.com/maps?q=loc:14.671496,-90.535240"
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(uri)
        startActivity(i)
    }
}