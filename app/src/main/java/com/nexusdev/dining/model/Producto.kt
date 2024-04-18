package com.nexusdev.dining.model

import android.os.Parcel
import android.os.Parcelable

data class Producto(
    var id: String? = null,
    var nombre: String? = null,
    var descripcion: String? = null,
    var precio: Double? = 0.0,
    var imagen: String? = null,
    var estado: String? = null,
    var categoria: String? = null,
    var total: Double? = 0.0
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Double::class.java.classLoader) as? Double
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(nombre)
        parcel.writeString(descripcion)
        parcel.writeValue(precio)
        parcel.writeString(imagen)
        parcel.writeString(estado)
        parcel.writeString(categoria)
        parcel.writeValue(total)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Producto> {
        override fun createFromParcel(parcel: Parcel): Producto {
            return Producto(parcel)
        }

        override fun newArray(size: Int): Array<Producto?> {
            return arrayOfNulls(size)
        }
    }
}
