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
) :
    RecyclerView.Adapter<ProductCartAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
        return ViewHolder(view.inflate(R.layout.item_product_cart, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = productList[position]
        holder.render(item)
    }

    override fun getItemCount(): Int = productList.size

    fun update(product: CartProd) {
        val index = productList.indexOf(product)
        if (index != -1) {
            productList.set(index, product)
            notifyItemChanged(index)
        }
    }

    fun delete(product: CartProd) {
        val index = productList.indexOf(product)
        if (index != -1) {
            productList.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    fun getProducts(): List<CartProd> = productList



    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun render(product: CartProd) {
            val binding = ItemProductCartBinding.bind(itemView)
            binding.tvName.text = product.name
            binding.tvPrice.text = "Q.${product.price.toString()}0"
            binding.tvQuantity.text = product.quantity.toString()
            val img = binding.imgProduct
            Glide
                .with(img)
                .load(product.image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .circleCrop()
                .into(img)
        }

    }
}