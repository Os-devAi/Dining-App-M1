package com.nexusdev.dining.views

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.nexusdev.dining.R
import com.nexusdev.dining.adapter.ProductCartAdapter
import com.nexusdev.dining.databinding.ActivityCartBinding
import com.nexusdev.dining.entities.Constants
import com.nexusdev.dining.model.CartProd

@Suppress("DEPRECATION")
class CartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding
    private lateinit var adapter: ProductCartAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.black)

        getCartItems()
        clicks()
    }

    private fun clicks() {
        binding.close.setOnClickListener {
            onBackPressed()
            finish()
        }
    }

    private fun getCartItems() {
        // Fetch cart items from the database or API
        val db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        db.collection(Constants.COLL_CART)
            .whereEqualTo("userId", userId)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                val productosList = mutableListOf<CartProd>()

                for (value in value!!.documentChanges) {
                    val cartItem = value.document.toObject(CartProd::class.java)
                    if (value.type == DocumentChange.Type.ADDED) {
                        productosList.add(cartItem)
                    }
                }
                configRecyclerView(productosList)
            }
    }

    private fun configRecyclerView(producList: List<CartProd>) {
        adapter = ProductCartAdapter(producList.toMutableList())
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(
                this@CartActivity, 1, GridLayoutManager.VERTICAL, false
            )
            adapter = this@CartActivity.adapter
        }
    }
}