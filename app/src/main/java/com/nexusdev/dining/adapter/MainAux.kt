package com.nexusdev.dining.adapter

import com.nexusdev.dining.model.Producto

interface MainAux {

    fun getProductsCart(): MutableList<Producto>
    fun updateTotal()
    fun clearCart()
    fun getProductSelected(): Producto?
    fun showButton(isVisible: Boolean)
    fun addProductToCart(product: Producto)

}