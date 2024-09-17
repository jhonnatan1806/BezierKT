package com.example.myktactil

import android.os.Parcel
import android.os.Parcelable

data class Nodo(
    val idNodo: Int,
    val x: Float,
    val y: Float,
    val nombre: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(idNodo)
        parcel.writeFloat(x)
        parcel.writeFloat(y)
        parcel.writeString(nombre)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Nodo> {
        override fun createFromParcel(parcel: Parcel): Nodo {
            return Nodo(parcel)
        }

        override fun newArray(size: Int): Array<Nodo?> {
            return arrayOfNulls(size)
        }
    }
}
