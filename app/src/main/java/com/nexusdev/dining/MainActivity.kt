package com.nexusdev.dining

import android.content.Intent
import android.os.Build
import android.os.Build.VERSION_CODES
import android.os.Build.VERSION_CODES.LOLLIPOP
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.nexusdev.dining.R.color.*
import com.nexusdev.dining.adapter.MenuAdapter
import com.nexusdev.dining.databinding.ActivityMainBinding
import com.nexusdev.dining.entities.Constants
import com.nexusdev.dining.model.Producto
import com.nexusdev.dining.views.CartFragment
import com.nexusdev.dining.views.DetailsActivity
import com.nexusdev.dining.views.InfoActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var adapter: MenuAdapter
    private lateinit var firestoreListener: ListenerRegistration
    private var estado: String = "Disponible"


    private val productCartList = mutableListOf<Producto>()


    private var isHoySelected = false
    private var isTodoSelected = false

    @RequiresApi(VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        window.statusBarColor = ContextCompat.getColor(this, black)
        window.navigationBarColor = ContextCompat.getColor(this, black)


        //Set Username
        val username = FirebaseAuth.getInstance().currentUser?.displayName
        binding.titleName.text = username

        binding.btnHoy.setChipBackgroundColorResource(green)
        getHoy(estado)
        click()
    }

    override fun onResume() {
        super.onResume()
        getHoy(estado)
    }

    private fun click() {
        binding.let {
            it.btnHoy.setOnClickListener {
                isHoySelected = true
                if (this.isHoySelected) {
                    binding.btnHoy.setChipBackgroundColorResource(green)
                    binding.btnTodos.setChipBackgroundColorResource(black_chip)
                }

                adapter.clearItems()
                getHoy(estado)
            }
            it.btnTodos.setOnClickListener {
                isTodoSelected = true
                if (this.isTodoSelected) {

                    binding.btnHoy.setChipBackgroundColorResource(black_chip)
                    binding.btnTodos.setChipBackgroundColorResource(green)
                }

                adapter.clearItems()
                getData()
            }
            it.btnInfo.setOnClickListener {
                val i = Intent(this, InfoActivity::class.java)
                startActivity(i)
            }
            it.btnViewCart.setOnClickListener {
                val fragment = CartFragment()
                fragment.show(
                    supportFragmentManager.beginTransaction(),
                    CartFragment::class.java.simpleName
                )
            }
        }
    }

    private fun getHoy(estado: String) {
        val db = FirebaseFirestore.getInstance()

        val prodRef = db.collection(Constants.COLL_MENU)
        firestoreListener =
            prodRef.orderBy("precio")
                .whereEqualTo("estado", estado).addSnapshotListener { value, error ->
                    if (error != null) {
                        Toast.makeText(this, "Error al consultar datos", Toast.LENGTH_SHORT).show()
                        return@addSnapshotListener
                    }

                    val productosList = mutableListOf<Producto>()

                    for (value in value!!.documentChanges) {
                        val product = value.document.toObject(Producto::class.java)
                        product.id = value.document.id

                        when (value.type) {
                            DocumentChange.Type.ADDED -> productosList.add(product)
                            else -> {}
                        }
                    }

                    configRecyclerView(productosList)
                }
    }

    private fun getData() {
        val db = FirebaseFirestore.getInstance()

        val prodRef = db.collection(Constants.COLL_MENU)
        firestoreListener = prodRef.addSnapshotListener { value, error ->
            if (error != null) {
                Toast.makeText(this, "Error al consultar datos", Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }

            val productosList = mutableListOf<Producto>()

            for (value in value!!.documentChanges) {
                val product = value.document.toObject(Producto::class.java)
                product.id = value.document.id

                when (value.type) {
                    DocumentChange.Type.ADDED -> productosList.add(product)
                    else -> {}
                }
            }

            configRecyclerView(productosList)
        }
    }

    private fun configRecyclerView(producList: List<Producto>) {
        adapter = MenuAdapter(producList.toMutableList())
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(
                this@MainActivity, 2, GridLayoutManager.VERTICAL, false
            )
            adapter = this@MainActivity.adapter
        }
        adapter.onItemClick = {
            val i = Intent(this@MainActivity, DetailsActivity::class.java)
            i.putExtra("producto", it)
            startActivity(i)
        }
    }

    fun getProductsCart(): MutableList<Producto> = productCartList

    fun showButton(isVisible: Boolean) {
        binding.btnViewCart.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    fun addProductToCart(product: Producto) {
        val index = productCartList.indexOf(product)

        if (index != -1) {
            productCartList[index] = product
        } else {
            productCartList.add(product)
        }

        updateTotal()
    }

    private fun updateTotal() {
        var total = 0.0
        productCartList.forEach { product ->
            total += product.total.toString().toDouble()
        }
    }

    fun clearCart() {
        productCartList.clear()
    }
}