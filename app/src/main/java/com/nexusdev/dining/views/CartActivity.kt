package com.nexusdev.dining.views

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nexusdev.dining.R
import com.nexusdev.dining.adapter.ProductCartAdapter
import com.nexusdev.dining.databinding.ActivityCartBinding
import com.nexusdev.dining.entities.Constants
import com.nexusdev.dining.model.CartProd

@Suppress("DEPRECATION")
class CartActivity : AppCompatActivity(), ProductCartAdapter.ProductCartListener {

    private lateinit var binding: ActivityCartBinding
    private lateinit var adapter: ProductCartAdapter
    private var productList: MutableList<CartProd> = mutableListOf()
    private var note: String? = null

    private var productID: String? = null
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
            this.onBackPressed()
            finish()
        }
        binding.efab.setOnClickListener {
            note = binding.etNote.text.toString().trim()
            sendMessage()
        }
    }

    private fun getCartItems() {
        val db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        db.collection(Constants.COLL_CART)
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { result ->
                productList = mutableListOf<CartProd>()

                for (document in result) {
                    val cartItem = document.toObject(CartProd::class.java)
                    productList.add(cartItem)
                }
                configRecyclerView(productList)
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error fetching cart items: $exception", Toast.LENGTH_SHORT)
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

    override fun onDeleteClicked(product: CartProd) {
        val db = FirebaseFirestore.getInstance()
        val docId = product.prodId
        if (docId != null) {
            db.collection(Constants.COLL_CART)
                .document(docId)
                .delete()
                .addOnSuccessListener {
                    Toast.makeText(
                        this,
                        "Product ${product.prodId.toString()} was deleted successfully",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    adapter.clear()
                    getCartItems()
                    showTotal(totalPrice)
                }
                .addOnFailureListener {
                    Toast.makeText(
                        this,
                        "Some error occurred while deleting the product",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

    override fun showTotal(total: Double) {
        totalPrice = total
        binding.tvTotal.text = "Total: $${totalPrice}0"
    }

    override fun setQuantity(product: CartProd) {
        adapter.update(product)
    }

    private fun sendMessage() {
        val userName = FirebaseAuth.getInstance().currentUser?.displayName
        if (productList.isEmpty()) {
            val snackbar =
                Snackbar.make(
                    binding.root,
                    "Your cart is empty",
                    Snackbar.LENGTH_SHORT
                )
            snackbar.show()
        } else {
            var pedido = ""

            pedido = pedido + "Order Details:"
            pedido = pedido + "\n"
            pedido = pedido + "\n"
            pedido = pedido + "Name: $userName"
            pedido = pedido + "\n"
            pedido = pedido + "___________________________"

            var index = 0
            while (index < productList.size) {
                pedido = "$pedido" +
                        "\n" +
                        "\n" +
                        "Product: ${productList[index].name}" +
                        "\n" +
                        "Price: $.${productList[index].price}" +
                        "\n" +
                        "Quantity: ${productList[index].quantity}" +
                        "\n" +
                        "___________________________\n"
                index++
            }

            pedido = pedido + "Total: $.$totalPrice"
            pedido = pedido + "\n"
            pedido = pedido + "\n"
            pedido = pedido + "Notes: \$.${note.toString()}\""
            pedido = pedido + "Thank you for your order!"

            val url = "https://wa.me/19196561970?text=$pedido"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }

    }
}
