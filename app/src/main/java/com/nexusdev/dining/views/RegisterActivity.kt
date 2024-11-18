package com.nexusdev.dining.views

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.nexusdev.dining.MainActivity
import com.nexusdev.dining.R
import com.nexusdev.dining.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.black)

        binding.btnCreate.setOnClickListener {
            getRegister()
        }
        binding.btnCancel.setOnClickListener {
            clearData()
            this.onBackPressed()
            this.finish()
        }
    }

    private fun getRegister() {
        disableUI()

        val nombre = binding.nombre.text.toString()
        val apellido = binding.apellido.text.toString()
        val email = binding.email.text.toString()
        val password = binding.password.text.toString()

        if (nombre.isEmpty() || apellido.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert()
            enableUI()
        } else {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = FirebaseAuth.getInstance().currentUser
                        val profileUpdate = UserProfileChangeRequest.Builder()
                            .setDisplayName("$nombre $apellido").build()

                        user?.updateProfile(profileUpdate)
                        clearData()
                        onBackPressed()
                    } else {
                        showAlert()
                        enableUI()
                    }
                }
        }
    }

    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Error al registrar usuario revisa todos los datos.")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun goHome() {
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
        this.finish()
    }

    private fun clearData() {
        binding.nombre.text.clear()
        binding.apellido.text.clear()
        binding.email.text.clear()
        binding.password.text.clear()
    }

    private fun disableUI() {
        binding.nombre.isEnabled = false
        binding.apellido.isEnabled = false
        binding.email.isEnabled = false
        binding.password.isEnabled = false
        binding.btnCreate.isEnabled = false
        binding.btnCancel.isEnabled = false
    }

    private fun enableUI() {
        binding.nombre.isEnabled = true
        binding.apellido.isEnabled = true
        binding.email.isEnabled = true
        binding.password.isEnabled = true
        binding.btnCreate.isEnabled = true
        binding.btnCancel.isEnabled = true
    }
}