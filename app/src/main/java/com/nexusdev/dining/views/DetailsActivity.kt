package com.nexusdev.dining.views

import android.content.Intent
import android.graphics.RenderEffect
import android.graphics.Shader
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nexusdev.dining.R
import com.nexusdev.dining.databinding.ActivityDetailsBinding
import com.nexusdev.dining.entities.Constants
import com.nexusdev.dining.model.CartProd
import com.nexusdev.dining.model.Producto

@Suppress("DEPRECATION")
class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding

    private var productos: Producto? = null
    private var estado: String = "Disponible"
    private var cantidadI: Int? = 0
    private var note: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.black)

        getData()
        click()
    }

    private fun click() {
        binding.btnBuyIt.setOnClickListener {
            if (productos!!.estado == estado) {
                note = binding.etNote.text.toString().trim()
                buyNow()
            } else {
                Toast.makeText(
                    this,
                    "Sorry this product is not available, or check the quantity!",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
        binding.btnAddCart.setOnClickListener {
            val usrId = FirebaseAuth.getInstance().currentUser!!.uid
            if (productos!!.estado == estado && binding.etNewQuantity.text.toString()
                    .toInt() > 0
            ) {
                val dataP = CartProd(
                    userId = usrId,
                    name = productos!!.nombre.toString(),
                    price = productos!!.precio,
                    quantity = binding.etNewQuantity.text.toString().toInt(),
                    image = productos!!.imagen,
                    total = productos?.precio.toString()
                        .toDouble() * binding.etNewQuantity.text.toString().toInt()
                )
                addToCart(dataP)
            } else {
                Toast.makeText(
                    this,
                    "Sorry this product is not available, or check the quantity!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun getData() {
        this.productos = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra("producto")
        } else {
            intent.getParcelableExtra("producto")
        }
        if (productos != null) {
            binding.tvName.text = productos!!.nombre
            binding.tvDescription.text = productos!!.descripcion
            binding.tvDisponible.text = productos!!.estado
            binding.tvCategoria.text = productos!!.categoria
            binding.tvTotalPriceQ.text = "$." + productos!!.precio.toString() + "0"
            Glide.with(binding.imgProduct).load(productos!!.imagen).into(binding.imgProduct)
            Glide.with(binding.imgBackground).load(productos!!.imagen).into(binding.imgBackground)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                binding.imgBackground.setRenderEffect(
                    RenderEffect.createBlurEffect(
                        30f,
                        30f,
                        Shader.TileMode.CLAMP
                    )
                )
            }
        }
    }

    private fun addToCart(dataP: CartProd) {
        val db = FirebaseFirestore.getInstance()
        val cartRef = db.collection(Constants.COLL_CART).document()
        val documentId = cartRef.id
        cartRef
            .set(dataP.copy(prodId = documentId))
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val snackbar =
                        Snackbar.make(
                            binding.root,
                            "Product added to cart",
                            Snackbar.LENGTH_SHORT
                        )
                    snackbar.show()
                } else {
                    // Error al crear el documento
                    println("Error adding document to Firestore: ${task.exception}")
                    Toast.makeText(
                        this,
                        "Sorry this product is not available, or check the quantity!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .addOnFailureListener { e ->
                // Error al crear el documento
                println("Failed to add document to Firestore: $e")
                Toast.makeText(
                    this,
                    "Sorry this product is not available, or check the quantity!",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun buyNow() {
        //Fix quantity default 1
        val userName = FirebaseAuth.getInstance().currentUser?.displayName
        var nuevaCantidad = binding.etNewQuantity.text.toString().toInt()
        if (nuevaCantidad.equals(null)) {
            nuevaCantidad = "1".toInt()
        } else {
            setNuevaCantidad(nuevaCantidad)
        }

        var pedido = ""
        pedido = pedido + "Order Details:"
        pedido = pedido + "\n"
        pedido = pedido + "\n"
        pedido = pedido + "Name: $userName"
        pedido = pedido + "\n"
        pedido = pedido + "___________________________"

        val total: Double = productos?.precio.toString().toDouble() * nuevaCantidad
        binding.let {
            pedido = pedido +
                    "\n" +
                    "\n" +
                    "Product: ${productos?.nombre.toString()}" +
                    "\n" +
                    "Quantity: $nuevaCantidad" +
                    "\n" +
                    "Price: $.${productos?.precio.toString()}" +
                    "\n" +
                    "___________________________" +
                    "\n" +
                    "TOTAL: $.${total}" +
                    "\n" +
                    "\n" +
                    "Notes: $.${note.toString()}"
        }

        val url = "https://wa.me/19196561970?text=$pedido"
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }

    private fun setNuevaCantidad(cantidad: Int) {
        if (binding.etNewQuantity.text.isNullOrEmpty()) {
            binding.etNewQuantity.setText("1").toString().toInt()
        } else {
            binding.etNewQuantity.setText("$cantidad")
            cantidadI = cantidad
        }
    }
}
