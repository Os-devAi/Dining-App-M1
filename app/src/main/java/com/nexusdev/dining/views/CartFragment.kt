package com.nexusdev.dining.views

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nexusdev.dining.R
import com.nexusdev.dining.adapter.MainAux
import com.nexusdev.dining.adapter.OnCartListener
import com.nexusdev.dining.adapter.ProductCartAdapter
import com.nexusdev.dining.databinding.FragmentCartBinding
import com.nexusdev.dining.model.Producto

class CartFragment : BottomSheetDialogFragment(), OnCartListener {

    private var binding: FragmentCartBinding? = null

    private lateinit var bottomSheetBehaivor: BottomSheetBehavior<*>
    private lateinit var adapter: ProductCartAdapter
    private var product: Producto? = null
    private var totalPrice = 0.0

    private var number: String = ""

    private val listOnCart: MutableList<Producto> = mutableListOf()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = FragmentCartBinding.inflate(LayoutInflater.from(activity))
        binding?.let {
            val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
            bottomSheetDialog.setContentView(it.root)

            bottomSheetBehaivor = BottomSheetBehavior.from(it.root.parent as View)
            bottomSheetBehaivor.state = BottomSheetBehavior.STATE_EXPANDED

            setupRecyclerView()
            setupButtoms()

            getProducts()

            return bottomSheetDialog
        }

        return super.onCreateDialog(savedInstanceState)
    }

    private fun setupRecyclerView() {
        binding?.let {
            adapter = ProductCartAdapter(mutableListOf(), this)

            it.recyclerView.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = this@CartFragment.adapter
            }
        }
    }

    private fun setupButtoms() {
        binding?.let {
            it.ibCancel.setOnClickListener {
                bottomSheetBehaivor.state = BottomSheetBehavior.STATE_HIDDEN
            }
            it.efab.setOnClickListener {

            }
        }
    }

    private fun getProducts() {
        (activity as? MainAux)?.getProductsCart()?.forEach {
            adapter.add(it)
            listOnCart.addAll(listOf(it))
        }
    }

    override fun setQuantity(product: Producto) {
        TODO("Not yet implemented")
    }

    override fun showTotal(total: Double) {
        TODO("Not yet implemented")
    }

    override fun setCantidad(p: Producto) {
        TODO("Not yet implemented")
    }
}