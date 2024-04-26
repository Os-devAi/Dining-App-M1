package com.nexusdev.dining.adapter

import com.nexusdev.dining.model.Producto

interface OnCartListener {

    fun setQuantity(product: Producto)
    fun showTotal(total: Double)

    fun setCantidad(p: Producto)

}