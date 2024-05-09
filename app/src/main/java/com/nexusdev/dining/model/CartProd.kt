package com.nexusdev.dining.model

import android.os.Parcel
import android.os.Parcelable

data class CartProd(
    var id: String? = null,
    var image: String? = null,
    var userId: String? = null,
    var name: String? = null,
    var price: Double? = 0.0,
    var quantity: Int? = 0,
    var total: Double? = 0.0,
    var newQuantity: Int? = 0
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Int::class.java.classLoader) as? Int,
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(image)
        parcel.writeString(userId)
        parcel.writeValue(price)
        parcel.writeValue(quantity)
        parcel.writeValue(total)
        parcel.writeValue(newQuantity)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CartProd> {
        override fun createFromParcel(parcel: Parcel): CartProd {
            return CartProd(parcel)
        }

        override fun newArray(size: Int): Array<CartProd?> {
            return arrayOfNulls(size)
        }
    }
}
