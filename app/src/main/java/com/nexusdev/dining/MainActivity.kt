package com.nexusdev.dining

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.nexusdev.dining.adapter.MenuAdapter
import com.nexusdev.dining.databinding.ActivityMainBinding
import com.nexusdev.dining.entities.Constants
import com.nexusdev.dining.model.Producto
import com.nexusdev.dining.views.DetailsActivity
import com.nexusdev.dining.views.InfoActivity
import com.nexusdev.dining.views.InfoFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var adapter: MenuAdapter
    private lateinit var firestoreListener: ListenerRegistration
    private var estado: String = "disponible"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)

        getHoy(estado)
        configRecyclerView()
        click()
    }


    private fun click() {
        binding.let {
            it.btnHoy.setOnClickListener {
                adapter.clearItems()
                getHoy(estado)
            }
            it.btnTodos.setOnClickListener {
                adapter.clearItems()
                getData()
            }
            it.btnInfo.setOnClickListener {
                val i = Intent(this, InfoActivity::class.java)
                startActivity(i)
            }
        }
    }

    private fun getHoy(estado: String) {
        val db = FirebaseFirestore.getInstance()

        val productRef = db.collection(Constants.COLL_MENU)
        firestoreListener =
            productRef
                .orderBy("precio")
                .whereEqualTo("estado", estado).addSnapshotListener { snpashot, error ->
                    if (error != null) {
                        Toast.makeText(this, "Error al consultar datos", Toast.LENGTH_SHORT).show()
                        return@addSnapshotListener
                    }

                    for (snapshot in snpashot!!.documentChanges) {
                        val product = snapshot.document.toObject(Producto::class.java)
                        product.id = snapshot.document.id

                        when (snapshot.type) {
                            DocumentChange.Type.ADDED -> adapter.add(product)
                            else -> {}
                        }
                    }
                }
    }

    private fun getData() {
        val db = FirebaseFirestore.getInstance()

        val productRef = db.collection(Constants.COLL_MENU)
        firestoreListener = productRef.orderBy("precio").addSnapshotListener { snpashot, error ->
            if (error != null) {
                Toast.makeText(this, "Error al consultar datos", Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }

            for (snapshot in snpashot!!.documentChanges) {
                val product = snapshot.document.toObject(Producto::class.java)
                product.id = snapshot.document.id

                when (snapshot.type) {
                    DocumentChange.Type.ADDED -> adapter.add(product)
                    else -> {}
                }
            }
        }
    }

    private fun configRecyclerView() {
        adapter = MenuAdapter(mutableListOf())
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(
                this@MainActivity, 2,
                GridLayoutManager.VERTICAL, false
            )
            adapter = this@MainActivity.adapter
        }

        adapter.onItemClick = {
            val i = Intent(this@MainActivity, DetailsActivity::class.java)
            i.putExtra("producto", it)
            startActivity(i)
        }
    }
}