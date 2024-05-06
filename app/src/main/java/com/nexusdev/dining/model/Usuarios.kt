package com.nexusdev.dining.model

import android.os.Parcel
import android.os.Parcelable

data class Usuarios(
    var id: String? = null,
    var nombre: String? = null,
    var apellido: String? = null,
    var email: String? = null,
    var password: String? = null,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(nombre)
        parcel.writeString(apellido)
        parcel.writeString(email)
        parcel.writeString(password)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Usuarios> {
        override fun createFromParcel(parcel: Parcel): Usuarios {
            return Usuarios(parcel)
        }

        override fun newArray(size: Int): Array<Usuarios?> {
            return arrayOfNulls(size)
        }
    }
}