package com.nexusdev.dining.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.nexusdev.dining.R
import com.nexusdev.dining.databinding.ItemListViewBinding
import com.nexusdev.dining.model.Producto

class MenuAdapter(private var itemList: MutableList<Producto>) :
    RecyclerView.Adapter<MenuAdapter.ViewHolder>() {

    //itemClick
    var onItemClick: ((Producto) -> Unit)? = null

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun render(producto: Producto) {
            val binding = ItemListViewBinding.bind(itemView)
            binding.productName.text = producto.nombre
            binding.currentPrice.text = "$." + producto.precio.toString() + "0"
            val imgUrl = producto.imagen
            val img = binding.productImage
            Glide
                .with(img)
                .load(imgUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(img)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_list_view, parent, false))
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.render(item)

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(item)
        }
    }

    fun add(data: Producto) {
        if (!itemList.contains(data)) {
            itemList.add(data)
            notifyItemInserted(itemList.size - 1)
        }
    }

    fun clearItems() {
        itemList.clear()
        notifyDataSetChanged()
    }
}