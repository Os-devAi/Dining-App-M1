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

class CartActivity : AppCompatActivity(), ProductCartAdapter.ProductCartListener {

    private lateinit var binding: ActivityCartBinding
    private lateinit var adapter: ProductCartAdapter

    private var totalPrice = 0.0
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
        val db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        db.collection(Constants.COLL_CART)
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { result ->
                val productList = mutableListOf<CartProd>()

                for (document in result) {
                    val cartItem = document.toObject(CartProd::class.java)
                    productList.add(cartItem)
                }
                configRecyclerView(productList)
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error al obtener los datos: $exception", Toast.LENGTH_SHORT)
                    .show()
            }
    }


    private fun configRecyclerView(productList: List<CartProd>) {
        adapter =
            ProductCartAdapter(productList.toMutableList(), this)
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(
                this@CartActivity, 1, GridLayoutManager.VERTICAL, false
            )
            adapter = this@CartActivity.adapter
        }
    }

    override fun onAddClicked(product: CartProd) {
        product.quantity = product.quantity!! + 1
        updateProduct(product)
    }

    override fun onRemoveClicked(product: CartProd) {
        if (product.quantity!! > 0) {
            product.quantity = product.quantity!! - 1
            updateProduct(product)
        }
    }

    override fun onDeleteClicked(product: CartProd) {
        product.id?.let {
            FirebaseFirestore.getInstance().collection(Constants.COLL_CART)
                .document(it)
                .delete()
                .addOnSuccessListener {
                    Toast.makeText(this, "Producto eliminado", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error al eliminar el producto", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun updateProduct(product: CartProd) {
        val db = FirebaseFirestore.getInstance()
        val docRef = product.id?.let { db.collection(Constants.COLL_CART).document(it) } ?: return

        docRef.update("quantity", product.quantity)
            .addOnSuccessListener {
                Toast.makeText(this, "Cantidad actualizada", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al actualizar la cantidad", Toast.LENGTH_SHORT).show()
            }
    }


    override fun showTotal(total: Double) {
        totalPrice = total
        binding.tvTotal.text = totalPrice.toString()
    }

    override fun setQuantity(product: CartProd) {
        adapter.update(product)
    }
}
