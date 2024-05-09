package com.nexusdev.dining.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.nexusdev.dining.R
import com.nexusdev.dining.databinding.ItemProductCartBinding
import com.nexusdev.dining.model.CartProd

class ProductCartAdapter(
    private val productList: MutableList<CartProd>,
    private val listener: ProductCartListener
) :
    RecyclerView.Adapter<ProductCartAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product_cart, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = productList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = productList.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemProductCartBinding.bind(itemView)

        @SuppressLint("SetTextI18n")
        fun bind(product: CartProd) {
            with(binding) {
                // Configuración de otros campos
                tvName.text = product.name
                tvPrice.text = "Q.${product.price.toString()}0"
                tvQuantity.text =
                    product.quantity.toString()  // Muestra la cantidad actual del producto
                Glide.with(imgProduct)
                    .load(product.image)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .circleCrop()
                    .into(imgProduct)

            }
            binding.ibDelete.setOnClickListener {
                listener.onDeleteClicked(product)
            }
        }

        private fun updateQuantity(quantity: Int) {
            val product = productList[adapterPosition]
            product.quantity = quantity
            notifyItemChanged(adapterPosition)
            calcTotal()
        }
    }

    fun add(product: CartProd) {
        if (!productList.contains(product)) {
            productList.add(product)
            notifyItemInserted(productList.size - 1)
            calcTotal()
        } else {
            update(product)
        }
    }

    fun delete(product: CartProd) {
        val index = productList.indexOf(product)
        if (index != -1) {
            productList.removeAt(index)
            notifyItemRemoved(index)
            calcTotal()
        }
    }

    fun update(product: CartProd) {
        val index = productList.indexOf(product)
        if (index != -1) {
            productList.set(index, product)
            notifyItemChanged(index)
            calcTotal()
        }
    }

    private fun calcTotal() {
        var result = 0.0
        for (product in productList) {
            result += product.total!!.toDouble()
        }
        listener.showTotal(result)
    }

    interface ProductCartListener {
        fun onDeleteClicked(product: CartProd)
        fun showTotal(total: Double)
        fun setQuantity(product: CartProd)
    }
}
