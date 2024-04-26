package com.nexusdev.dining.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.nexusdev.dining.R
import com.nexusdev.dining.databinding.ItemProductCartBinding
import com.nexusdev.dining.model.Producto

class ProductCartAdapter(
    private val productList: MutableList<Producto>,
    private val listener: OnCartListener
) :
    RecyclerView.Adapter<ProductCartAdapter.ViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_product_cart, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = productList[position]

        holder.setListenner(product)

        holder.binding.tvName.text = product.nombre
        holder.binding.tvQuantity.text = product.newQuantity.toString()

        Glide.with(context)
            .load(product.imagen)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .circleCrop()
            .into(holder.binding.imgProduct)
    }

    override fun getItemCount(): Int = productList.size

    fun add(product: Producto) {
        if (!productList.contains(product)) {
            productList.add(product)
            notifyItemInserted(productList.size - 1)
            calcTotal()
        } else {
            update(product)
        }
    }

    fun update(product: Producto) {
        val index = productList.indexOf(product)
        if (index != -1) {
            productList.set(index, product)
            notifyItemChanged(index)
            calcTotal()
        }
    }

    fun delete(product: Producto) {
        val index = productList.indexOf(product)
        if (index != -1) {
            productList.removeAt(index)
            notifyItemRemoved(index)
            calcTotal()
        }
    }

    private fun calcTotal() {
        var result = 0.0
        for (product in productList) {
        }
        listener.showTotal(result)
    }

    fun getProducts(): List<Producto> = productList

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemProductCartBinding.bind(view)

        fun setListenner(product: Producto) {
            binding.ibSum.setOnClickListener {
                product.newQuantity = product.newQuantity!! + 1
                listener.setQuantity(product)
            }
            binding.ibSub.setOnClickListener {
                product.newQuantity = product.newQuantity!! - 1
                listener.setQuantity(product)
            }
        }
    }
}