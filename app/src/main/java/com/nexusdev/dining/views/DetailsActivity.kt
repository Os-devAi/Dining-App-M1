package com.nexusdev.dining.views

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.nexusdev.dining.R
import com.nexusdev.dining.databinding.ActivityDetailsBinding
import com.nexusdev.dining.model.Producto

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding

    private var productos: Producto? = null
    private var estado: String = "disponible"
    private var db = FirebaseFirestore.getInstance()
    private var productID: String? = null
    var cantidadI: Int? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)

        getData()
        click()
    }

    private fun click() {
        binding.btnBuyIt.setOnClickListener {
            if (productos!!.estado == estado) {
                buyNow()
            } else {
                Toast.makeText(
                    this,
                    "Lo sentimos no hay disponibles o la cantidad a ordenar no es correcta",
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
            productID = productos!!.id
            binding.tvName.text = productos!!.nombre
            binding.tvDescription.text = productos!!.descripcion
            binding.tvDisponible.text = productos!!.estado
            binding.tvCategoria.text = productos!!.categoria
            Glide.with(binding.imgProduct).load(productos!!.imagen).into(binding.imgProduct)
            Glide.with(binding.imgBackground).load(productos!!.imagen).into(binding.imgBackground)
        }
    }


    private fun buyNow() {
        //Fix quantity default 1
        var nuevaCantidad = binding.etNewQuantity.text.toString().toInt()
        if (nuevaCantidad.equals(null)) {
            nuevaCantidad = "1".toInt()
        } else {
            setNuevaCantidad(nuevaCantidad)
        }

        var pedido = ""
        pedido = pedido + "Detalles de mi pedido:" + "\n"
        pedido += "\n"
        pedido += "\n"
        pedido += "___________________________"

        val total: Double = productos?.precio.toString().toDouble() * nuevaCantidad
        binding.let {
            pedido = pedido +
                    "\n" +
                    "\n" +
                    "Producto: ${productos?.nombre.toString()}" +
                    "\n" +
                    "Cantidad: $nuevaCantidad" +
                    "\n" +
                    "Precio: Q. ${productos?.precio.toString()}" +
                    "\n" +
                    "___________________________" +
                    "\n" +
                    "TOTAL: Q.${total}"
        }

        val url = "https://wa.me/50241642429?text=$pedido"
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