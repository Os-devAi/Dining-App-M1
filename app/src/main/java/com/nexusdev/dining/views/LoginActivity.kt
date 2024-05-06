package com.nexusdev.dining.views

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.nexusdev.dining.MainActivity
import com.nexusdev.dining.R
import com.nexusdev.dining.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.black)

        checkUser()
        click()
    }

    private fun click() {
        binding.let {
            it.btnLogin.setOnClickListener {
                if (binding.email.text.toString().isEmpty() || binding.password.text.toString()
                        .isEmpty()
                ) {
                    val snackbar =
                        Snackbar.make(
                            binding.root,
                            "Please fill in all fields",
                            Snackbar.LENGTH_SHORT
                        )
                    snackbar.show()
                    return@setOnClickListener
                } else {
                    val snackbar =
                        Snackbar.make(binding.root, "Logging in...", Snackbar.LENGTH_SHORT)
                    snackbar.show()
                }
                login(binding.email.text.toString(), binding.password.text.toString())
            }
            it.btnRegister.setOnClickListener {
                val i = Intent(this, RegisterActivity::class.java)
                startActivity(i)
            }
        }
    }

    private fun login(email: String, password: String) {
        disableUI()
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener
                val i = Intent(this, MainActivity::class.java)
                startActivity(i)
                enableUI()
                finish()
            }
            .addOnFailureListener {
                val snackbar =
                    Snackbar.make(binding.root, "Login Failed", Snackbar.LENGTH_SHORT)
                snackbar.show()
                enableUI()
            }
    }

    private fun checkUser() {
        val uid = FirebaseAuth.getInstance().uid
        if (uid != null) {
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            finish()
        }
    }

    private fun disableUI() {
        binding.email.isEnabled = false
        binding.password.isEnabled = false
        binding.btnRegister.isEnabled = false
        binding.btnLogin.isEnabled = false
    }

    private fun enableUI() {
        binding.email.isEnabled = true
        binding.password.isEnabled = true
        binding.btnLogin.isEnabled = true
        binding.btnRegister.isEnabled = true
    }
}